package server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bean.MyRequest;
import bean.ParsedResult;
import config.ServerConfig;
import exceptions.BadRequestMethodException;
import exceptions.CannotFindException;
import exceptions.IllegalParamInputException;
import exceptions.ParamException;
import exceptions.SystemFileException;
import message.Code;
import message.Message;
import message.RequestType;
import message.ServerType;
import utils.UrlUtils;

/**
 * @author Lehr
 * @date 2019年12月10日
 */
public class TinyServer {

	/**
	 * TinyServer服务器内置缓存
	 */
	private Cache cache;

	/**
	 * 动态方法路由记录的表
	 */
	private Router router;

	/**
	 * 采用阻塞队列，线程安全，及时把请求从accept队列里拿上来，底层accpet队列的长度可由backlog参数修改
	 */
	private Queue<Socket> socketBuffer = new ArrayBlockingQueue<>(ServerConfig.SOCKET_BUFFER_LEN);

	/**
	 * 启动整个服务器并响应服务，大概步骤都在这个方法里面 1.加载配置文件 2.主线程接收请求并放入队列 3.工作线程开始从请求队列里取出请求并开始响应
	 * 
	 * @throws Exception
	 */

	public void startUp() throws Exception {

		// 加载配置文件
		try {
			cache = Cache.getInstance();
			router = Router.getInstance();
		} catch (SystemFileException e) {
			e.printStackTrace();
			e.getMessage();
		}

		/*
		 * 创建线程池 线程池不允许使用Executors去创建 而是通过ThreadPoolExecutor的方式 这样的处理方式让写的同学更加明确线程池的运行规则
		 * 规避资源耗尽的风险
		 */
		ExecutorService e = Executors.newFixedThreadPool(ServerConfig.THREAD_POOL_SIZE);

//		ServerSocketChannel server = ServerSocketChannel.open();
//		server.bind(new InetSocketAddress(ServerConfig.LISTEN_PORT));
//		SocketChannel socket = server.accept();
//		

		// 开始监听端口，并用try-resources自动关闭资源
		try (ServerSocket server = new ServerSocket(ServerConfig.LISTEN_PORT)) {

			while (true) {

				// 请求放入队列里
				socketBuffer.add(server.accept());

				// 工作线程取出请求并工作
				e.execute(() -> doIt(socketBuffer.remove()));

			}
		}

		// 关闭方法解决的不是很好欸

	}

	/**
	 * 我自己抽出来的一层，用于报错处理
	 * 
	 * @param socket
	 */

	public void doIt(Socket socket) {

		try {
			// 处理请求
			serveIt(socket);
		} catch (InvocationTargetException | IllegalAccessException | IOException e) {
			// SocketIO报错 or 反射报错
			ServeUtils.clientError(socket, Message.DEFAULT_HTTP_VERSION, Code.INTERNALSERVERERROR, cache);
		} catch (IllegalParamInputException e) {
			// 入参非法（类型不对）
			ServeUtils.clientError(socket, Message.DEFAULT_HTTP_VERSION, Code.PARAMILLEGAL, cache);
		} catch (BadRequestMethodException e) {
			// 请求方式不对
			ServeUtils.clientError(socket, Message.DEFAULT_HTTP_VERSION, Code.BADREQUEST, cache);
		} catch (ParamException e) {
			// 参数错误（名字or个数）
			ServeUtils.clientError(socket, Message.DEFAULT_HTTP_VERSION, Code.PARAMWRONG, cache);
		} catch (CannotFindException e) {
			// 找不到方法or资源
			ServeUtils.clientError(socket, Message.DEFAULT_HTTP_VERSION, Code.NOTFOUND, cache);
		}

	}

	/**
	 * 具体的服务执行阶段：解析请求-->进行处理-->返回结果
	 * 
	 * @param server
	 * @throws IOException
	 * @throws BadRequestMethodException
	 * @throws ParamException
	 * @throws CannotFindException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalParamInputException
	 * @throws Exception
	 */

	public void serveIt(Socket socket) throws IOException, BadRequestMethodException, IllegalAccessException,
			InvocationTargetException, CannotFindException, ParamException, IllegalParamInputException {

		// 接收请求
		MyRequest request = new MyRequest(socket);

		// 分析请求类型：静态还是动态请求
		ParsedResult result = UrlUtils.parseUri(request.getUri());

		// 分析结果并分别响应
		ServerType serverType = result.getType();

		if (ServerType.STATIC_RESOURCES.equals(serverType)) {

			// 如果是静态，则只能是GET方法
			if (!RequestType.GET.equals(request.getRequestType())) {

				// 抛出错误：不支持的请求方法
				throw new BadRequestMethodException();

			}

			// 静态文件处理
			ServeUtils.serverStatic(result.getParseUri(), socket, Message.DEFAULT_HTTP_VERSION, cache);

		}
		if (ServerType.DYNAMIC_JAVA.equals(serverType)) {

			// 动态JavaSE程序处理
			ServeUtils.serverDynamic(request, result, socket, router);

		}
		if (ServerType.PROXY.equals(serverType)) {
			ServeUtils.serverProxy(result, socket);
		}

	}

}
