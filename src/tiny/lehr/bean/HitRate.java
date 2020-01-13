package tiny.lehr.bean;

/**
 * @author Lehr
 * @create 2020-01-14
 * 配合LFU实现简单的缓存替换算法
 */
public class HitRate implements Comparable<HitRate> {

    /**
     * 静态资源的uri
     */
    private String uri;
    /**
     * 击中次数
     */
    private Integer count;
    /**
     * 上次击中时间
     */
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
