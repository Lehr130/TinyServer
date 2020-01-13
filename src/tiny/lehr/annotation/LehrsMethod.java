package tiny.lehr.annotation;

import tiny.lehr.enums.RequestType;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Lehr
 * @create 2020-01-14
 * <hr>
 * 这个注解用来标记静态方法，使得其能够被动态导入,例子如下：
 * <br>
 * @LehrsMethod(pathUri="test/hey",requestType=RequestType.GET)<br>
 * public static void test(){<br>
 * 		System.out.println("Hey!");<br>
 * }<br>
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface LehrsMethod {

	/**
	 * 该方法对应的路径映射
	 * @return
	 */
	String pathUri();

	/**
	 * 请求该方法的请求类型，默认是GET
	 * @return
	 */
	RequestType requestType() default RequestType.GET;
	
	
	
	
	
}
