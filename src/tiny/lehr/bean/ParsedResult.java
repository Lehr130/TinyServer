package tiny.lehr.bean;

import tiny.lehr.enums.ServerType;

import java.util.HashMap;

/**
 * @author Lehr
 * @create 2020-01-14
 * 用来代表被处理过的请求的类型结果，内部封装了请求的内容来交给后面的处理器处理
 */
public class ParsedResult {

	/**
	 * 处理后的uri，比如有些是默认添加了index.html的，有的是被转化成代理路径的
	 */
	private String parseUri;

	/**
	 * 对应的是哪一种处理器来处理：静态?代理？DynamicJava？
	 */
	private ServerType type;

	/**
	 * 通过GET后面URL的参数
	 */
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
