package tiny.lehr.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Lehr
 * @create 2020-01-14
 * 用来标记参数名字的注解
 */
@Documented
@Retention(RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ParamName {

	/**
	 * 用来记录方法名字
	 *
	 */
	String name();
	
}
