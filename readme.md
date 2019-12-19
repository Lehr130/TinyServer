# Lehr的Tiny Server开发计划


<li>Java输入输出流NIO管道加速</li>
<li>美化ui</li>
<li>添加服务器缓存功能（目前有个很简陋的LFU算法的缓存），接下来打算缓存要先判断文件有没有被修改才缓存</li>
<li>学习使用NIO非阻塞的方式提高响应速度</li>
<li>动态代理orAOP+Logger生成日志</li>
<li>对动态方法实现重载</li>
<li>实现代理功能</li>
<li>用户给动态方法配的路由的语法检查--->优先服务静态方法</li>
<li>增加解析Cookie的能力</li>
<li>HTTP/1.1长连接似乎没有生效......</li>
<li>搞懂迭代器和URLClassLoader</li>
<li>目前已经完成导入任意Jar包，按照规定书写即可响应，但是代码很烂</li>
<li>迭代解析Jar中Jar</li>