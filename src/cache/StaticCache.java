package cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import bean.HitRate;
import config.ConfigFacade;
import exceptions.SystemFileException;

/**
 * 一个垃圾的缓存池设计
 * 
 * @author Lehr
 * @date 2019年12月10日
 */
public class StaticCache {

	private ConfigFacade config = ConfigFacade.getInstance();
	/**
	 * 通过uri找到对应的缓存内容
	 */
	private Map<String, byte[]> fileCacheMap = new HashMap<>(config.getAttributeInteger("CACHE_SIZE"));

	/**
	 * Least Frequently Used算法需要的数据:uri和命中频率表
	 */
	private Map<String, HitRate> lfuMap = new HashMap<>();

	protected StaticCache() throws SystemFileException {		
		
	}

	/**
	 * 存入缓存，哈希线程不安全，所以我就先放着这样粗粒度的解决这个问题 以后心情好了再ReentrantLock
	 * 
	 * @param uri
	 * @param response
	 */
	protected void putIntoCache(String uri, byte[] fileContent) {
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
	protected void cacheReplace() {
		if (fileCacheMap.size() > config.getAttributeInteger("CACHE_SIZE")) {

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
	protected byte[] checkCache(String uri) {

		HitRate hr = lfuMap.get(uri);

		// 如果是之前命中过的就加一
		if (hr != null) {
			hr.setCount(hr.getCount() + 1);
			hr.setLastTime(System.nanoTime());
		}

		return fileCacheMap.get(uri);
	}


}
