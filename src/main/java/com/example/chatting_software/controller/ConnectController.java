package com.example.chatting_software.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ConnectController {
    @RequestMapping("/connect")
    public String connect() {
        System.out.println("CONNECT");
        return "redirect:/main.html";
    }
}
