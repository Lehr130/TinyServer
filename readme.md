# Lehr的Tiny Server Java应用服务器

# 开发计划

## Servlet容器部分
- 完成热部署和热加载
- 完成session控制功能
- 完成MyRequest到ServletRequest的转换
## 连接器部分
- Java输入输出流NIO管道加速
- 改进那个很简陋的LFU算法的缓存
- 修改架构
- 学习UML
- 规定一个良好的配置文件设置
- 缓存要先判断文件有没有被修改才缓存
-  动态代理 + Logger + 注解类  ===》AOP生成日志
- 动态方法语法检查
- 完善报错链
- 增加解析Cookie的能力
- HTTP/1.1长连接似乎没有生效......
- 搞懂迭代器和URLClassLoader
-  目前已经完成导入任意Jar包，按照规定书写即可响应，但是代码很烂
- 迭代解析Jar中Jar
- TimerTask实现定时刷新加载的类否