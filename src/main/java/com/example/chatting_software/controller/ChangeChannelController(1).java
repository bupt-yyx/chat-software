package com.example.chatting_software.controller;

import com.example.chatting_software.tool.MyWebsocket;
import com.example.chatting_software.tool.UserSet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpSession;


@Controller
    public class ChangeChannelController {
    @RequestMapping("/channel_changing")
    public String changeChannel(HttpSession session, @RequestParam("channelNo") String channelNo, RedirectAttributesModelMap model){
        if(channelNo==""){
            model.addFlashAttribute("WrongChannel","WrongChannel");
            return "redirect:/main.html";
        }else{
        String user_id =(String) session.getAttribute("loginUser");
        UserSet.setChannel(user_id,Integer.valueOf(channelNo).intValue());
        model.addFlashAttribute("users_in_same_channel",UserSet.getAllUserInSameChannel(Integer.valueOf(channelNo).intValue()));
        MyWebsocket.broadcast("join");
        return "redirect:/main.html";
        }
    }
}
