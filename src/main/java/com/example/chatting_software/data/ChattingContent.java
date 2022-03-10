package com.example.chatting_software.data;

import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChattingContent {
    private String sender;
    private Date content_date;
    private String chatting_content;


    public ChattingContent(String chatting_content) {
        this.chatting_content = chatting_content;
        content_date = new Date();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getContent_date() {
        return content_date;
    }

    public void setContent_date(Date content_date) {
        this.content_date = content_date;
    }

    public String getChatting_content() {
        return chatting_content;
    }

    public void setChatting_content(String chatting_content) {
        this.chatting_content = chatting_content;
    }
}
