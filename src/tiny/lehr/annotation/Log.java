package tiny.lehr.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
/**
 * @author Lehr
 * @create 2020-01-14
 * 用来生成日志的注解
 */
public @interface Log {

}
