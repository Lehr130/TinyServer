package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bean.MyRequest;
import bean.MyResponse;
import exceptions.EmptyRequestException;
import exceptions.SystemFileException;
import message.Code;
import message.Message;
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
	 * 启动整个服务器并响应服务，大概步骤都在这个方法里面
	 * 
	 * @throws Exception
	 */
	public void startUp() throws Exception {

		// 加载配置文件
		try {
			cache = MyCache.getInstance();
		} catch (SystemFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.getMessage();
		}

		//线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
		
		ExecutorService e = Executors.newFixedThreadPool(500);
		
		
		try(ServerSocket server = new ServerSocket(1234))
		{			
			//开始接受请求
			while (true) {
				//我发现之前没有写.close()的时候，就会出现NPE的情况
				//所以对于浏览器来说，结束到底是收到响应之后还是关闭套接字之后呢？  那个半关闭的问题？我还没有细细处理过关哪个流的问题
				//而且try-resources还不能用在这里，自己思考一下跨线程的问题
				
				
				//拿到一个socket之后线程池里的一群就会去抢
				Socket socket = server.accept();
				
				
				
					e.execute(() -> {
						try {	
							
							serveIt(socket);
							socket.close();
					
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
		
		// 处理请求
		MyRequest request = new MyRequest(socket);			
		
		


		// 我发现发请求的时候会发两次？！然后有一个叫做favicon.ico的东西？？

		
		// 去看看是不是静态文件
		String parseUri = UrlUtils.parseUri(request.getUri());
		
		// 目前强制要求获取资源只能用GET方法！
		if (!Message.DYNAMIC.equals(parseUri)) {
			
			if (!Message.GET.equals(request.getMethod())) {

				clientError(socket, request.getVersion(), Code.METHODNOTSUPPORT);
				
				return ;
			}

			// 静态文件处理
			
			try
			{
				
				serverStatic(parseUri, socket, request.getVersion());
			}
			catch(Exception e)
			{
				clientError(socket, request.getVersion(), Code.INTERNALSERVERERROR);
			}

			return ;
			
		}
		if(Message.DYNAMIC.equals(parseUri)){
			
			// 动态程序处理
			serverDynamic(request.getUri(), socket, request.getVersion());
			
			return ;
		}
		
		return ;

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

		MyResponse res = new MyResponse(version, code, fileType, fileContent);

		res.sendResponse(socket);

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
			//如果上面有错就不会执行这里了
			cache.putIntoCache(filename, fileContent);
			
		}

		sendResponse(socket, version, fileType, fileContent, Code.OK);

	}

	
	public void serverDynamic(String uri, Socket socket, String version) throws IOException {
		
		clientError(socket, version, Code.NOTFOUND);
	}
}
