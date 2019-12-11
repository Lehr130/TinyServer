package server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import bean.HitRate;
import exceptions.SystemFileException;
import message.Code;
import message.Message;
import utils.FileUtils;

/**
 * @author Lehr
 * @date 2019年12月10日
 */
public class MyCache {

	/**
	 * 缓存本体
	 */
	private Map<String, byte[]> map = new HashMap<String, byte[]>();

	
	private static final Integer CACHE_SIZE = 15;
	
	/**
	 * Least Frequently Used算法需要的数据
	 */
	private Map<String, HitRate> lfuMap = new HashMap<String, HitRate>();

	/**
	 * 错误设计缓存：垃圾设计
	 */
	private Map<String, String> eMap = new HashMap<String, String>();

	/**
	 * 错误设计缓存：内容栏
	 */
	private Map<String, byte[]> epMap = new HashMap<String, byte[]>();

	/**
	 * 单例对象
	 */
	private static MyCache cache;

	private MyCache() throws Exception {
		// 预先放入错误页面

		
		eMap.put(Code.NOTFOUND, Message.ROOT_PATH+"/"+Code.NOTFOUND+".html");
		eMap.put(Code.METHODNOTSUPPORT, Message.ROOT_PATH+"/"+Code.METHODNOTSUPPORT+".html");
		eMap.put(Code.INTERNALSERVERERROR,Message.ROOT_PATH+"/"+Code.INTERNALSERVERERROR+".html");
		
		loadSysFile(eMap.get(Code.NOTFOUND));
		loadSysFile(eMap.get(Code.METHODNOTSUPPORT));
		loadSysFile(eMap.get(Code.INTERNALSERVERERROR));

	}

	private void loadSysFile(String filename) throws Exception {

		byte[] fileContent = FileUtils.fileToByte(filename, new SystemFileException());

		epMap.put(filename, fileContent);
	}

	/**
	 * 单例模式（管你懒汉饿汉安不安全）
	 * 
	 * @return
	 * @throws Exception
	 */
	public static MyCache getInstance() throws Exception {
		if (cache == null) {
			return new MyCache();
		}
		return cache;

	}

	/**
	 * 存入缓存，哈希线程不安全，所以我就先放着这样粗粒度的解决这个问题
	 * 
	 * @param uri
	 * @param response
	 */
	public synchronized void putIntoCache(String uri, byte[] fileContent) {
		// 检查缓存替换
		cacheReplace();

		// 放入缓存
		map.put(uri, fileContent);
		// 记录信息
		lfuMap.put(uri, new HitRate(uri, 1, System.nanoTime()));
	}

	/**
	 * 缓存替换算法，目前写的很简陋
	 */
	public void cacheReplace() {
		if (map.size() > CACHE_SIZE) {

			// 把在一段时间内使用最少的替换了
			HitRate min = Collections.min(lfuMap.values());

			lfuMap.remove(min.getUri());
			map.remove(min.getUri());

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

		return map.get(uri);
	}

	public byte[] getErrorCache(String code) {

		String filename = eMap.get(code);
		return epMap.get(filename);
	}

}
