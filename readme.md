# Lehr的Tiny Server Java应用服务器

# 开发计划

## Servlet容器部分
- 完成热部署
- 热加载
- 关于重定向功能？
- 支持listener
- JSessionId的url重写实现一下
- Session的回收机制
- Session的本地持久化机制
- 再完善容器层级关系
- 实现war包的自动解压
- 各个类的权限问题：用门面模式再修一下
## 连接器部分
- Java NIO
- 改进那个很简陋的LFU算法的缓存
- 规定一个良好的配置文件设置
- 缓存要先判断文件有没有被修改才缓存
-  动态代理 + Logger + 注解类  ===》AOP生成日志
- 完善报错链
- HTTP/1.1长连接似乎没有生效......
- 迭代解析Jar中Jar