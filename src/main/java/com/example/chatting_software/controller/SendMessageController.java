package com.example.chatting_software.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SendMessageController {
    @RequestMapping("/send_message")
    public String sendMessage() {
        System.out.println("SEND");
        return "redirect:/main.html";
    }
}
