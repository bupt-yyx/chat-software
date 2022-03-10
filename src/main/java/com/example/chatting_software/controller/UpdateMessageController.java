package com.example.chatting_software.controller;

import org.springframework.boot.web.server.Http2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpSession;

@Controller
public class UpdateMessageController {

    @RequestMapping(value = "/update/{message}/{channel_select}/{input_onfocus}/{select_channel_onfocus}")
    public String update(@PathVariable("message") String message,
                         @PathVariable("channel_select") Integer channel_select,
                         @PathVariable("input_onfocus") String input_onfocus,
                         @PathVariable("select_channel_onfocus") String select_channel_onfocus,
                         RedirectAttributesModelMap model,
                         HttpSession session) {
        System.out.println("UPDATE");
        System.out.println("last_input_onfocus = " + input_onfocus.toString());
        System.out.println("last_select_channel_onfocus = " + select_channel_onfocus.toString());

        if(message.equals("99999999")) {
            message = "";
        }
        if(channel_select.equals(99999999)) {
            channel_select = null;
        }
        model.addFlashAttribute("last_message", message);
        model.addFlashAttribute("last_channel_select", channel_select);
        session.setAttribute("last_input_onfocus", input_onfocus.equals( "true"));
        session.setAttribute("last_select_channel_onfocus", select_channel_onfocus.equals("true"));

     //   model.addFlashAttribute("last_input_onfocus", input_onfocus == "true" ? true : false);
    //    model.addFlashAttribute("last_select_channel_onfocus", select_channel_onfocus == "true" ? true : false);
        return "redirect:/main.html";
    }
}
