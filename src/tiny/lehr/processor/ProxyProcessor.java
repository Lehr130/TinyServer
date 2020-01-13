package tiny.lehr.processor;

import tiny.lehr.bean.MyRequest;
import tiny.lehr.bean.MyX509TrustManager;
import tiny.lehr.bean.ParsedResult;
import tiny.lehr.bean.ProcessedData;
import tiny.lehr.enums.Code;
import tiny.lehr.enums.RequestType;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class ProxyProcessor extends Processor {

    @Override
    protected ProcessedData prepareData(Socket socket, MyRequest request, ParsedResult parsedResult) {

        byte[] buffer;

        String proxyUrl = parsedResult.getParseUri();

        try {

            if (proxyUrl.contains("https")) {
                buffer = sendHttpRequest(parsedResult, request);
            } else {
                buffer = sendHttpsRequest(parsedResult, request);
            }

            //这里的错误码，fileType可以变得更灵活
            return new ProcessedData("text/html", buffer, Code.OK);
        } catch (Exception e) {
            return new ProcessedData("text/html", null, Code.INTERNALSERVERERROR);

        }


    }

    private byte[] sendHttpsRequest(ParsedResult result, MyRequest request) throws NoSuchAlgorithmException, IOException, KeyManagementException {

        String targetUrl = result.getParseUri();
        RequestType type = request.getRequestType();

        SSLContext sslContext = SSLContext.getInstance("SSL");
        TrustManager[] tm = {new MyX509TrustManager()};
        sslContext.init(null, tm, new SecureRandom());
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        URL url = new URL(targetUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod(type.name());
        conn.setSSLSocketFactory(ssf);
        conn.connect();

        //读取服务器端返回的内容
        InputStream is = conn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        return buffer;

    }

    private byte[] sendHttpRequest(ParsedResult result, MyRequest request) throws IOException {
        URLConnection urlConnection = new URL(result.getParseUri()).openConnection();
        InputStream is = urlConnection.getInputStream();
        // 访问获取连接
        urlConnection.connect();
        // 获得一个输入流
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        return buffer;
    }
}
