package tiny.lehr.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 
 * @author Lehr
 * @date 2019年12月17日
 * 
 */
@Documented
@Retention(RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ParamName {
	
	String name();
	
}
