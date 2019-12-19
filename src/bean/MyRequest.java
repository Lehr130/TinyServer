package bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

import exceptions.BadRequestMethodException;
import exceptions.IllegalParamInputException;
import message.RequestType;
import utils.UrlUtils;

/**
 * @author Lehr
 * @date 2019年12月11日
 */
public class MyRequest {

	private RequestType requestType;
	private String uri;
	private String version;

	private HashMap<String, String> heads;

	private HashMap<String, String> bodys;

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public HashMap<String, String> getHeadsList() {
		return heads;
	}

	public void setHeadsList(HashMap<String, String> headsList) {
		this.heads = headsList;
	}

	public HashMap<String, String> getBodyList() {
		return bodys;
	}

	public void setBodyList(HashMap<String, String> bodyList) {
		this.bodys = bodyList;
	}

	public MyRequest(Socket socket) throws IOException, IllegalParamInputException, BadRequestMethodException {

		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		Integer bufferSize = 1024;

		// 这里其实应该循环读入
		char[] buffer = new char[bufferSize];

		StringBuilder sb = new StringBuilder();

		// read()方法当读取完数据之后就开始阻塞，等待返回-1结束，这个等待的过程占到了总时间的99%

		Integer count = bufferSize;
		while (count.equals(bufferSize)) {
			count = br.read(buffer);
			sb.append(buffer);
		}

		String request = new String(sb);

		packUp(request);

		
		System.out.println(request);
		
		// 收完报文后半关闭
		socket.shutdownInput();

		/*
		 * 下面是一种出错的情况，似乎你一次读过头了之后对于socket就阻塞不返回-1你就死了 read(buffer)到截至时的返回值好像不太对劲？？？ int
		 * count = br.read(buffer); while(count>0) { sb.append(buffer); count =
		 * br.read(buffer); }
		 */

	}

	public void packUp(String request) throws IllegalParamInputException, BadRequestMethodException {

		String[] requestLine = request.split("\r\n");

		// 处理请求行 "POST /path/uri HTTP/1.1"
		packLine(requestLine[0]);
		
		Integer count;

		this.heads = new HashMap<>(16);

		// 处理请求头 "Connection: keep-alive" 多行
		for (count = 1; count < requestLine.length && requestLine[count].length() > 0; count++) {
			String[] repart = requestLine[count].split(": ");
			heads.put(repart[0], repart[1]);
		}

		// 跳过空行， 如果有请求体的话
		if (requestLine[++count].trim().length() > 0) {
			// 处理请求体 "adsf=dsaf&adsf=dsaf"
			bodys = UrlUtils.getParamMap(requestLine[count]);
		}

	}

	/**
	 * 处理请求行
	 * 
	 * @param line
	 * @throws BadRequestMethodException
	 */
	public void packLine(String line) throws BadRequestMethodException {
		// 切片
		String[] reh = line.split(" ");

		// 获取uri
		this.uri = reh[1];

		// 获取HTTP协议版本
		this.version = reh[2];

		for (RequestType rt : RequestType.values()) {
			// 忽略大小写进行比较
			if (rt.toString().equalsIgnoreCase(reh[0])) {
				this.requestType = rt;
				return;
			}

		}

		// 如果完了都还没找到对应的方法就报错
		throw new BadRequestMethodException();

	}

}
