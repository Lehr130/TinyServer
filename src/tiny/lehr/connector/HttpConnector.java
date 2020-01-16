package tiny.lehr.connector;

import tiny.lehr.bean.MyRequest;
import tiny.lehr.bean.ParsedResult;
import tiny.lehr.config.ConfigFacade;
import tiny.lehr.enums.Code;
import tiny.lehr.exceptions.BadRequestMethodException;
import tiny.lehr.exceptions.IllegalParamInputException;
import tiny.lehr.processor.Processor;
import tiny.lehr.processor.ProcessorFactory;
import tiny.lehr.utils.UrlUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Lehr
 * @date 2019年12月10日
 */
public class HttpConnector {

	private Queue<Socket> socketQueue;

	private ExecutorService pool;

	private ServerSocket server;

	private Boolean SHUTDOWN;

	/**
	 * 启动前的准备
	 * 
	 * @throws IOException
	 */
	protected HttpConnector() throws IOException {

		// 获取配置文件
		ConfigFacade config = ConfigFacade.getInstance();
		// 建立socket缓冲队列 采用阻塞队列，线程安全，及时把请求从accept队列里拿上来，底层accpet队列的长度可由backlog参数修改
		socketQueue = new ArrayBlockingQueue<>(config.getAttributeInteger("SOCKET_BUFFER_LEN"));
		// 创建线程池
		pool = Executors.newFixedThreadPool(config.getAttributeInteger("THREAD_POOL_SIZE"));
		// 建立监听
		server = new ServerSocket(config.getAttributeInteger("LISTEN_PORT"));
		// 模仿人家Tomcat设计一个SHUTDOWN
		SHUTDOWN = false;

	}

	/**
	 * 启动这个Connector，对整个端口进行监听服务 <br>
	 * 大概步骤： 1.加载配置文件 <br>
	 * 2.主线程接收请求并放入队列 <br>
	 * 3.工作线程开始从请求队列里取出请求并开始响应 <br>
	 * 
	 * @throws IOException
	 */
	protected void startUp() throws IOException {

		/*
		 *  怎么设置socket等待timeout?
		 *  具体方法待定
		 *  
		 */

		while (!SHUTDOWN) {
			// 请求放入队列里
			socketQueue.add(server.accept());
			// 工作线程取出请求并工作
			pool.execute(() -> doIt(socketQueue.remove()));
		}
	}


	protected void shutDown()
	{
		SHUTDOWN = true;
	}
	
	private void doIt(Socket socket)
	{
		Processor processor;
		
		try {

			// 接收请求
			MyRequest request = new MyRequest(socket);

			// 分析请求类型：静态还是动态请求
			ParsedResult result = UrlUtils.parseUri(request.getUri());

			// 获取请求类型对应的处理器，但我不知道这样每次new一个出来好不好
			processor = ProcessorFactory.createProcessor(result.getType());

			processor.processRequest(socket, request, result);

		} catch (IllegalParamInputException e) {
			Processor.processError(socket,Code.PARAMILLEGAL);
		} catch (BadRequestMethodException e) {
			Processor.processError(socket,Code.BADREQUEST);
		} catch (IOException e) {
			Processor.processError(socket,Code.INTERNALSERVERERROR);
		} catch (NullPointerException e)
		{
			Processor.processError(socket,Code.INTERNALSERVERERROR);
		}

	}
	
}
