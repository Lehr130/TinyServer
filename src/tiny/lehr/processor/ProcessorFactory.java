package tiny.lehr.processor;

import tiny.lehr.enums.ServerType;

/**
 * 一个简单工厂类
 * 
 * @author Lehr
 * @date 2019年12月22日
 * 
 */
public class ProcessorFactory {

	private ProcessorFactory() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * 根据请求类型来返回对应的处理器
	 * @param type
	 * @return
	 */
	public static Processor createProcessor(ServerType type) {

		if (ServerType.STATIC_RESOURCES.equals(type)) {
			return new StaticProcessor();
		}
		if (ServerType.DYNAMIC_JAVA.equals(type)) {
			return new DynamicProcessor();
		}
		if (ServerType.PROXY.equals(type)) {
			return new ProxyProcessor();
		}
		if(ServerType.SERVLET.equals(type))
		{
			return new ServletProcessor();
		}

		return null;

	}
}
