package logger;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import message.Message;


/**
 * 
 * @author Lehr
 * @date 2019年12月21日
 * 
 */
public class MyLogger{

	private static MyLogger myLogger;
	
	private Logger logger;
	
	private MyLogger() throws SecurityException, IOException {
		logger = Logger.getLogger("Lehr's Logger:");
		FileHandler fileHandler = new FileHandler(Message.ROOT_PATH + File.separator + "3.txt");
		fileHandler.setFormatter(new MyFormatter());
		logger.addHandler(fileHandler);
	}
	
	
	public static MyLogger getInstance()
	{
		if(myLogger==null)
		{
			try {
				myLogger = new MyLogger();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return myLogger;
	}
	
	public void info(String message)
	{
		logger.info(message);
	}

	
	
	
}
