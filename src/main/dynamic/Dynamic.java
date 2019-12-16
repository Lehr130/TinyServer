package main.dynamic;

import annotation.LehrsMethod;
import annotation.ParamName;
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
	public static String sayHey(@ParamName(name = "name")String name, @ParamName(name = "says")String says) {
		return "Hey Lehr! It's Dynamic!" + " and " + name + " says: " + says;
	}

	@LehrsMethod(pathUri = "/users/sayBye", requestType = RequestType.GET)
	public static String sayBye(@ParamName(name = "name")String name, @ParamName(name = "says")String says) {
		return "Bye Lehr" + " and " + name + " says: " + says;
	}
	
	@LehrsMethod(pathUri = "/users/count", requestType = RequestType.GET)
	public static Integer getName(@ParamName(name = "firstName")Integer firstName, @ParamName(name = "lastName")Integer lastName) {
		return firstName + lastName;
	}
	

	

}
