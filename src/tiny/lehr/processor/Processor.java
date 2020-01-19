package tiny.lehr.processor;

import tiny.lehr.bean.MyRequest;
import tiny.lehr.bean.MyResponse;
import tiny.lehr.bean.ParsedResult;
import tiny.lehr.bean.ProcessedData;
import tiny.lehr.cache.CacheFacade;
import tiny.lehr.enums.Code;
import tiny.lehr.enums.Message;

import java.net.Socket;

/**
 * 这是处理模式的抽象类，用于使用模板方法这个设计模式，后期可以加上工厂模式
 * 
 * @author Lehr
 * @date 2019年12月22日
 * 
 */
public abstract class Processor {

	/**
	 * 统一处理数据，并在自己内部处理异常，获得文件类型和错误码
	 * 
	 * @param socket
	 * @param request
	 * @param parsedResult
	 * @return
	 */
	protected abstract ProcessedData prepareData(Socket socket, MyRequest request, ParsedResult parsedResult);

	public final void processRequest(Socket socket, MyRequest request, ParsedResult parsedResult) {

		// 先处理数据
		ProcessedData data = prepareData(socket, request, parsedResult);

		// 把错误处理放在中间
		if (Code.OK != data.getCode()) {
			processError(socket, data.getCode());
			return;
		}

		// 然后再发送请求
		sendResponse(socket, request.getVersion(), data.getFileType(), data.getFileContent(), data.getCode());
	}

	/**
	 * 一个通用的错误处理方法
	 * @param socket
	 * @param code
	 */
	public static void processError(Socket socket, Code code) {
		//这里用默认的是不是不太好，但也是为了外层处理request的时候报错处理更方便了

		// 向服务器记录报错信息
		System.err.println("fuck that!");

		// 这里的话怎么调用CacheFacade还需要再设计一下

		// 获取报错页面并去响应
		sendResponse(socket, Message.DEFAULT_HTTP_VERSION, "text/html", CacheFacade.getInstance().getErrorPage(code.getCode()), code);

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
