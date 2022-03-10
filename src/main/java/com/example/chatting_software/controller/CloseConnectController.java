package com.example.chatting_software.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CloseConnectController {
        @RequestMapping("/close_connect")
    public String CloseConnect() {
        System.out.println("Close Connect");
        return "redirect:/main.html";
    }
}
