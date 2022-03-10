package com.example.chatting_software.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GotoRegisterController {
    @RequestMapping("/user/gotoRegister")
    public String gotoRegister() {
        System.out.println("gotoRegister");
        return "forward:/register.html";
    }

}
