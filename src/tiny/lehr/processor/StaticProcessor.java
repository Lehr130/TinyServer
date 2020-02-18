package tiny.lehr.processor;

import tiny.lehr.bean.MyRequest;
import tiny.lehr.bean.MyResponse;
import tiny.lehr.cache.CacheFacade;
import tiny.lehr.enums.Code;
import tiny.lehr.enums.Message;
import tiny.lehr.exceptions.BadRequestMethodException;
import tiny.lehr.exceptions.CannotFindException;
import tiny.lehr.utils.FileUtils;
import tiny.lehr.utils.UrlUtils;

public class StaticProcessor extends Processor {

	@Override
	protected void prepareData(MyRequest req, MyResponse res) throws Exception{

		// 如果是静态，则只能是GET方法
		if (!"GET".equals(req.getMethod())) {

			// 抛出错误：不支持的请求方法
			//这个在nginx里对应的是405 Not Allowed的情况
			throw new BadRequestMethodException();

		}

		//首先获取文件的路径
		String filename = Message.ROOT_PATH + req.getRequestURI();

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
			try {
				fileContent = FileUtils.fileToByte(filename);
			} catch (CannotFindException e) {
				throw new CannotFindException("找不到！");
			}

			// 解析成功后放入缓存
			cache.putIntoCache(filename, fileContent);

		}

		res.setFileType(fileType);

		res.setResBody(fileContent);

		res.setCode(Code.OK.getCode());

	}

	
	
	

}
