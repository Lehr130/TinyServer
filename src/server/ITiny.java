package server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import annotation.Log;
import exceptions.BadRequestMethodException;
import exceptions.CannotFindException;
import exceptions.IllegalParamInputException;
import exceptions.ParamException;

public interface ITiny {

	/**
	 * 启动整个服务器并响应服务，大概步骤都在这个方法里面 1.加载配置文件 2.主线程接收请求并放入队列 3.工作线程开始从请求队列里取出请求并开始响应
	 * 
	 * @throws Exception
	 */
	@Log
	void startUp() throws Exception;

	/**
	 * 我自己抽出来的一层，用于报错处理
	 * 
	 * @param socket
	 */
	void doIt(Socket socket);

	/**
	 * 具体的服务执行阶段：解析请求-->进行处理-->返回结果
	 * 
	 * @param server
	 * @throws IOException
	 * @throws BadRequestMethodException
	 * @throws ParamException
	 * @throws CannotFindException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalParamInputException
	 * @throws Exception
	 */
	@Log
	void serveIt(Socket socket) throws IOException, BadRequestMethodException, IllegalAccessException,
			InvocationTargetException, CannotFindException, ParamException, IllegalParamInputException;

}