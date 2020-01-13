package tiny.lehr.container;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;

import tiny.lehr.annotation.LehrsMethod;
import tiny.lehr.annotation.ParamName;
import tiny.lehr.annotation.ThisGonnaBeADynamicMethod;
import tiny.lehr.bean.MyMethod;
import tiny.lehr.enums.RequestType;
import tiny.lehr.exceptions.ConflictMethodException;

@SuppressWarnings("rawtypes")
public class Router {

	private HashMap<String, MyMethod> methodMap = new HashMap<>(16);

	
	protected Router()
	{
		
	}
	
	protected void registerMethod(Class clazz) throws ConflictMethodException {

		// 遍历所有方法，通过注解找出来已经注册了的方法
		for (Method method : clazz.getDeclaredMethods()) {

			if (method.isAnnotationPresent(LehrsMethod.class)) {
				// 获取注解
				LehrsMethod lm = method.getAnnotation(LehrsMethod.class);
				// 注册
				String requestPathUri = fixUri(clazz, method);

				System.out.println("注册方法：" + method.getName() + ",uri:" + requestPathUri + "，请求类型：" + lm.requestType());

				// 检测冲突 这里我先规定的是不准同请求方式同uri的方法重载....具体我有点想不起SpringMVC的是怎么处理的了
				MyMethod existMethod = methodMap.get(lm.pathUri());
				if (existMethod != null && existMethod.getRequestType().equals(lm.requestType())) {
					throw new ConflictMethodException("方法重复了！");
				}

				// 用于记录参数名和参数类型的表
				HashMap<String, Class> paraMap = new HashMap<>(16);

				// 记录参数
				for (Parameter p : method.getParameters()) {
					ParamName pn = p.getAnnotation(ParamName.class);
					paraMap.put(pn.name(), p.getType());
					System.out.println("Parameter:" + pn.name() + ", Class:" + p.getType());
				}

				// 存入
				methodMap.put(requestPathUri, new MyMethod(lm.requestType(), method, clazz, paraMap));

			}
		}
	}
	
	private String fixUri(Class clazz, Method method) {
		LehrsMethod lm = method.getAnnotation(LehrsMethod.class);

		// 我也不知道为什么非要强转
		@SuppressWarnings("unchecked")
		ThisGonnaBeADynamicMethod tg = (ThisGonnaBeADynamicMethod) clazz.getAnnotation(ThisGonnaBeADynamicMethod.class);

		String fixed = tg.pathUri() + "/" + lm.pathUri();

		if (fixed.charAt(0) != '/') {
			fixed = "/" + fixed;

		}

		fixed = fixed.replaceAll("[/]+", "/");

		if (fixed.charAt(fixed.length() - 1) == '/') {
			fixed = fixed.substring(0, fixed.length() - 1);
		}

		return fixed;

	}


	/**
	 * 获取方法，这里规定方法返回值必须是字符串或者是空的 以后如果要实现重载功能的话就再在这里扩展
	 * 
	 * @param <T>
	 * @param uri
	 * @param requestType
	 * @return
	 */
	protected <T> MyMethod getMethod(String uri, RequestType requestType) {
		MyMethod m = methodMap.get(uri);

		if (m == null) {
			return null;
		}

		if (requestType != m.getRequestType()) {
			return null;
		}

		return m;

	}

}
