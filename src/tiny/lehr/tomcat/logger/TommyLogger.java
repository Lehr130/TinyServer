package tiny.lehr.tomcat.logger;

/**
 * @author Lehr
 * @create: 2020-03-02
 */
public abstract class TommyLogger {

    //冗余级别
    public static final int FATAL = Integer.MIN_VALUE;

    public static final int ERROR = 1;

    public static final int WARNING = 2;

    public static final int INFORMATION = 3;

    public static final int DEBUG = 4;

    private int verbosity = 4;

    public void setVerbosity(int verbosity) {
        this.verbosity = verbosity;
    }

    public int getVerbosity() {
        return this.verbosity;
    }


    public void info(String message) {
        info(message, null);
    }

    public void info(String message, Throwable cause) {
        log(INFORMATION, message, cause);
    }

    public void debug(String message) {
        debug(message, null);
    }

    public void debug(Throwable cause) {
        debug(null, cause);
    }

    public void debug(String message, Throwable cause) {
        log(DEBUG, message, cause);
    }

    public void error(String message) {
        error(message, null);
    }

    public void error(String message, Throwable cause) {
        log(ERROR, message, cause);
    }

    //如果当前数字越小，则是越重要的情况，则越需要输出
    private Boolean checkLevel(int level) {
        return level <= verbosity;

    }


    private void log(int level, String message, Throwable cause) {

        //如果级别不需要，则不用输出
        if (!checkLevel(level)) {
            return;
        }

        String levelName = null;

        if(level==1)
        {
            levelName = "ERROR";
        }
        if(level==2)
        {
            levelName = "WARNGING";
        }
        if(level==3)
        {
            levelName = "INFO";
        }
        if(level==4)
        {
            levelName = "DEBUG";
        }

        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        String klass = stackTrace[3].getClassName();
        doLog(levelName,message,cause,klass);

    }

    public abstract void doLog(String levelName, String message, Throwable cause,String klass);


}
