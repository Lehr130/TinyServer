package tiny.lehr.bean;

import java.util.HashMap;

import tiny.lehr.enums.ServerType;

/**
 * 
 * @author Lehr
 * @date 2019年12月17日
 * 
 */
public class ParsedResult {

	private String parseUri;

	private ServerType type;

	private HashMap<String,String> params;

	public String getParseUri() {
		return parseUri;
	}

	public ParsedResult(String parseUri, ServerType type, HashMap<String,String> params) {
		super();
		this.parseUri = parseUri;
		this.type = type;
		this.params = params;
	}

	public void setParseUri(String parseUri) {
		this.parseUri = parseUri;
	}

	public ServerType getType() {
		return type;
	}

	public HashMap<String,String> getParams() {
		return params;
	}

	public void setParams(HashMap<String,String> params) {
		this.params = params;
	}

	public void setType(ServerType type) {
		this.type = type;
	}
}
