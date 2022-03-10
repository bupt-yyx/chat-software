package com.example.chatting_software.controller;

import com.example.chatting_software.tool.FixInterval;
import com.example.chatting_software.tool.GetSalts;
import com.example.chatting_software.tool.UserSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping("/user/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password,
                        RedirectAttributesModelMap model, HttpSession session) {
        try {
            String md5Password = GetSalts.getSaltedPassword(password);
            if (!username.isEmpty() && checkPassword(username, md5Password)) {
                session.setAttribute("loginUser", username);
                UserSet.setChannel(username, 0);
                session.setAttribute("last_input_onfocus", false);
                session.setAttribute("last_select_channel_onfocus",false);
               // FixInterval.fixUpdate();
                return "redirect:/main.html";
            }
            model.addFlashAttribute("password_message", "用户名或者密码错误");
            return "redirect:/index.html";
        } catch (Exception exception) {
            model.addFlashAttribute("password_message", "发生错误:" + exception.getMessage());
            return "redirect:/index.html";
        }
    }

    private boolean checkPassword(String userId, String md5Password) {
        String sql = "select user_id, user_password from user where user_id= ?";
        System.out.println(sql);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, new Object[]{userId});
        if(result.isEmpty()) {
            return false;
        }
        System.out.println(result);
        return result.get(0).get("user_password").equals(md5Password);
    }
}
