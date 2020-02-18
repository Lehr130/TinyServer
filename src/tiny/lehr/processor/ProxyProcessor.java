package tiny.lehr.processor;

import tiny.lehr.bean.MyRequest;
import tiny.lehr.bean.MyResponse;
import tiny.lehr.bean.MyX509TrustManager;
import tiny.lehr.config.ConfigFacade;
import tiny.lehr.enums.Code;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class ProxyProcessor extends Processor {

    @Override
    protected void prepareData(MyRequest req, MyResponse res) throws Exception {

        byte[] buffer;

        String proxyUrl = ConfigFacade.getInstance().getProxy(req.getRequestURI());


        if (!proxyUrl.contains("https")) {
            buffer = sendHttpRequest(proxyUrl);
        } else {
            buffer = sendHttpsRequest(proxyUrl, req.getMethod());
        }

        //TODO: 这里的错误码，fileType可以变得更灵活
        res.setCode(Code.OK.getCode());
        res.setFileType("text/html");
        res.setResBody(buffer);

    }

    private byte[] sendHttpsRequest(String targetUrl, String requestMethod) throws NoSuchAlgorithmException, IOException, KeyManagementException {


        SSLContext sslContext = SSLContext.getInstance("SSL");
        TrustManager[] tm = {new MyX509TrustManager()};
        sslContext.init(null, tm, new SecureRandom());
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        URL url = new URL(targetUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod(requestMethod);
        conn.setSSLSocketFactory(ssf);
        conn.connect();

        //读取服务器端返回的内容
        InputStream is = conn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        return buffer;

    }

    private byte[] sendHttpRequest(String uri) throws IOException {
        URLConnection urlConnection = new URL(uri).openConnection();
        InputStream is = urlConnection.getInputStream();
        // 访问获取连接
        urlConnection.connect();
        // 获得一个输入流
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        return buffer;
    }
}
