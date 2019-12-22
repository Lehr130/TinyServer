package cache;

import exceptions.SystemFileException;

/**
 * 
 * @author Lehr
 * @date 2019年12月22日
 * 
 */
public class CacheFacade {

	/**
	 * 使用饿汉，是各种你缓存模式的门面模式入口
	 */
	private static CacheFacade instance = new CacheFacade(); 
	
	private ErrorCache errorCache;
	
	private StaticCache staticCache;
	
	private CacheFacade()
	{
		try {
			errorCache = new ErrorCache();
			staticCache = new StaticCache();
		} catch (SystemFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static CacheFacade getInstance()
	{
		return instance;
	}
	
	
	public byte[] getErrorPage(String code)
	{
		return errorCache.getErrorPage(code);
	}
	
	
	public byte[] checkCache(String uri)
	{
		return staticCache.checkCache(uri);
	}
	
	public void putIntoCache(String uri, byte[] fileContent)
	{
		staticCache.putIntoCache(uri, fileContent);
	}
}
