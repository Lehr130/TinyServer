package tiny.lehr.bean;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Lehr
 * @create 2020-01-14
 * 响应封装类，自带封装和发送方法
 */
public class MyResponse {

	/**
	 * HTTP版本号，例如"HTTP/1.1"
	 */
	private String version;

	/**
	 * 状态码
	 */
	private String code;

	/**
	 * 内容数据，但是目前好像没用
	 */
	private String data;

	/**
	 * 文件类型
	 */
	private String fileType;

	/**
	 * 响应体
	 */
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

	public MyResponse(String version, String code, String fileType, byte[] resBody) {
		super();
		this.version = version;
		this.code = code;
		this.fileType = fileType;
		this.resBody = resBody;
	}

	/**
	 * 发送请求并关闭socket
	 * 
	 * @param socket
	 */
	public void sendResponse(Socket socket) {
		// 记录时间
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");
		Date date = new Date();

		// 获取输出流 然后执行完后自动关闭socket（高的关了低的自动关）
		try (PrintStream output = new PrintStream(socket.getOutputStream(), true)) {

			// 响应头
			output.print(version + " " + code + " \r\n");
			output.print("Date:" + sdf.format(date) + "\r\n");
			output.print("Server:Lehr's Tiny Server \r\n");

			if (resBody == null) {
				output.print("Content-length: 0\r\n\r\n");
			} else {
				// 所以这个最后浏览器渲染的结果还和这几个响应头的顺序有关？！？！
				output.print("Content-length: " + resBody.length + "\r\n");
				output.print("Content-type: " + fileType + ";charset=utf-8" + "\r\n\r\n");
				// 响应体
				output.write(resBody);
				output.print("\r\n");
			}

		} catch (IOException e) {
			// 这个还是没能处理
			e.printStackTrace();
		}

		System.out.println(socket.isClosed() + "   " + socket);

		/*
		 * 而且我还是很好奇这里println自动刷出的问题，如果已经发送了一部分报文的话，那么那边会不会是接收到的是不完整的报文？主要是这里恰巧就...
		 * 欸我也不知道这个了 根据实验得到的结果是：socket关闭了之后才会发送回去 不对，好像取决于是不是一个完整的http请求？》？？
		 * 关于关闭资源的问题？？？半关闭？？？？？ http长连接是什么情况下在用一个socket发多次请求？
		 */

	}

}
