package top.huzhurong.web.support.http;

import top.huzhurong.web.annotation.*;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/20
 */
@RequestMapping("t")
@Controller
public class UserCrtl {

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
//        int i = 100 / 0;
        System.out.println("hello:" + hello);
        return "query" + name;
    }

}
