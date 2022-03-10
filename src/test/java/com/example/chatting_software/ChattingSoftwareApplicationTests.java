package com.example.chatting_software;

import com.example.chatting_software.tool.UserInfo;
import com.example.chatting_software.tool.UserSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChattingSoftwareApplicationTests {
    @Autowired
    DataSource dataSource;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() throws SQLException {
        //查看数据源
        System.out.println(dataSource.getClass());
        //获得数据库连接
     //   Connection connection = dataSource.getConnection();
    //    System.out.println(connection);
        //UserInfo.AddUserName("123", "yyx");
        System.out.println(UserInfo.GetUserName("1"));
        UserInfo.ChangeUserName("123", "杨宇翔");
        String sql = "select * from user_info";
        List<Map<String, Object>> list_maps = jdbcTemplate.queryForList(sql);
        for(Map<String, Object> a : list_maps) {
            System.out.println(a.keySet());
            System.out.println(a.values());
        }
        System.out.println(UserSet.getAllUser());
        System.out.println(UserSet.getAllUserWithName());
    //    connection.close();
    }


}
