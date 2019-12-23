package processor;

import java.net.Socket;

import bean.MyRequest;
import bean.MyResponse;
import bean.ParsedResult;
import bean.ProcessedData;
import cache.CacheFacade;
import message.Code;

/**
 * 这是处理模式的抽象类，用于使用模板方法这个设计模式，后期可以加上工厂模式
 * @author Lehr
 * @date 2019年12月22日
 * 
 */
public abstract class Processor {

	
	protected abstract ProcessedData prepareData(Socket socket, MyRequest request, ParsedResult parsedResult) throws Exception;
	
	public final void processRequest(Socket socket, MyRequest request, ParsedResult parsedResult) throws Exception
	{
		//先处理数据
		ProcessedData data = prepareData(socket,request,parsedResult);
		
		//把错误处理放在中间
		if(Code.OK!=data.getCode())
		{
			clientError(socket,request.getVersion(),data.getCode());
		}
		
		//然后再发送请求		//这个接口可以再该改小一点
		sendResponse(socket, request.getVersion(), data.getFileType(), data.getFileContent(), data.getCode());
	}
	
	/**
	 * 当发送异常的时候返回错误提示页面（以后可以扩展这里）
	 * 
	 * @param socket
	 * @param version
	 * @param code
	 * @param cache
	 */
	private static void clientError(Socket socket, String version, Code code) {

		// 向服务器记录报错信息
		System.err.println("fuck that!");

		//这里的话怎么调用CacheFacade还需要再设计一下
		
		// 获取报错页面并去响应
		sendResponse(socket, version, "text/html", CacheFacade.getInstance().getErrorPage(code.getCode()), code);

	}
	
	

	/**
	 * 发送响应
	 * 
	 * @param socket
	 * @param version
	 * @param fileType
	 * @param fileContent
	 * @param code
	 */
	private static void sendResponse(Socket socket, String version, String fileType, byte[] fileContent, Code code) {

		// 把所有的数据聚合成一个响应
		MyResponse res = new MyResponse(version, code.getCode(), fileType, fileContent);

		// 并发送响应
		res.sendResponse(socket);

	}


	
	
}
