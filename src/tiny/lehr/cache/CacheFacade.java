package tiny.lehr.cache;

import tiny.lehr.exceptions.SystemFileException;

/**
 * @author Lehr
 * @create 2020-01-14
 * Cache的门面对象，用来对外提供统一的调用接口
 */
public class CacheFacade {

    /**
     * 使用饿汉，是各种缓存模式的门面模式入口
     */
    private static CacheFacade instance = new CacheFacade();

    /**
     * 内置错误页面缓存
     */
    private ErrorCache errorCache;

    /**
     * 内置运行后的静态页面缓存
     */
    private StaticCache staticCache;

    private CacheFacade() {
        try {
            errorCache = new ErrorCache();
            staticCache = new StaticCache();
        } catch (SystemFileException e) {
            e.printStackTrace();
        }
    }

    public static CacheFacade getInstance() {
        return instance;
    }

    /**
     * 通过状态码，从错误缓存中获取错误页面
     * @param code
     * @return
     */
    public byte[] getErrorPage(String code) {
        return errorCache.getErrorPage(code);
    }


    /**
     * 传入uri，检测静态页面缓存是否存在
     * @param uri
     * @return
     */
    public byte[] checkCache(String uri) {
        return staticCache.checkCache(uri);
    }

    /**
     * 传入内容，加入到静态缓存，底层有具体的算法处理
     * @param uri
     * @param fileContent
     */
    public void putIntoCache(String uri, byte[] fileContent) {
        staticCache.putIntoCache(uri, fileContent);
    }
}
