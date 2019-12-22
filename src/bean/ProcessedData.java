package bean;

import message.Code;

public class ProcessedData {

	private String fileType;
	
	private byte[] fileContent;
	
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
