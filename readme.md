### 如何运行

*   maven
*   jdk1.8
*   安装lombok

具体的运行测试可以在 `top.huzhurong.aop.core.TestUse` 中找到，have a try  😊

### 申明式事务

*   事务的处理步骤
    *   begin/start;(开启事务)
    *   sql operation;(sql操作,curd)
    *   exception(异常rollback)
    *   success(commit)

> 如上即是事务的完整处理步骤，而sql操作当中又可以细分为多次操作(子事务)，这样就可以解决保存点的问题，
每一个子事务都是完整的可以不影响父事务，`innodb的平板事务模型`.

#### 如何实现申明式事务处理

添加 `TransactionAdvisor` 切面拦截实现了申明式事务，事务的原子操作面向的其实 `java.sql.Connection`，
即只要通过控制`多个Service`之间的`Connection`是一个即可实现事务的完整操作。

### bean实例化处理顺序(Spring大致实现顺序)

0、创建bean实例

1、创建bean,eg:test(实例化)

2、调用aware接口实现aware(beanFactory)注入

3、beanPostProcessor init前置处理 BeanPostProcessorsBeforeInitialization

4、前置处理完成之后，上下文容器context(ioc 容器)注入

5、处理InitializingBean后置实现 afterPropertiesSet,自定义 init 方法，init方法在后边

6、beanPostProcessor init后置处理 BeanPostProcessorsAfterInitialization


>ps:实力不够，所以在Spring上进行一次变种，极大的简化了这一层面的操作