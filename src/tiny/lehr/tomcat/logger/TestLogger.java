package tiny.lehr.tomcat.logger;

public class TestLogger {
    public static void main(String[] args) {
        TommyLogger logger = new SystemOutLogger();
        logger.info("SystemOutLogger");

    }
}
