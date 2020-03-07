package tiny.lehr.tomcat.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemOutLogger
        extends TommyLogger {

    @Override
    public void doLog(String levelName, String message, Throwable cause, String klass) {

        Date date = new Date();//此时date为当前的时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");


        StringBuilder sb = new StringBuilder().append(dateFormat.format(date))
                .append("  [")
                .append(levelName)
                .append("]：[")
                .append(klass)
                .append("]")
                .append(":\t")
                .append(message);

        if (cause != null) {
            sb.append("\n")
                    .append("the cause is:")
                    .append(cause);
        }


        System.out.println(sb);
    }
}
