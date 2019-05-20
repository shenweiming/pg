package cn.ecust.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Auther: SWM
 * @Date: 2019/5/12 20:50
 * @Description: 
 */
@Slf4j
@Controller
public class IndexController {
    @GetMapping("index")
    public String showIndex(){
        log.info("用户请求主页");
        return "index";
    }
    @GetMapping("login")
    public String showLogin(){
        log.info("用户请求登陆");
        return "login";
    }
    @GetMapping("register")
    public String showRegister(){
        log.info("用户请求注册");
        return "register";
    }
    @GetMapping("forget")
    public String showForget(){
        log.info("用户请求注册");
        return "forgot_password";
    }

}
