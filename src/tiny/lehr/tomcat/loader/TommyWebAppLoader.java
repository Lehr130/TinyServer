package tiny.lehr.tomcat.loader;

import tiny.lehr.tomcat.container.TommyContext;
import tiny.lehr.tomcat.lifecircle.TommyLifecycle;
import tiny.lehr.tomcat.lifecircle.TommyLifecycleListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Lehr
 * @create: 2020-01-17
 * 一个封装好了的类加载器，用于加载Servlet到Wrapper里
 * 现在就是不知道能不能解析Jar包里的东西了
 */
public class TommyWebAppLoader extends ClassLoader implements TommyLifecycle {

    private String path;

    //用于之前加载的类文件缓存
    private Map<String, Class> resourcesMap = new HashMap<>();


    //用于记录所有文件修改情况
    private Map<String, Long> records;

    private TommyContext container;

    //默认关闭重载
    private Boolean reloadable;


    public TommyWebAppLoader(String path, Boolean reloadable, TommyContext container) {
        this.path = path;
        this.reloadable = reloadable;
        this.container = container;
    }

    private void addResource(String name, Class c) {
        resourcesMap.put(name, c);
    }

    /**
     * 去自己定义的目录下加载类
     *
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        Class c = null;

        name = name.replace('.', '/');
        String fileName = path + File.separator + "WEB-INF/classes/" + name + ".class";
        /*

         *   对于InputStream is = getClass().getResourceAsStream("/"+fileName)
         *
         *  我去看了一下他底层的代码，如果fileName前面没/就意味着从当前classpath下然后再接上后面的作为相对路径
         *  我多加一个这个直接让他作为绝度路径试试，从而实现自由加载
         *  然而我发现他这个方法好像是本地去调用另外一个类加载器，然后还是本地方法，什么什么之类的，反正还是会错
         *  所以我就选择不用这个接口了，直接用文件操作把文件以二进制方式读入
         *
         */

        try (InputStream is = new FileInputStream(fileName)) {

            byte[] data = new byte[is.available()];

            is.read(data);
            //然后又要把名字变回来.....
            name = name.replace('/', '.');
            c = defineClass(name, data, 0, data.length);

            //加入缓存
            addResource(name, c);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return c;


    }

    private Class<?> findLoadedClass0(String name) {

        return resourcesMap.get(name);
    }

    /**
     * 重写一个自定义的类加载器，用来打破双亲委派模型
     * 但是同时要确保能够调用到ext加载器来加载java.lang的内容
     * <p>
     * Web应用类加载器默认的加载顺序是：
     * <p>
     * (1).先从缓存中加载；
     * (2).如果没有，则从JVM的Bootstrap类加载器加载；
     * (3).如果没有，则从当前类加载器加载（按照WEB-INF/classes、WEB-INF/lib的顺序）；（默认是不用双亲委派的，当然也有选项可以的）
     * (4).如果没有，则从父类加载器加载，由于父类加载器采用默认的委派模式，所以加载顺序是AppClassLoader、Common、Shared。
     *
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {

        //先傻逼处理一下javax/servlet
        if (name.contains("javax.servlet")) {
            return getSystemClassLoader().loadClass(name);
        }
        //检查当前类加载器里加载过没有
        Class c = findLoadedClass0(name);

        if (c == null) {
            //检查虚拟机里加载过没有
            c = findLoadedClass(name);
        }


        if (c == null) {
            try {
                //JavaSE  loader的一个概念 还要判断包是不是来自基础库的
                ClassLoader javaseLoader = String.class.getClassLoader();
                if (javaseLoader == null) {
                    // System.out.println("我是空的！！！");

                    //我也不知道为什么要做这一步设计？？？上面的那个不是必定是null吗为何不直接用ext加载器啊

                    //那加载servlet.jar还是有问题啊，是不是他就交给sharedClassLoader了？？？

                    javaseLoader = getSystemClassLoader();
                    while (javaseLoader.getParent() != null) {
                        javaseLoader = javaseLoader.getParent();
                    }
                }
                c = javaseLoader.loadClass(name);
            } catch (ClassNotFoundException e) {

            }

        }

        if (c == null) {
            //利用自己重写的findClass方法来加载目标文件夹下的类
            c = findClass(name);

        }

        return c;

    }

    //这三个都是空的
    @Override
    public void addLifecycleListener(TommyLifecycleListener listener) {

    }

    @Override
    public List<TommyLifecycleListener> findLifecycleListeners() {
        return null;
    }

    @Override
    public void removeLifecycleListener(TommyLifecycleListener listener) {

    }


    //因为他不会管理子容器 所以他里面的实现就很简单
    @Override
    public synchronized void start() {
        System.out.println("Hey! Loader is starting!");

        records = recordFiles();

    }

    @Override
    public synchronized void stop() {

    }

    public void backgroundProcess() {

        System.out.println("热加载检查ing");

        if (reloadable && modified()) {

            System.out.println("触发了热加载");
            container.reload();
        }
    }


    private Boolean modified() {

        Map<String, Long> newRecords = recordFiles();

        return !newRecords.equals(records);

    }

    //这个其实设计得比较简陋，只是一个用file url记录文件修改时间的集合而已 用来做热加载检查
    private Map<String, Long> recordFiles() {
        Map<String, Long> records = new HashMap<>();

        getFiles(new File(path)).forEach(f ->
                records.put(f.getPath(), f.lastModified())
        );

        return records;
    }


    //TODO: 太傻逼了！！！！有空回来改！！！！
    private List<File> getFiles(File file) {
        List<File> files1 = Arrays.asList(file.listFiles());

        List<File> files2 = new ArrayList<>();

        files1.forEach(f -> {
            if (f.isDirectory()) {
                List<File> secondary = getFiles(f);
                files2.addAll(secondary);
            }
            if (f.isFile()) {
                files2.add(f);
            }
        });

        return files2;
    }

}
