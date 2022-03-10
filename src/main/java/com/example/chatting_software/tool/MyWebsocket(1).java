package com.example.chatting_software.tool;

import com.example.chatting_software.comfig.GetHttpSessionConfigurator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;


class SessionInfo {
    public String channel;
    public Session session;

    public SessionInfo(String channel, Session session) {
        this.channel = channel;
        this.session = session;
    }
}

@ServerEndpoint(value = "/websocket/{nickname}/{channel}/{user_id}", configurator = GetHttpSessionConfigurator.class)
@Component
@Slf4j
public class MyWebsocket {
    /**
     * channel, list(session)
     */
    private static Map<String, CopyOnWriteArrayList<Session>> map = new ConcurrentHashMap<String, CopyOnWriteArrayList<Session>>();

    private static CopyOnWriteArraySet<MyWebsocket> clients = new CopyOnWriteArraySet<MyWebsocket>();

    private static Map<String, SessionInfo> connectedUsersMap = new ConcurrentHashMap<String, SessionInfo>();

    private Session session;

    private String nickname;

    private HttpSession httpSession;

    private String channel;

    private String loginUser;

    @OnOpen
    public void onOpen(Session session, @PathParam("nickname") String nickname, @PathParam("channel") String channel, EndpointConfig config) {
        this.session = session;
        this.nickname = nickname;
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.loginUser = (String) httpSession.getAttribute("loginUser");
        this.channel = channel;
        if (!map.containsKey(channel)) {
            map.put(channel, new CopyOnWriteArrayList<>());
        }
        if (connectedUsersMap.containsKey(this.loginUser)) {
            log.info("自动断开上个链接");
            map.get(connectedUsersMap.get(this.loginUser).channel).remove(connectedUsersMap.get(this.loginUser).session);
            connectedUsersMap.remove(this.loginUser);
        } else {
            clients.add(this);
        }
        connectedUsersMap.put(this.loginUser, new SessionInfo(channel, session));
        map.get(channel).add(session);

        log.info("有新用户加入,当前人数为：{}", clients.size());
        //this.session.getAsyncRemote().sendText(nickname + "已成功连接(其频道号为：" + channel + "),当前在线人数为：" + clients.size());
    }

    @OnClose
    public void onClose() {
        clients.remove(this);
        connectedUsersMap.remove(this.loginUser);
        log.info("有用户断开连接,当前人数为：{}", clients.size());
        if (map.containsKey(this.channel)) {
            map.get(this.channel).remove(this.session);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("nickname") String nickname, @PathParam("channel") String channel, @PathParam("user_id") String user_id) {
        log.info("来自客户端：{}发来的消息：{}", nickname, message);
        System.out.println("来自客户端" + nickname + "发来的消息");
        System.out.println(nickname);
        System.out.println(channel);
        System.out.println(user_id);
        SocketConfig socketConfig;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            socketConfig = objectMapper.readValue(message, SocketConfig.class);
            this.channel = socketConfig.getChannel();
            if (socketConfig.getType() == 1) {  //私聊
                socketConfig.setFromUser(session.getId());
                Session fromSession = null;
                for (Session s : map.get(channel)) {
                    if (s.getId() == session.getId()) {
                        fromSession = s;
                        break;
                    }
                }
                List<Session> toSession = map.get(channel);

                if (toSession != null) {  //接受者存在,发送以下消息给接受者和发送者
                    for (Session s : toSession) {
                        s.getAsyncRemote().sendText(nickname + "：" + socketConfig.getMsg());
                    }
                } else {  //发送者不存在,发送以下消息给发送者
                    assert fromSession != null;
                    fromSession.getAsyncRemote().sendText("频道号不存在或对方不在线");
                }
            } else {  //群聊
                broadcast(nickname + "：" + socketConfig.getMsg());
            }
        } catch (Exception e) {
            log.error("发送消息出错");
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("出现错误");
        error.printStackTrace();
    }

    /**
     * 自定义群发消息
     *
     * @param message
     */
    public static void broadcast(String message) {
        for (MyWebsocket websocket : clients) {
            //异步发送消息
            //websocket.session.getAsyncRemote().sendText(message);
            try {
                websocket.session.getBasicRemote().sendText(message);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }

        }
    }
}

