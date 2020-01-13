package tiny.lehr.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import tiny.lehr.enums.RequestType;

/**
 *      
 * @author Lehr
 * @date 2019年12月17日
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
	
	String pathUri();
	
	RequestType requestType() default RequestType.GET;
	
	
	
	
	
}
