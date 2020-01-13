package tiny.lehr.bean;

/**
 * @author Lehr
 * @date 2019年12月11日
 */
public class HitRate implements Comparable<HitRate>{

	private String uri;
	private Integer count;
	private Long lastTime;
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Long getLastTime() {
		return lastTime;
	}
	public void setLastTime(Long lastTime) {
		this.lastTime = lastTime;
	}
	
	public HitRate(String uri, Integer count, Long lastTime) {
		this.uri = uri;
		this.count = count;
		this.lastTime = lastTime;
	}
	
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	@Override
	public int compareTo(HitRate o) {
		
		
		//我觉得我还要去复习下这里
		Integer hr = this.count.compareTo(o.count);
		
		return hr != 0 ? hr : this.lastTime.compareTo(o.lastTime);
		
		
	}
	
	
	
	
	
	
}
