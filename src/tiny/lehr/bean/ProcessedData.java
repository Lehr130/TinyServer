package tiny.lehr.bean;

import tiny.lehr.enums.Code;

/**
 * @author Lehr
 * @create 2020-01-14
 * 这个Bean代表Processor处理后的数据，即可作为信息交给Response
 */
public class ProcessedData {

	/**
	 * 要响应的文件的类型
	 */
	private String fileType;

	/**
	 * 文本内容
	 */
	private byte[] fileContent;

	/**
	 * 状态码
	 */
	private Code code;

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public ProcessedData(String fileType, byte[] fileContent, Code code) {
		super();
		this.fileType = fileType;
		this.fileContent = fileContent;
		this.code = code;
	}
	
	
	
}
