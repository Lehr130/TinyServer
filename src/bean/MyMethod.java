package bean;

import java.lang.reflect.Method;

import message.RequestType;

/**
 * 
 * 每个方法注册将会被包装成这样一个对象
 * @author Lehr
 * @date 2019年12月16日
 *
 */
public class MyMethod {

	private RequestType requestType;
	private Method method;
	private Class fromClazz;
	
	
	public Class getFromClazz() {
		return fromClazz;
	}

	public void setFromClazz(Class fromClazz) {
		this.fromClazz = fromClazz;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public <T> MyMethod(RequestType requestType, Method method, Class<T> fromClazz) {
		super();
		this.requestType = requestType;
		this.method = method;
		this.fromClazz = fromClazz;
	}

	
}
