package com.example.chatting_software.tool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserInfo {
    static JdbcTemplate jdbcTemplate;
    @Autowired
    public void init(JdbcTemplate jdbcTemplate){
        UserInfo.jdbcTemplate = jdbcTemplate;
    }

    public static String GetUserName(String user_id) {
        try {
            String sql = "select user_name from user_info where user_id = ?";
            String result = (String) jdbcTemplate.queryForMap(sql, new Object[]{user_id}).get("user_name");
            return result;
        } catch (Exception exception) {
            return null;
        }
    }

    public static void AddUserName(String user_id, String new_user_name) {
        String sql = "insert into user_info value(?,?)";
        jdbcTemplate.update(sql, new Object[]{user_id, new_user_name});
    }

    public static void ChangeUserName(String user_id, String new_user_name) {
        String sql = "update user_info set user_name = ? where user_id = ?";
        jdbcTemplate.update(sql, new Object[]{new_user_name, user_id});
    }
}
