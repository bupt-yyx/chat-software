package com.example.chatting_software.controller;

import com.example.chatting_software.dao.ChattingContentDao;
import com.example.chatting_software.data.ChattingContent;
import com.example.chatting_software.tool.MyWebsocket;
import com.example.chatting_software.tool.UserSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpSession;
import java.util.Collection;

@Controller
public class ChattingContentController {
    @RequestMapping("/chats")
    public String list(HttpSession session, @RequestParam String now_user, RedirectAttributesModelMap model){
        Integer channel = UserSet.getChannel((String) session.getAttribute("loginUser"));
        Collection<ChattingContent> chattingContents = ChattingContentDao.getAll(channel);
        model.addFlashAttribute("now_user",now_user);
        model.addFlashAttribute("now_user_name",UserSet.getName(now_user));
        model.addFlashAttribute("contents",chattingContents);
        model.addFlashAttribute("all_users", UserSet.getAllUser());
        model.addFlashAttribute("all_users_with_name", UserSet.getAllUserWithName());
        model.addFlashAttribute("all_online_users", UserSet.getAllOnlineUser());
        return "redirect:/main.html";
    }

    @RequestMapping("/info-sending")
    public String info_handing(HttpSession session ,@RequestParam("chattingContent") String content, RedirectAttributesModelMap model, ChattingContent chattingContent){
        Integer channel = UserSet.getChannel((String) session.getAttribute("loginUser"));
        if(content.isEmpty()){
            model.addFlashAttribute("msg","输入的信息不能为空");
            Collection<ChattingContent> chattingContents = ChattingContentDao.getAll(channel);
            model.addFlashAttribute("contents",chattingContents);
            model.addFlashAttribute("all_users", UserSet.getAllUser());
            model.addFlashAttribute("all_users_with_name", UserSet.getAllUserWithName());
            model.addFlashAttribute("all_online_users", UserSet.getAllOnlineUser());
            return "redirect:/main.html";
        }else{
            chattingContent.setSender((String) session.getAttribute("loginUser"));
            ChattingContentDao.ContentAdd(channel, chattingContent);
            Collection<ChattingContent> chattingContents = ChattingContentDao.getAll(channel);
            model.addFlashAttribute("contents",chattingContents);
            model.addFlashAttribute("all_users", UserSet.getAllUser());
            model.addFlashAttribute("all_users_with_name", UserSet.getAllUserWithName());
            model.addFlashAttribute("all_online_users", UserSet.getAllOnlineUser());
            MyWebsocket.broadcast("send message");
            return "redirect:/main.html";
        }
    }

    @RequestMapping("/change_user_name")
    public String username_change(){return "ChangeUserName.html";}

}
