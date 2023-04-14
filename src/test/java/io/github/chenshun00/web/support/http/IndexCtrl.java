package io.github.chenshun00.web.support.http;

import io.github.chenshun00.aop.annotation.Transactional;
import io.github.chenshun00.web.annotation.Controller;
import io.github.chenshun00.web.annotation.RequestMapping;
import io.github.chenshun00.web.support.impl.Request;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author chenshun00@gmail.com
 * @since 2023/4/14 16:02
 */
@RequestMapping("")
@Controller
@Slf4j
public class IndexCtrl {

    @Transactional
    @RequestMapping("/")
    public Object first(Request request, String cc) throws IOException {
        return "<h1>hello world</h1>";
    }
}
