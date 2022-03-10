package com.example.chatting_software.controller;

import com.example.chatting_software.dao.ChattingContentDao;
import com.example.chatting_software.data.ChattingContent;
import com.example.chatting_software.tool.UserSet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpSession;
import java.util.Collection;

@Controller
public class MainController {
    @RequestMapping("/main.html")
    public String init(HttpSession session,Model model){
        //model.addFlashAttribute("now_user", UserSet.getFirst());
        String loginUser = (String)session.getAttribute("loginUser");
        Integer channel = UserSet.getChannel(loginUser);
        System.out.println(channel);
        Collection<ChattingContent> chattingContents = ChattingContentDao.getAll(channel);
        model.addAttribute("users_in_same_channel",UserSet.getAllUserInSameChannel(channel));
        model.addAttribute("contents",chattingContents);
        model.addAttribute("channel", UserSet.getChannel(loginUser));
        model.addAttribute("login_id", loginUser);
        model.addAttribute("nickname", UserSet.getName(loginUser));
        model.addAttribute("all_users", UserSet.getAllUser());
        model.addAttribute("all_online_users", UserSet.getAllOnlineUser());
        model.addAttribute("all_users_with_name", UserSet.getAllUserWithName());
        model.addAttribute("id_to_name_map", UserSet.idToNameMap);
        //  if(session.getAttribute("last_input_onfocus") == null) {
        model.addAttribute("last_input_onfocus", session.getAttribute("last_input_onfocus"));
        //  }
        System.out.println("last_input_onfocus = " + model.getAttribute("last_input_onfocus") != null ? model.getAttribute("last_input_onfocus").toString() : "null");

        //  if(session.getAttribute("last_select_channel_onfocus") == null) {
        model.addAttribute("last_select_channel_onfocus", session.getAttribute("last_select_channel_onfocus"));
        //     }
        System.out.println("last_select_channel_onfocus = " + model.getAttribute("last_select_channel_onfocus") != null ? model.getAttribute("last_select_channel_onfocus").toString() : "null");

        return "main";
    }
}
