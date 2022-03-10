package com.example.chatting_software.tool;

import lombok.Data;


@Data
public class SocketConfig {
    //聊天类型 0：群聊  1：私聊
    private int type;
    //发送者
    private String fromUser;
    //接受者
    private String channel;
    //消息
    private String msg;

    private String user_id;
}

