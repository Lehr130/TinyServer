package tiny.lehr.processor;

import tiny.lehr.bean.MyRequest;
import tiny.lehr.bean.MyResponse;
import tiny.lehr.cache.CacheFacade;

import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 这是处理模式的抽象类，用于使用模板方法这个设计模式，后期可以加上工厂模式
 *
 * @author Lehr
 * @date 2019年12月22日
 */
public abstract class Processor {

    /**
     * 统一处理数据，并在自己内部处理异常，获得文件类型和错误码
     *
     * @param request
     * @return
     * @Param response
     */
    protected abstract void prepareData(MyRequest request, MyResponse response) throws Exception;

    public final void processRequest(MyRequest request, MyResponse response) {

        // 先处理数据
        try {
            prepareData(request, response);
        } catch (Exception e) {
            //错误处理
			// 向服务器记录报错信息
			System.err.println("fuck that!");

            //先全部404
            response.setResBody(CacheFacade.getInstance().getErrorPage("404"));

			response.setCode("404");

			response.setFileType("text/html");

        }finally {
			// 然后再发送请求
			sendResponse(response);
		}
    }


    /**
     * 发送响应
     */
    private static void sendResponse(MyResponse res) {


        // 记录时间
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");
        Date date = new Date();

        // 获取输出流 然后执行完后自动关闭socket（高的关了低的自动关）
		// IMPROVE: 这里我没采用直接获取PrintWriter 注意，这里为了避免一个重写的方法，这个名字是getOutStream!!!
        try (PrintStream output = new PrintStream(res.getOutStream(), true)) {

            // 响应头
            output.print(res.getProtocol() + " " + res.getCode() + " \r\n");
            output.print("Date:" + sdf.format(date) + "\r\n");
            output.print("Server:Lehr's Tiny Server \r\n");
            //放置曲奇饼
            res.getCookies().forEach(c->output.print("Set-Cookie: " + c.getName() + "=" +c.getValue()+ "\r\n"));

            if (res.getResBody() == null) {
                output.print("Content-length: 0\r\n\r\n");
            } else {
                // 所以这个最后浏览器渲染的结果还和这几个响应头的顺序有关？！？！
                output.print("Content-length: " + res.getResBody().length + "\r\n");
                output.print("Content-type: " + res.getFileType() + ";charset=utf-8" + "\r\n\r\n");
                // 响应体
                output.write(res.getResBody());
                output.print("\r\n");
            }

        } catch (IOException e) {
            // 这个还是没能处理
            e.printStackTrace();
        }

    }

}
