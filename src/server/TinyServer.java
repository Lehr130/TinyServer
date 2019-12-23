package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bean.MyRequest;
import bean.ParsedResult;
import config.ConfigFacade;
import processor.Processor;
import processor.ProcessorFactory;
import utils.UrlUtils;

/**
 * @author Lehr
 * @date 2019年12月10日
 */
public class TinyServer {

	private ConfigFacade config = ConfigFacade.getInstance();


	/**
	 * 采用阻塞队列，线程安全，及时把请求从accept队列里拿上来，底层accpet队列的长度可由backlog参数修改
	 */
	private Queue<Socket> socketBuffer = new ArrayBlockingQueue<>(config.getAttributeInteger("SOCKET_BUFFER_LEN"));

	/**
	 * 启动整个服务器并响应服务，大概步骤都在这个方法里面 1.加载配置文件 2.主线程接收请求并放入队列 3.工作线程开始从请求队列里取出请求并开始响应
	 * 
	 * @throws IOException
	 * 
	 * @throws Exception
	 */

	public void startUp() throws IOException {

		
		//这里其实就相当于Tomcat的连接器Connector  但是我不知道他为什么要给每一个请求都创一个连接器，可能是处理多个端口？？？不确定
		//那然后他把我后面相当于doIt的部分又单独做成了HttpProcessor 我也不知道为什么这样new一个来单独执行.....
		
		// 加载配置文件

		/*
		 * 创建线程池 线程池不允许使用Executors去创建 而是通过ThreadPoolExecutor的方式 这样的处理方式让写的同学更加明确线程池的运行规则
		 * 规避资源耗尽的风险
		 */
		ExecutorService e = Executors.newFixedThreadPool(config.getAttributeInteger("THREAD_POOL_SIZE"));

		// 开始监听端口，并用try-resources自动关闭资源
		try (ServerSocket server = new ServerSocket(config.getAttributeInteger("LISTEN_PORT"))) {

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
	 * 单个线程处理请求，还需要设计异常链
	 * @param socket
	 */
	public void doIt(Socket socket) {

		try {
			
			// 接收请求
			MyRequest request = new MyRequest(socket);

			// 分析请求类型：静态还是动态请求
			ParsedResult result;
			result = UrlUtils.parseUri(request.getUri());
			// 获取请求类型对应的处理器，但我不知道这样每次new一个出来好不好
			Processor processor = ProcessorFactory.createProcessor(result.getType());

			// 执行处理任务
			processor.processRequest(socket, request, result);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
