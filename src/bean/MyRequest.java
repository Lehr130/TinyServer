package bean;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import message.RequestType;

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

	public MyRequest(Socket socket) throws IOException {

		BufferedInputStream input = new BufferedInputStream(socket.getInputStream());

		// Integer len = input.available();

		byte[] buffer = new byte[100];

		long startTime = System.currentTimeMillis();

		// read()方法当读取完数据之后就开始阻塞，等待返回-1结束，这个等待的过程占到了总时间的99%
		input.read(buffer);

		long endTime = System.currentTimeMillis();

		System.out.println("解析请求的程序运行时间： " + (endTime - startTime) + "ms");

		// https://www.jianshu.com/p/e52db372d986
		String re = new String(buffer);

		packUp(re);

	}

	public void packUp(String re) {
		String[] body = re.split("\r\n");

		String[] reh = body[0].split(" ");
		
		if("GET".equals(reh[0]))
		{
			this.requestType = RequestType.GET;
		}
		if("POST".equals(reh[0]))
		{
			this.requestType = RequestType.POST;
		}
		if("PUT".equals(reh[0]))
		{
			this.requestType = RequestType.PUT;
		}
		if("DELETE".equals(reh[0]))
		{
			this.requestType = RequestType.DELETE;
		}
		
		this.uri = reh[1];
		this.version = reh[2];

//		for(String s : body)
//		{
//			System.out.println("It's : "+s +"  |||");
//		}
//		
	}

}
