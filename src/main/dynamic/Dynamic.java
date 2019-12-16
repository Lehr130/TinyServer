package main.dynamic;

import org.junit.Test;

import annotation.LehrsMethod;
import message.RequestType;

/**
 * 目前迫于技术限制，只能在这一个类里写方法
 * 
 * @author Lehr
 * @date 2019年12月16日
 *
 */
public class Dynamic {

	@LehrsMethod(pathUri = "/users/sayHey", requestType = RequestType.GET)
	public static String sayHey(String name, String says) {
		return "Hey Lehr! It's Dynamic!" + " and " + name + " says: " + says;
	}

	@LehrsMethod(pathUri = "/users/sayBye", requestType = RequestType.GET)
	public static String sayBye(String name, String says) {
		return "Bye Lehr" + " and " + name + " says: " + says;
	}
	
	@LehrsMethod(pathUri = "/users/count", requestType = RequestType.GET)
	public static Integer getName(Integer firstName, Integer lastName) {
		Integer sum =  firstName+lastName;
		return sum;
	}
	

	

}
