package tiny.lehr.enums;


/**
 * @author Lehr
 * @date 2019年12月11日
 */
public enum Code {
	
	OK("200","Success To Response GET Request"),
	
	INTERNALSERVERERROR("500","Server's Error"),
	
	PARAMILLEGAL("1000","The Wrong Type of Parameter Has Been Input"),
	
	BADREQUEST("400","Do Not Support The Method To Do This Request"),
	
	PARAMWRONG("1001","Wrong Name or Number of Parameters"),
	
	NOTFOUND("404","Cannot Find The Method Or The Resource");
	
	String CODE;
	String TIP;
	
	private Code(String cODE, String tIP) {
		CODE = cODE;
		TIP = tIP;
	}
	
	public String getCode()
	{
		return CODE;
	}
	
	
	
}
