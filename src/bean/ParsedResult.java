package bean;

public class ParsedResult {

	private String parseUri;

	private Boolean isStatic;

	private String[] params;

	public String getParseUri() {
		return parseUri;
	}

	public ParsedResult(String parseUri, Boolean isStatic, String[] params) {
		super();
		this.parseUri = parseUri;
		this.isStatic = isStatic;
		this.params = params;
	}

	public void setParseUri(String parseUri) {
		this.parseUri = parseUri;
	}

	public Boolean isStatic() {
		return isStatic;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	public void setIsStatic(Boolean isStatic) {
		this.isStatic = isStatic;
	}
}
