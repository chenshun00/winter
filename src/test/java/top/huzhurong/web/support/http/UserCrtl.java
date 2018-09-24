package top.huzhurong.web.support.http;

import top.huzhurong.web.annotation.Controller;
import top.huzhurong.web.annotation.Json;
import top.huzhurong.web.annotation.RequestMapping;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/20
 */
@RequestMapping("info")
@Controller
public class UserCrtl {


    @Json
    @RequestMapping("add")
    public Object add(User user) {
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
    public Object query(String name, Integer age, String hello, String world) {
        System.out.println("age:" + age);
        System.out.println("world:" + world);
        int i = 100 / 0;
        System.out.println("hello:" + hello);
        return "query" + name;
    }

}
