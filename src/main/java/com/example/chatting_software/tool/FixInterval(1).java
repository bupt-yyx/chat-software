package com.example.chatting_software.tool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Configuration
@EnableScheduling
public class FixInterval {

   /* @Scheduled(cron = "0/1 * * * * ?")
    public void fixUpdate() {
        System.out.println("FIX UPDATE");
    }*/
}
