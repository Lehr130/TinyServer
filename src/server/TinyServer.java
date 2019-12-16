package server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bean.MyMethod;
import bean.MyRequest;
import bean.MyResponse;
import bean.ParsedResult;
import exceptions.ParamNameException;
import exceptions.SystemFileException;
import main.dynamic.Router;
import message.Code;
import message.RequestType;
import utils.FileUtils;
import utils.UrlUtils;

/**
 * @author Lehr
 * @date 2019年12月10日
 */
public class TinyServer {

	/**
	 * TinyServer服务器内置缓存
	 */
	private MyCache cache;

	/**
	 * Router动态方法表
	 */
	private Router router;

	/**
	 * 采用阻塞队列，暂时保证了线程安全
	 */
	private Queue<Socket> socketBuffer = new ArrayBlockingQueue<Socket>(1000);

	/**
	 * 启动整个服务器并响应服务，大概步骤都在这个方法里面
	 * 
	 * @throws Exception
	 */
	public void startUp() throws Exception {

		// 加载配置文件
		try {
			cache = MyCache.getInstance();
			router = Router.getInstance();
		} catch (SystemFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.getMessage();
		}

		// 线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
		ExecutorService e = Executors.newFixedThreadPool(3500);

		try (ServerSocket server = new ServerSocket(1234)) {
			// 开始接受请求
			while (true) {
				// 我发现之前没有写.close()的时候，就会出现NPE的情况
				// 所以对于浏览器来说，结束到底是收到响应之后还是关闭套接字之后呢？ 那个半关闭的问题？我还没有细细处理过关哪个流的问题
				// 而且try-resources还不能用在这里，自己思考一下跨线程的问题

				// 拿到一个socket之后线程池里的一群就会去抢,放到队列里，线程安全
				socketBuffer.add(server.accept());

				e.execute(() -> {
					try {
						serveIt(socketBuffer.remove());
						// 套接字将在发送了响应之后关闭，也就是myResponse方法里

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				});

			}
		}

		// 关闭方法解决的不是很好欸

	}

	/**
	 * 具体的某个线程的服务执行阶段：解析请求-->进行处理-->返回结果
	 * 
	 * @param server
	 * @throws Exception
	 */
	public void serveIt(Socket socket) throws Exception {

		System.out.println("目前的socket是：" + socket);

		// 处理请求
		long startTime = System.currentTimeMillis();

		MyRequest request = new MyRequest(socket);

		long endTime = System.currentTimeMillis();

		System.out.println(
				"服务器日志：" + Thread.currentThread().getName() + ":" + "响应的程序运行时间： " + (endTime - startTime) + "ms");

		// 我发现发请求的时候会发两次？！然后有一个叫做favicon.ico的东西？？

		ParsedResult result = UrlUtils.parseUri(request.getUri());

		// 去看看是不是静态文件
		if (result.isStatic()) {

			// 目前强制要求获取资源只能用GET方法！
			if (!RequestType.GET.equals(request.getRequestType())) {

				clientError(socket, request.getVersion(), Code.METHODNOTSUPPORT);

				return;
			}

			// 静态文件处理

			try {

				serverStatic(result.getParseUri(), socket, request.getVersion());
			} catch (Exception e) {
				clientError(socket, request.getVersion(), Code.INTERNALSERVERERROR);
			}

			return;

		}
		else{

			// 动态程序处理
			try {
				serverDynamic(request,result, socket);
			} catch (Exception e) {
				clientError(socket, request.getVersion(), Code.INTERNALSERVERERROR);
			}



			return;
		}

		

	}

	/**
	 * 返回错误提示
	 * 
	 * @param output
	 * @param version
	 * @param error
	 * @param code
	 * @throws IOException
	 */
	public void clientError(Socket socket, String version, String code) throws IOException {

		// 向服务器记录报错信息
		System.out.println("bad gate!");

		// 错误页面直接缓存在内存里面的！
		sendResponse(socket, version, "text/html", cache.getErrorCache(code), code);

	}

	public void sendResponse(Socket socket, String version, String fileType, byte[] fileContent, String code)
			throws IOException {

		long startTime = System.currentTimeMillis();

		MyResponse res = new MyResponse(version, code, fileType, fileContent);

		res.sendResponse(socket);

		long endTime = System.currentTimeMillis();

		System.out.println(
				"服务器日志：" + Thread.currentThread().getName() + ":" + "响应的程序运行时间： " + (endTime - startTime) + "ms");

	}

	/**
	 * 服务器响应静态文件
	 * 
	 * @param filename
	 * @param output
	 * @param version
	 * @throws Exception
	 */
	public void serverStatic(String filename, Socket socket, String version) throws Exception {

		System.out.println(filename);

		// 获取文件类型
		String fileType = UrlUtils.getFileType(filename);

		byte[] fileContent = cache.checkCache(filename);
		// 如果有就调用缓存的
		if (fileContent == null) {

			fileContent = FileUtils.fileToByte(filename, new IOException());
			// 如果上面有错就不会执行这里了
			cache.putIntoCache(filename, fileContent);

		}

		sendResponse(socket, version, fileType, fileContent, Code.OK);

	}

	public void serverDynamic(MyRequest request, ParsedResult result, Socket socket) throws IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, InstantiationException, ParamNameException {

		// 获取参数列表
		HashMap<String,String> inputParamMap = result.getParams();

		// 获取方法
		MyMethod myMethod = router.getMethod(result.getParseUri(), request.getRequestType());


		if (myMethod == null) {
			clientError(socket, request.getVersion(), Code.NOTFOUND);
			return;
		}

		// 获取方法本体
		Method method = myMethod.getMethod();

		// 现在能获取到注册了了的方法了,所以我们需要做的就是灌入参数，得到返回值
		
		// 检查参数----个数检查
		Integer paraCount = method.getParameterCount();
		
		if(inputParamMap==null)
		{
			if(paraCount!=0)
			{
				clientError(socket, request.getVersion(), Code.NOTFOUND);
				return;
			}
		}
		else
		{
			if(paraCount!=inputParamMap.size())
			{
				clientError(socket, request.getVersion(), Code.NOTFOUND);
				return;
			}
		}
		
		//准备：最终参数数组
		Object[] paramArray = new Object[paraCount];
		
		//参数录入位置校正
		HashMap<String,Class> standardParamMap = myMethod.getParaMap();
		
		Integer i = 0;
		
		for (Entry<String, Class> entry : standardParamMap.entrySet()) {
		
			Class type = entry.getValue();
			
			String paramResult = inputParamMap.get(entry.getKey());
			
			if(paramResult==null)
			{
				throw new ParamNameException();
			}
			
			
			//只提供几种常见的转型先？
			if(type==Integer.class)
			{
				paramArray[i++] = Integer.parseInt(paramResult);
			}
			else if(type==Double.class)
			{
				paramArray[i++] = Double.parseDouble(paramResult);
			}
			else if(type==Float.class)
			{
				paramArray[i++] = Float.parseFloat(paramResult);
			}
			else
			{
				paramArray[i++] = type.cast(paramResult);
			}
			
		}
		
		
		// 规定了必须是静态方法所以我这里第一个参数就是null了
		//这里转型会出问题
		Object ret = method.invoke(null,paramArray);
		
		System.out.println(ret);
		
		byte[] bytes = null;

		if (ret != null) {
			bytes  = String.valueOf(ret).getBytes();
		}

		sendResponse(socket, request.getVersion(), "text/html", bytes, Code.OK);

	}
}
