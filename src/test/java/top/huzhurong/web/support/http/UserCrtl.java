package top.huzhurong.web.support.http;

import lombok.extern.slf4j.Slf4j;
import top.huzhurong.ioc.bean.IocContainer;
import top.huzhurong.ioc.bean.aware.IocContainerAware;
import top.huzhurong.web.annotation.*;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/20
 */
@RequestMapping("t")
@Controller
@Slf4j
public class UserCrtl implements IocContainerAware {

    @Json
    @RequestMapping(value = "{name}/{info}/{age}/q.json")
    public Object test(@PathVariable("name") String name, @PathVariable("info") String info,
                       @PathVariable("age") Integer age, @RequestParam("chen") String ccc) {
        return name + "--" + info + "--" + age + "--ccc:--" + ccc;
    }

    @Json
    @RequestMapping("add")
    public Object add(@RequestParam("gg") User user) {
        System.out.println("user----:" + user);
        return user;
    }

    @Json
    @RequestMapping(value = "update", method = RequestMethod.PUT)
    public Object update() {
        return "update";
    }

    @Json
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public Object delete() {
        return "delete";
    }

    @Json
    @RequestMapping(value = "query", method = RequestMethod.GET)
    public Object query(@RequestParam("namee") String name, @RequestParam("agee") Integer age, @RequestParam("helloo") String hello,
                        @RequestParam("worldd") String world) {
        System.out.println("age:" + age);
        System.out.println("world:" + world);
        System.out.println("hello:" + hello);
        return "query" + name;
    }

    private IocContainer iocContainer;

    @Json
    @RequestMapping("cc")
    public User factoryBean() {
        User bean = iocContainer.getBean(User.class);
        log.info("user :{}", bean);
        return bean;
    }

    @Override
    public void setIocContainer(IocContainer iocContainer) {
        this.iocContainer = iocContainer;
    }
}
