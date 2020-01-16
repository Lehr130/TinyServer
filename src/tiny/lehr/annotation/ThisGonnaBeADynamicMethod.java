package tiny.lehr.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Lehr
 * @create 2020-01-14
 * 用来指定哪些类会被录入为动态方法
 */
@Documented
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface ThisGonnaBeADynamicMethod {

	/**
	 *这个类的映射的路径，
	 * 相当于SpringMVC里在顶上的RequestMapping
	 */
	String pathUri();
		
	
}
