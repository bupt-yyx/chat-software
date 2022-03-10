package com.example.chatting_software.tool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class GetSalts {
    static JdbcTemplate jdbcTemplate;
    @Autowired
    public void init(JdbcTemplate jdbcTemplate){
        GetSalts.jdbcTemplate = jdbcTemplate;
    }
    public static List<String> getSalts() {
        ArrayList<String> returnValue = new ArrayList<String>();
        String salt1, salt2;
        try {
            List<Map<String, Object>> result = jdbcTemplate.queryForList("select content from salt");
            salt1 = (String) result.get(0).get("content");
            salt2 = (String) result.get(1).get("content");
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("can not get salt from database");
            salt1 = "BUPT535xxq";
            salt2 = "thhyj";
        }
        returnValue.add(salt1);
        returnValue.add(salt2);
        return returnValue;
    }

    public static String getSaltedPassword(String password) {
        String salt1, salt2;
        List<String> result = getSalts();
        salt1 = result.get(0);
        salt2 = result.get(1);
        return DigestUtils.md5DigestAsHex((salt1 + password + salt2).getBytes());
    }
}
