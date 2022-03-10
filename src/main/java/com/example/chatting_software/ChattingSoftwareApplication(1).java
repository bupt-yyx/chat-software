package com.example.chatting_software;

import com.example.chatting_software.tool.UserSet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChattingSoftwareApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChattingSoftwareApplication.class, args);
    }

}
