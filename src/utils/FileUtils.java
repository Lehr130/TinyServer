package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * @author Lehr
 * @date 2019年12月11日
 */
public class FileUtils {

	
	public static byte[] fileToByte(String filename) throws IOException{
		
		
		
		
		File file = new File(filename);
		Long len = file.length();
		byte[] fileContent = new byte[len.intValue()];
		try (FileInputStream in = new FileInputStream(file)) {
			in.read(fileContent);
			
		} catch (IOException e) {
			throw e;
		}

		return fileContent;
	}
	

}
