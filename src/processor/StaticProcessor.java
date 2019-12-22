package processor;

import java.net.Socket;

import bean.MyRequest;
import bean.ParsedResult;
import bean.ProcessedData;
import cache.CacheFacade;
import exceptions.BadRequestMethodException;
import exceptions.CannotFindException;
import message.Code;
import message.RequestType;
import utils.FileUtils;
import utils.UrlUtils;

public class StaticProcessor extends Processor {

	@Override
	protected ProcessedData prepareData(Socket socket, MyRequest request, ParsedResult parsedResult) throws CannotFindException, BadRequestMethodException {

		
		// 如果是静态，则只能是GET方法
		if (!RequestType.GET.equals(request.getRequestType())) {

			// 抛出错误：不支持的请求方法
			throw new BadRequestMethodException();

		}

		
		String filename = parsedResult.getParseUri();
		
		//关于这个缓存我感觉这种获得方式不太好
		CacheFacade cache = CacheFacade.getInstance();
		
		// out方法不一定会及时输出，err更方便debug，可以及时输出，常见场景：循环出错
		System.err.println(filename);

		// 获取文件类型
		String fileType = UrlUtils.getFileType(filename);

		// 尝试获取到缓存的内容
		byte[] fileContent = cache.checkCache(filename);

		// 如果缓存里没有就再去目录里解析文件
		if (fileContent == null) {

			// 去找
			fileContent = FileUtils.fileToByte(filename);

			// 解析成功后放入缓存
			cache.putIntoCache(filename, fileContent);

		}

		return new ProcessedData(fileType,fileContent,Code.OK);
	}
	
	
	
	

}
