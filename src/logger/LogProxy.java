package logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class LogProxy implements InvocationHandler {

	private Object src;

	private MyLogger myLogger = MyLogger.getInstance();
	
	// 在私有的构造中给成员设置值
	private LogProxy(Object src) {
		this.src = src;
	}

	public static Object factory(Object src) {
		Object proxyedObj = Proxy.newProxyInstance(LogProxy.class.getClassLoader(), src.getClass().getInterfaces(),
				new LogProxy(src));
		return proxyedObj;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Long startTime = System.currentTimeMillis();
		Object invoke = method.invoke(src, args);
		Long endTime = System.currentTimeMillis();
		Long finalTime = endTime-startTime;
		myLogger.info(method.getName()+"执行完成，用时："+finalTime+"ms");		
		return invoke;

	}

}
