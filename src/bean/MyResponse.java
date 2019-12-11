package bean;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Lehr
 * @date 2019年12月11日
 */
public class MyResponse {

	private String version;
	private String code;
	private String data;
	private String fileType;
	private byte[] resBody;
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public byte[] getResBody() {
		return resBody;
	}
	public void setResBody(byte[] resBody) {
		this.resBody = resBody;
	}
	
	public MyResponse(String version, String code, String fileType,byte[] resBody) {
		super();
		this.version = version;
		this.code = code;
		this.fileType = fileType;
		this.resBody = resBody;
	}
	
	public void sendResponse(Socket socket) throws IOException
	{
		
		//记录时间
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");
		Date date = new Date();

		
		//获取输出流
		PrintStream output = new PrintStream(socket.getOutputStream(), true);
		
		//响应头
		output.print(version + " "+ code +" \r\n");
		output.print("Date:" + sdf.format(date) + "\r\n");
		output.print("Server:Lehr's Tiny Server \r\n");
		output.print("Content-length: " + resBody.length + "\r\n");
		output.print("Content-type: " + fileType + ";charset=utf-8" + "\r\n\r\n");

		//响应体
		output.write(resBody);
		output.print("\r\n");
		
		System.out.println("响应成功！");
		
		
		//关于关闭资源的问题？？？
		//半关闭？？？？？

	}
	

	

	
}
