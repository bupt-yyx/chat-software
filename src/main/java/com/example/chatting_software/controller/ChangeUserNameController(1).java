package com.example.chatting_software.controller;

import com.example.chatting_software.tool.MyWebsocket;
import com.example.chatting_software.tool.UserInfo;
import com.example.chatting_software.tool.UserSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpSession;

@Controller
public class ChangeUserNameController {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @RequestMapping("/user/changeName")
    public String ChangeName(@RequestParam("username_change") String username_change, RedirectAttributesModelMap model,  HttpSession httpSession){
        String loginUser = (String) httpSession.getAttribute("loginUser");
        System.out.println(loginUser);
        try {
            if(UserInfo.GetUserName(loginUser) == null) {
                UserInfo.AddUserName(loginUser, username_change);
            } else {
                UserInfo.ChangeUserName(loginUser, username_change);
            }
            model.addFlashAttribute("change_name_message", "修改成功");
            UserSet.update();
            MyWebsocket.broadcast("sb. rename");
            return "redirect:/ChangeUserName.html";
        } catch (Exception exception) {
            model.addFlashAttribute("change_name_message", "修改失败:" + exception.getMessage());
            return "redirect:/ChangeUserName.html";
        }
    }
}
