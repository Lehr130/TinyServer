package processor;

import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

import bean.MyRequest;
import bean.ParsedResult;
import bean.ProcessedData;
import message.Code;

public class ProxyProcessor extends Processor{

	@Override
	protected ProcessedData prepareData(Socket socket, MyRequest request, ParsedResult parsedResult) throws Exception {

		//目前不支持https
		URLConnection urlConnection = new URL(parsedResult.getParseUri()).openConnection();
		try (InputStream is = urlConnection.getInputStream()) {
			// 访问获取连接
			urlConnection.connect();
			// 获得一个输入流
			byte[] buffer = new byte[is.available()];
			is.read(buffer);

			//这里的错误码，fileType可以变得更灵活
			return new ProcessedData("text/html", buffer, Code.OK);
		}

	}

	
	
	
}
