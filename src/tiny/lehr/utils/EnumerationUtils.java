package tiny.lehr.utils;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Lehr
 * @create: 2020-01-23
 */
public class EnumerationUtils {

    public static Enumeration<String> getEnumerationStringByMap(Map map)
    {
        Iterator<String> it = map.keySet().iterator();

        //只能很苦逼的包装一下了
        return new Enumeration<String>() {
            @Override
            public boolean hasMoreElements() {
                return it.hasNext();
            }

            @Override
            public String nextElement() {
                return it.next();
            }
        };
    }

}
