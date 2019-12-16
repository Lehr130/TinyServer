package bean;

import java.util.HashMap;

public class ParsedResult {

	private String parseUri;

	private boolean isStatic;

	private HashMap<String,String> params;

	public String getParseUri() {
		return parseUri;
	}

	public ParsedResult(String parseUri, boolean isStatic, HashMap<String,String> params) {
		super();
		this.parseUri = parseUri;
		this.isStatic = isStatic;
		this.params = params;
	}

	public void setParseUri(String parseUri) {
		this.parseUri = parseUri;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public HashMap<String,String> getParams() {
		return params;
	}

	public void setParams(HashMap<String,String> params) {
		this.params = params;
	}

	public void setIsStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}
}
