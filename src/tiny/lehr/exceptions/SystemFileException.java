package tiny.lehr.exceptions;


/**
 * @author Lehr
 * @date 2019年12月11日
 */
public class SystemFileException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4685412544629025817L;
	
    public SystemFileException(String message) {
        super(message);
    }


	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "服务器缺失基本文件，无法启动";
	}

}
