package server;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import bean.HitRate;
import exceptions.SystemFileException;
import message.Code;
import message.Message;
import utils.FileUtils;

/**
 * 一个垃圾的缓存池设计
 * @author Lehr
 * @date 2019年12月10日
 */
public class MyCache {

	/**
	 * fileCacheMap的最大容量
	 */
	private static final Integer CACHE_SIZE = 15;

	/**
	 * 通过uri找到对应的缓存内容
	 */
	private Map<String, byte[]> fileCacheMap = new HashMap<>(CACHE_SIZE);

	/**
	 * Least Frequently Used算法需要的数据:uri和命中频率表
	 */
	private Map<String, HitRate> lfuMap = new HashMap<>();

	/**
	 * 通过你给的状态码找到对应的错误页面的uri
	 */
	private Map<String, String> errorCodeMap = new HashMap<>();

	/**
	 * 通过错误页面的uri来获取内容，放这里是为了不被lfu刷掉，感觉这样设计不太好
	 */
	private Map<String, byte[]> errorCacheMap = new HashMap<>();

	/**
	 * 单例对象
	 */
	private static MyCache cache;

	/**
	 * 构造的时候预先放入错误页面.....
	 * @throws SystemFileException
	 */
	private MyCache() throws SystemFileException  {
			
		errorCodeMap.put(Code.NOTFOUND, Message.ROOT_PATH+Message.SLASH+Code.NOTFOUND+Message.HTML_SUFFIX);
		errorCodeMap.put(Code.METHODNOTSUPPORT, Message.ROOT_PATH+Message.SLASH+Code.METHODNOTSUPPORT+Message.HTML_SUFFIX);
		errorCodeMap.put(Code.INTERNALSERVERERROR,Message.ROOT_PATH+Message.SLASH+Code.INTERNALSERVERERROR+Message.HTML_SUFFIX);

		loadSysFile(errorCodeMap.get(Code.NOTFOUND));
		loadSysFile(errorCodeMap.get(Code.METHODNOTSUPPORT));
		loadSysFile(errorCodeMap.get(Code.INTERNALSERVERERROR));

	}

	/**
	 * 加载系统文件
	 * @param filename
	 * @throws SystemFileException
	 */
	private void loadSysFile(String filename) throws SystemFileException {

		
		byte[] fileContent = null;
		
		try {			
			fileContent = FileUtils.fileToByte(filename);
		} catch (IOException e) {
			//将异常附带信息包裹出去
			SystemFileException se = new SystemFileException("Fail To Load System File!");
			se.initCause(e);
			throw se;
		}
		
		errorCacheMap.put(filename, fileContent);

	}

	/**
	 * 单例模式（管你懒汉饿汉安不安全）
	 * 
	 * @return
	 * @throws SystemFileException 
	 * @throws Exception
	 */
	public static MyCache getInstance() throws SystemFileException {
		if (cache == null) {
			cache =  new MyCache();
		}
		return cache;

	}

	/**
	 * 存入缓存，哈希线程不安全，所以我就先放着这样粗粒度的解决这个问题
	 * 以后心情好了再ReentrantLock
	 * 
	 * @param uri
	 * @param response
	 */
	public synchronized void putIntoCache(String uri, byte[] fileContent) {
		// 检查缓存替换
		cacheReplace();

		// 放入缓存
		fileCacheMap.put(uri, fileContent);
		// 记录信息
		lfuMap.put(uri, new HitRate(uri, 1, System.nanoTime()));
	}

	/**
	 * 缓存替换算法，目前写的很简陋
	 */
	public void cacheReplace() {
		if (fileCacheMap.size() > CACHE_SIZE) {

			// 把在一段时间内使用最少的替换了
			HitRate min = Collections.min(lfuMap.values());

			lfuMap.remove(min.getUri());
			fileCacheMap.remove(min.getUri());

		}
	}

	/**
	 * 通过uri获取缓存，没有的话就是null
	 * 
	 * @param uri
	 * @return
	 */
	public byte[] checkCache(String uri) {

		HitRate hr = lfuMap.get(uri);

		// 如果是之前命中过的就加一
		if (hr != null) {
			hr.setCount(hr.getCount() + 1);
			hr.setLastTime(System.nanoTime());
		}

		return fileCacheMap.get(uri);
	}

	public byte[] getErrorCache(String code) {

		return errorCacheMap.get(errorCodeMap.get(code));
	}

}
