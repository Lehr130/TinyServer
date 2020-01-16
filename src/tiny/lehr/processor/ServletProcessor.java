package tiny.lehr.processor;

import tiny.lehr.bean.*;
import tiny.lehr.enums.Code;

import javax.servlet.Servlet;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Lehr
 * @create 2020-01-14
 * 用来处理Servlet请求的处理器
 * 由于之前的结构问题，并不能像Tomcat一样直接拿输出流回传信息
 * 这样，设计的时候给ServletResponse传入的不是socket的writer,
 * 而是给一个新的writer,获取输入之后再统一合并到后面的步骤去
 */
public class ServletProcessor extends Processor {


    private static URLClassLoader urlLoader = null;

    static {
        try {
            //这个必须是唯一的
            urlLoader = new URLClassLoader(new URL[]{new URL("file:")});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }



    /**
     * 由于之前的接口设计问题，所以暂时只能把ServletRequest放到里面来生成了
     *
     * @param socket
     * @param request
     * @param parsedResult
     * @return
     */
    @Override
    protected ProcessedData prepareData(Socket socket, MyRequest request, ParsedResult parsedResult) {


        //我这里应该去建立一对节点流，给传入output，获取input
        try (Socket dumpSocket = new Socket()) {

            //这个是要传入的writer
            PrintWriter servletWriter = new PrintWriter(dumpSocket.getOutputStream());
            //这个是要用来读出的reader
            InputStream input = dumpSocket.getInputStream();

            //完善请求和响应体
            TommyRequest tRequest = new TommyRequest();



            //封装请求
            //tRequest.setRequestFromMyRequest(request);

            //传入writer
            TommyResponse tResponse = new TommyResponse(servletWriter, tRequest);

            //获取目标Servlet名字
            String servletName = parsedResult.getParseUri();

            //调用类加载器来处理请求
            Class servletClass = urlLoader.loadClass(servletName);

            //获取Servlet实例
            Servlet servlet = (Servlet) servletClass.newInstance();

            //要不要再来对导入的类做一个类型检查

            //暂时还没有设计

            //执行service
            servlet.service(tRequest, tResponse);

            //获取刚才传入的哪个输入流的输出并放入文件
            byte[] buffer = new byte[input.available()];
            input.read(buffer);

            return new ProcessedData("text/html", buffer, Code.OK);
        } catch (Exception e) {
            //这里代表Servlet执行失败了
            return new ProcessedData("text/html", null, Code.INTERNALSERVERERROR);
        }

    }
}
