package tiny.lehr.bean;

import tiny.lehr.enums.RequestType;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author Lehr
 * @create 2020-01-14
 * 每个我自定义的DynamicJava方法会被以这样的形式放入内存中
 */
@SuppressWarnings("rawtypes")
public class MyMethod {

	/**
	 * 这个方法被请求所需要的请求类型
	 */
	private RequestType requestType;
	/**
	 * 方法元数据
	 */
	private Method method;
	/**
	 * 来自哪个类（暂时没用，想着做扩展用）
	 */
	private Class fromClazz;
	/**
	 * 方法的参数表，分别是名字和参数类型
	 */
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
