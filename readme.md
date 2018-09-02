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