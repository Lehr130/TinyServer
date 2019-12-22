package cache;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import exceptions.CannotFindException;
import exceptions.SystemFileException;
import message.Code;
import message.Message;
import utils.FileUtils;

/**
 * 一个垃圾的缓存池设计
 * 
 * @author Lehr
 * @date 2019年12月10日
 */
public class ErrorCache {

	/**
	 * 通过你给的状态码找到对应的错误页面的uri
	 */
	private Map<String, String> errorCodeMap = new HashMap<>();

	/**
	 * 通过错误页面的uri来获取内容
	 */
	private Map<String, byte[]> errorCacheMap = new HashMap<>();

	/**
	 * 构造的时候预先放入错误页面.....
	 * 
	 * @throws SystemFileException
	 */
	protected ErrorCache() throws SystemFileException {

		for (Code c : Code.values()) {
			if (!c.getCode().equals("200")) {
				errorCodeMap.put(c.getCode(), Message.ROOT_PATH + File.separator + "systemFile" + File.separator
						+ c.getCode() + Message.HTML_SUFFIX);
				loadSysFile(errorCodeMap.get(c.getCode()));
			}
		}

	}

	/**
	 * 加载系统文件
	 * 
	 * @param filename
	 * @throws SystemFileException
	 */
	private void loadSysFile(String filename) throws SystemFileException {

		byte[] fileContent = null;

		try {
			fileContent = FileUtils.fileToByte(filename);
		} catch (CannotFindException e) {
			// 将异常附带信息包裹出去
			SystemFileException se = new SystemFileException("Fail To Load System File!");
			se.initCause(e);
			throw se;
		}

		errorCacheMap.put(filename, fileContent);

	}

	protected byte[] getErrorPage(String code) {

		return errorCacheMap.get(errorCodeMap.get(code));
	}
	
	
	

}
