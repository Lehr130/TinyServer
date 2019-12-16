package annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import message.RequestType;

@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface LehrsMethod {
	
	String pathUri();
	
	RequestType requestType() default RequestType.GET;
	
	
	
	
	
}
