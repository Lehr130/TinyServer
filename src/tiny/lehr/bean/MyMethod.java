package tiny.lehr.bean;

import java.lang.reflect.Method;
import java.util.HashMap;

import tiny.lehr.enums.RequestType;

/**
 * 
 * 每个方法注册将会被包装成这样一个对象
 * 
 * @author Lehr
 * @date 2019年12月16日
 *
 */
@SuppressWarnings("rawtypes")
public class MyMethod {

	private RequestType requestType;
	private Method method;
	private Class fromClazz;
	private HashMap<String, Class> paraMap;

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

	public HashMap<String, Class> getParaMap() {
		return paraMap;
	}

	public void setParaMap(HashMap<String, Class> paraMap) {
		this.paraMap = paraMap;
	}

	public MyMethod(RequestType requestType, Method method, Class fromClazz, HashMap<String, Class> paraMap) {
		super();
		this.requestType = requestType;
		this.method = method;
		this.fromClazz = fromClazz;
		this.paraMap = paraMap;
	}

}
