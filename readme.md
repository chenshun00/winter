### 如何运行

*   按照 `maven` 
*   按照 `jdk1.8` 
*   安装 `lombok`，安装步骤 [Intellij IDEA 安装lombok及使用详解](https://blog.csdn.net/zhglance/article/details/54931430)


#### Aop 测试

##### 1、直接运行测试代码

`cglib` 测试代码可以在 `top.huzhurong.ioc.CglibProxyTest#cglib` 类中找到，直接运行即可出现如下结果
`jdk proxy` 测试代码可以在 `top.huzhurong.ioc.JdkProxyTest#jdkProxy` 类中找到，直接运行即可出现如下结果

```text
----# testScan1's initAware interface #----
----* v^v AspectjSecond aspectj before TestScan1 v^v *----
----# start invoke testScan1's hello method #----
----$ start invoke TestScan2's hello method $----
----^ invoke TestScan3's test3 method ^----
----$ end invoke TestScan2's hello method $----
----# end invoke testScan1's hello method #----
----* ^@^ AspectjSecond aspectj after TestScan1 ^@^ *----
```

##### 2、自定义测试

1、定义切面类，代码 `top.huzhurong.ioc.scan.test.AspectjTest.AspectjSecond` 所示，`@Aspecjt` 表示切面，`@Order` 表示切面执行顺序
2、定义被拦截类+方法
3、执行测试，具体细节请参考1
#### 申明式事务测试

1、加入test 目录下的test.sql文件，修改测试代码的url,user及password即可执行

```text
    @Test
    public void transactionTest() {
        TestService testService = (TestService) this.beanFactory.getBean("testService");
        Test test = Test.builder().age(22).id(18).name("test").build();
        testService.addTest(test);
    }
```

在 `TestService`中加入 `int i = 10/0` 即可测试异常发生后事务的回滚

```text
    @Transactional
    public void addTest(Test test) {
        testDao.addTest(test);
        Test testById = testDao.getTestById(1);
        testById.setName("transaction测试");
        testDao.updatetestById(testById);
        //注释即可打开异常
        //int i = 10 / 0;
    }
```

执行结果分别如下

```text
INFO  t.h.a.a.transaction.manager.JdbcTransactionManager - inject dataSource to jdbcTransactionManager
INFO  com.alibaba.druid.pool.DruidDataSource - {dataSource-1} inited
INFO  t.h.a.a.transaction.manager.TransactionInterceptor - 成功获取事务
INFO  top.huzhurong.ioc.transaction.TestDao - addTest方法当中，从ConnectionManager中获取数据库链接:com.mysql.jdbc.JDBC4Connection@587c290d
INFO  top.huzhurong.ioc.transaction.TestDao - getTestById方法当中，从ConnectionManager中获取数据库链接:com.mysql.jdbc.JDBC4Connection@587c290d
INFO  top.huzhurong.ioc.transaction.TestDao - updatetestById方法当中，从ConnectionManager中获取数据库链接:com.mysql.jdbc.JDBC4Connection@587c290d
INFO  top.huzhurong.ioc.transaction.TestDao - 执行的sql:update `test` set name = transaction测试 , age = 21 where id = 1
INFO  t.h.a.a.transaction.manager.JdbcTransactionManager - 事务提交

---------------------执行事务出现异常----------------------
2018-09-15 15:22:10.587 [main] INFO  com.alibaba.druid.pool.DruidDataSource - {dataSource-1} inited
2018-09-15 15:22:10.822 [main] INFO  t.h.a.a.transaction.manager.TransactionInterceptor - 成功获取事务
2018-09-15 15:22:10.822 [main] INFO  top.huzhurong.ioc.transaction.TestDao - addTest方法当中，从ConnectionManager中获取数据库链接:com.mysql.jdbc.JDBC4Connection@587c290d
2018-09-15 15:22:10.838 [main] INFO  top.huzhurong.ioc.transaction.TestDao - getTestById方法当中，从ConnectionManager中获取数据库链接:com.mysql.jdbc.JDBC4Connection@587c290d
2018-09-15 15:22:10.841 [main] INFO  top.huzhurong.ioc.transaction.TestDao - updatetestById方法当中，从ConnectionManager中获取数据库链接:com.mysql.jdbc.JDBC4Connection@587c290d
2018-09-15 15:22:10.841 [main] INFO  top.huzhurong.ioc.transaction.TestDao - 执行的sql:update `test` set name = transaction测试 , age = 21 where id = 1
2018-09-15 15:22:10.842 [main] ERROR t.h.a.a.transaction.manager.TransactionInterceptor - 事务处理出现异常:null,开始进行事务回滚
2018-09-15 15:22:10.842 [main] INFO  t.h.a.a.transaction.manager.JdbcTransactionManager - 事务回滚
java.lang.reflect.InvocationTargetException

	at top.huzhurong.aop.advisor.transaction.manager.TransactionInterceptor.doTransaction(TransactionInterceptor.java:38)
	at top.huzhurong.aop.advisor.transaction.TransactionAdvisor.invoke(TransactionAdvisor.java:42)
	at top.huzhurong.aop.invocation.AbstractInvocation.proceed(AbstractInvocation.java:55)
	at top.huzhurong.aop.invocation.ProxyFactory.lambda$createProxy$0(ProxyFactory.java:19)
	at top.huzhurong.ioc.transaction.TestService$$EnhancerByCGLIB$$583d7058.addTest(<generated>)
	at top.huzhurong.ioc.CglibProxyTest.transactionTest(CglibProxyTest.java:57)
Caused by: java.lang.ClassNotFoundException: xxx.xxx.xxx.xxx
	at java.net.URLClassLoader.findClass(URLClassLoader.java:381)
	at java.lang.ClassLoader.loadClass(ClassLoader.java:424)
	at sun.misc.Launcher$AppClassLoader.loadClass(Launcher.java:338)
	at java.lang.ClassLoader.loadClass(ClassLoader.java:357)
	at java.lang.Class.forName0(Native Method)
	at java.lang.Class.forName(Class.java:264)
	at top.huzhurong.ioc.transaction.TestService.addTest(TestService.java:33)

```


##### 如何实现申明式事务处理

添加 `TransactionAdvisor` 切面拦截实现了申明式事务，事务的原子操作面向的其实 `java.sql.Connection`，
即只要通过控制`多个Service`之间的`Connection`是一个即可实现事务的完整操作。

### bean实例化处理顺序(Spring大致实现顺序)

0、创建bean实例

1、创建bean,eg:test(实例化)

2、调用aware接口实现aware(beanFactory)注入

3、beanPostProcessor init前置处理 BeanPostProcessorsBeforeInitialization

4、前置处理完成之后，上下文容器context(ioc 容器)注入

5、处理InitializingBean后置实现 afterPropertiesSet,自定义 init 方法，init方法在后边

6、beanPostProcessor init后置处理 BeanPostProcessorsAfterInitialization,bean对象可能在这里包装成代理


>ps:实力不够，所以在Spring上进行一次变种，极大的简化了这一层面的操作

### 问题
  
1、使用 `cglib` 和 `jdk proxy` 的时候，两者的实现动态代理的原理是不一致的，`cglib` 使用继承父类的的形式展开，这样我们就可以通过向上
转型(cast)将该代理(子类)转型为父类。而 `jdk proxy` 则采取了另一种形式，实现相同的接口，这就是为什么 `jdk proxy` 一定需要接口的原因，如果没有接口那么就代理
失败，正式因为通过实现接口的方式导致了它只能转型为实现的接口，如果转型为子类的那么就会出现，  `$proxy0 cast xxx.xxx.xx` 的异常信息，
可以到这里看看 `jdk proxy` 生成的 `$proxy0` -----> [$proxy0](https://git.io/fAKeS)  


2、为什么 `jdk代理` 和 `cglib代理` 注入的时机不一致，jdk 注入的时候是在生成代理之前，因为生成代理的时候是实现的接口，而接口是没有 `setXXX` 的 `set方法` 的，而 `cglib` 的注入是采用继承去实现的
如果先注入后继承，发现存在原先注入的字段为null了，经过查看发现不是我写的bug，而是cglib本身传递的就是xxx.class，没有将对象传递进去，导致它的字段为null，为此
我重写了一下  `cglib` 的一个方法，才获取原先注入的 `field`，可以在这里看我提的 `issue` ---> [cglib issue](https://github.com/cglib/cglib/issues/134)