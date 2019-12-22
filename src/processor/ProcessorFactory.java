package processor;

import message.ServerType;

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

		return null;

	}
}
