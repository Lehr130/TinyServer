package exceptions;


/**
 * @author Lehr
 * @date 2019年12月11日
 */
public class ParamException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4685412524623025817L;
	
	public ParamException(String message) {
        super(message);
    }

	public ParamException() {
		
	}
	
}
