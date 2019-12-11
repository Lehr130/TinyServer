package bean;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

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

	@Override
	public String toString() {
		return "MyRequest:\n" + this.method + " " + this.uri + " ";

	}

	public MyRequest(Socket socket) throws IOException {

		// 这里千万不能关闭  我也不知道为什么，以后去研究下
		Scanner input = new Scanner(socket.getInputStream(), "UTF-8");

		String[] requestLine = input.nextLine().split(" ");
		this.method = requestLine[0];
		this.uri = requestLine[1];
		this.version = requestLine[2];

		
//			String heas = input.readLine();
//			
//			while(!"\r\n".equals(heas))
//			{
//				String[] hea = heas.split(":");
//				heads.put(hea[0], hea[1]);
//				heas = input.readLine();
//			}
//
//			
//			String bos = input.readLine();
//			
//			while(bos!=null)
//			{
//				String[] bo = bos.split(":");
//				heads.put(bo[0], bo[1]);
//				bos = input.readLine();
//			}
//
//			

		System.out.println(this);

	}

}
