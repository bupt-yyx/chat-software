package com.example.chatting_software.dao;

import com.example.chatting_software.data.ChattingContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.*;

@Component
public class ChattingContentDao {
    private static Map<Integer, Map<Integer, ChattingContent>> chattingContents = null;
    private static Map<Integer, Integer> initid;
    @Autowired
    public void init(){
        initid = new HashMap<Integer, Integer>();
        chattingContents = new HashMap<Integer, Map<Integer, ChattingContent>>();
    }


    public static Collection<ChattingContent> getAll(Integer channel){
        if(!chattingContents.containsKey(channel)) {
            chattingContents.put(channel, new HashMap<Integer, ChattingContent>());
        }
        return chattingContents.get(channel).values();
    }

    /*public ChattingContent getChattingContentByReceiver(String receiver){
        return chattingContents.get(receiver);
    }

    public ChattingContent getChattingContentBySender(String sender){
        return chattingContents.get(sender);
    }*/

    public static void ContentAdd(Integer channel,ChattingContent chattingContent){
        if(!chattingContents.containsKey(channel)) {
            chattingContents.put(channel, new HashMap<Integer, ChattingContent>());
        }
        if(!initid.containsKey(channel)) {
            initid.put(channel, 0);
        }
        chattingContents.get(channel).put(initid.get(channel),chattingContent);
        initid.put(channel, initid.get(channel) + 1);
    }


}
