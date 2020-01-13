package tiny.lehr.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import tiny.lehr.bean.HitRate;
import tiny.lehr.config.ConfigFacade;
import tiny.lehr.exceptions.SystemFileException;

/**
 * 一个垃圾的缓存池设计
 * 
 * @author Lehr
 * @date 2019年12月10日
 */
public class StaticCache {

	
	private ConfigFacade config;
	
	/**
	 * 通过uri找到对应的缓存内容
	 */
	private Map<String, byte[]> fileCacheMap;

	/**
	 * Least Frequently Used算法需要的数据:uri和命中频率表
	 */
	private Map<String, HitRate> lfuMap;

	protected StaticCache(){
		
		config = ConfigFacade.getInstance();
		
		fileCacheMap = new HashMap<>(config.getAttributeInteger("CACHE_SIZE"));
		
		lfuMap = new HashMap<>();
	}

	protected void putIntoCache(String uri, byte[] fileContent) {
		// 检查缓存替换
		cacheReplace();

		// 放入缓存
		fileCacheMap.put(uri, fileContent);
		// 记录信息
		lfuMap.put(uri, new HitRate(uri, 1, System.nanoTime()));
	}


	protected void cacheReplace() {
		if (fileCacheMap.size() > config.getAttributeInteger("CACHE_SIZE")) {

			// 把在一段时间内使用最少的替换了
			HitRate min = Collections.min(lfuMap.values());

			lfuMap.remove(min.getUri());
			
			fileCacheMap.remove(min.getUri());

		}
	}


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
