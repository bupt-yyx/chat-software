package com.example.chatting_software.controller;

import com.example.chatting_software.tool.GetSalts;
import com.example.chatting_software.tool.UserSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class RegisterController {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping("/user/register")
    public String register(@RequestParam("username") String username, @RequestParam("password") String password,
                           @RequestParam("repeat") String repeat, RedirectAttributesModelMap model, HttpSession session) {
        try {
            String salt1, salt2;
            List<String> salts = GetSalts.getSalts();
            salt1 = salts.get(0);
            salt2 = salts.get(1);
            System.out.println(repeat);
            if (checkExisted(username)) {
                model.addFlashAttribute("password_message", "用户名已存在");
                return "redirect:/register.html";
            }
            else if(!password.equals(repeat)){
                model.addFlashAttribute("password_message", "密码不一致，请重新输入");
                return "redirect:/register.html";
            }
            else {
                addUser(username, GetSalts.getSaltedPassword(password));
                model.addFlashAttribute("register_message", "注册成功");
                UserSet.update();
                return "redirect:/register.html";
            }
        } catch (Exception exception) {
            model.addFlashAttribute("register_message", "发生错误:" + exception.getMessage());
            return "redirect:/register.html";
        }
    }

    private void addUser(String username, String md5Password) {
        String sql = "insert into user value(?,?)";
        jdbcTemplate.update(sql, new Object[]{username, md5Password});
    }

    private boolean checkExisted(String username) {
        String sql = "select COUNT(user_id) as num from user where user_id=?";
        Object qwq = jdbcTemplate.queryForMap(sql, new Object[]{username}).get("num");
        return (boolean) (((long)qwq) > 0);
    }
}
