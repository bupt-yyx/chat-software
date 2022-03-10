package com.example.chatting_software.tool;

import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class IdWithName {
    public String id;
    public String name;

    @Override
    public String toString() {
        return "IdWithName{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public IdWithName(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

class IdWithChannel {
    public String id;
    public int channel;

    public IdWithChannel(String id, int channel) {
        this.id = id;
        this.channel = channel;
    }
    public String getId() {
        return id;
    }

    public int getChannel() {
        return channel;
    }
}

class IdWithNameAndChannel {
    public String id;
    public String name;
    public int channel;

    public IdWithNameAndChannel(String id, String name, int channel) {
        this.id = id;
        this.name = name;
        this.channel = channel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }
}

@Component
public class UserSet {
    static JdbcTemplate jdbcTemplate;
    @Autowired
    public void init(JdbcTemplate jdbcTemplate){
        UserSet.jdbcTemplate = jdbcTemplate;
        initializer();
    }
    //记录在线用户的user_id
    public static SortedSet<String> onlineUserSet;
    //记录离线用户的user_id
    public static SortedSet<String> userSet;
    public static TreeMap<String, String> idToNameMap;
    public static ConcurrentHashMap<String, Integer> idToChannel;
    public static void initializer() {
        onlineUserSet = new TreeSet<String>();
        userSet = new TreeSet<String>();
        idToNameMap = new TreeMap<String, String>();
        idToChannel = new ConcurrentHashMap<String, Integer>();
        update();
    }
    @Synchronized
    public static void update(){
        try {
            String sql = "select user_id from user";
            List<Map<String, Object>> result =  jdbcTemplate.queryForList(sql);
            for(Map<String, Object> map:result) {
                userSet.add((String) map.get("user_id"));
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        try {
            String sql = "select user_id, user_name from user_info";
            List<Map<String, Object>> result =  jdbcTemplate.queryForList(sql);
            for(Map<String, Object> a : result) {
                idToNameMap.put((String) a.get("user_id"),(String) a.get("user_name"));
            }
            sql = "select user_id from user";
            result =  jdbcTemplate.queryForList(sql);
            for(Map<String, Object> a : result) {
                if(!idToNameMap.containsKey(a.get("user_id"))) {
                    idToNameMap.put((String) a.get("user_id"),(String) a.get("user_id"));
                }
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static String getFirst() {
        if(onlineUserSet.isEmpty()) {
            if(userSet.isEmpty()) {
                return null;
            } else {
                return userSet.first();
            }
        } else {
            return onlineUserSet.first();
        }
    }

    public static Collection<String> getAllOnlineUser() {
        return onlineUserSet;
    }
    public static Collection<String> getAllUser() {
        return userSet;
    }
    public static String getName(String user_id) {
        try {
            return idToNameMap.get(user_id);
        } catch (Exception exception) {
            return null;
        }
    }

    public static void setChannel(String user_id, int channel) {
        idToChannel.put(user_id, channel);
    }

    public static Integer getChannel(String user_id) {
        if(!idToChannel.containsKey(user_id)) {
            return 0;
        } else {
            return idToChannel.get(user_id);
        }
    }

    public static Collection<IdWithNameAndChannel> getAllUserWithName() {
        ArrayList<IdWithNameAndChannel> userNameSet = new ArrayList<IdWithNameAndChannel>();
        for(String id:userSet) {
            userNameSet.add(new IdWithNameAndChannel(id, getName(id), getChannel(id)));
        }
        return userNameSet;
    }

    public static Collection<IdWithNameAndChannel> getAllUserInSameChannel(int channelNo){
        ArrayList<IdWithNameAndChannel> userNameSet = new ArrayList<IdWithNameAndChannel>();
        for(String id:userSet){
            if(getChannel(id)==channelNo)
                userNameSet.add(new IdWithNameAndChannel(id, getName(id), getChannel(id)));
        }
        return userNameSet;
    }
}
