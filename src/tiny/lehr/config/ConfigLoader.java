package tiny.lehr.config;

import tiny.lehr.enums.Message;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * @author Lehr
 * @create 2020-01-13
 *
 * 配置文件的读取方法都在这个里面
 */
public class ConfigLoader {

    protected static void loadAttribute(Map configMap) throws IOException {
        try (BufferedReader br = new BufferedReader( new FileReader(Message.ROOT_PATH+ File.separator+"systemFile"+File.separator+"ServerConfig.properties")))
        {
            String line = br.readLine();
            while (line != null) {
                line = line.trim();
                if (line.length() < 1 || line.charAt(0) == '#') {
                    line = br.readLine();
                    continue;
                }
                String[] parts = line.split("=");
                configMap.put(parts[0].trim(), parts[1].trim());
                line = br.readLine();
            }
        }
    }

    protected static void loadPorxyConfig(Map proxyMap) throws IOException {
        try (BufferedReader br = new BufferedReader( new FileReader(Message.ROOT_PATH+File.separator+"systemFile"+File.separator+"ProxyConfig.properties")))
        {
            String line = br.readLine();
            while (line != null) {
                line = line.trim();
                if (line.length() < 1 || line.charAt(0) == '#') {
                    line = br.readLine();
                    continue;
                }
                String[] parts = line.split("=");

                String orginal = parts[0].trim();
                String target = parts[1].trim();

                if(orginal.indexOf(0)=='/')
                {
                    if(target.endsWith("/"))
                    {
                        target = target + orginal.substring(1);
                    }
                }


                proxyMap.put(orginal, target);

                line = br.readLine();
            }
        }
    }


}
