package bean;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

import exceptions.EmptyRequestException;

/**
 * @author Lehr
 * @date 2019年12月11日
 */
public class MyRequest {

	private String method;
	private String uri;
	private String version;

	private HashMap<String, String> heads;

	private HashMap<String, String> bodys;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
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

	
	public MyRequest(Socket socket) throws IOException{

		long startTime = System.currentTimeMillis();  
		
		// 这里千万不能关闭  我也不知道为什么，以后去研究下
//		Scanner input = new Scanner(socket.getInputStream(), "UTF-8");
//		String[] hs = input.nextLine().split(" ");
//		this.method = hs[0];
//		this.uri = hs[1];
//		this.version = hs[2];
//		

		
		InputStream input = socket.getInputStream();
//		Integer readed = input.available();  并不是说chrome的问题，貌似对有些情况发送的报文（比如Jmeter发的）就完全接受不了，这个变为0了但实际是有报文的
		byte[] buffer = new byte[100];   //好像再大点也对性能没有太大影响
		
		input.read(buffer);
		
		String re = new String(buffer);
		
		
		packUp(re);
		
		long endTime= System.currentTimeMillis();  
		
		System.out.println("解析请求的程序运行时间： "+(endTime-startTime)+"ms");  
		
		
		
		//开始封装请求
		

	}
	
	
	public void packUp(String re)
	{
		String[] body = re.split("\r\n");
		
		String[] reh = body[0].split(" ");
		this.method = reh[0];
		this.uri = reh[1];
		this.version = reh[2];
		
//		for(String s : body)
//		{
//			System.out.println("It's : "+s +"  |||");
//		}
//		
	}

}
