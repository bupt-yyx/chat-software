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
            log.info("????????????????????????");
            map.get(connectedUsersMap.get(this.loginUser).channel).remove(connectedUsersMap.get(this.loginUser).session);
            connectedUsersMap.remove(this.loginUser);
        } else {
            clients.add(this);
        }
        connectedUsersMap.put(this.loginUser, new SessionInfo(channel, session));
        map.get(channel).add(session);

        log.info("??????????????????,??????????????????{}", clients.size());
        //this.session.getAsyncRemote().sendText(nickname + "???????????????(??????????????????" + channel + "),????????????????????????" + clients.size());
    }

    @OnClose
    public void onClose() {
        clients.remove(this);
        connectedUsersMap.remove(this.loginUser);
        log.info("?????????????????????,??????????????????{}", clients.size());
        if (map.containsKey(this.channel)) {
            map.get(this.channel).remove(this.session);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("nickname") String nickname, @PathParam("channel") String channel, @PathParam("user_id") String user_id) {
        log.info("??????????????????{}??????????????????{}", nickname, message);
        System.out.println("???????????????" + nickname + "???????????????");
        System.out.println(nickname);
        System.out.println(channel);
        System.out.println(user_id);
        SocketConfig socketConfig;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            socketConfig = objectMapper.readValue(message, SocketConfig.class);
            this.channel = socketConfig.getChannel();
            if (socketConfig.getType() == 1) {  //??????
                socketConfig.setFromUser(session.getId());
                Session fromSession = null;
                for (Session s : map.get(channel)) {
                    if (s.getId() == session.getId()) {
                        fromSession = s;
                        break;
                    }
                }
                List<Session> toSession = map.get(channel);

                if (toSession != null) {  //???????????????,??????????????????????????????????????????
                    for (Session s : toSession) {
                        s.getAsyncRemote().sendText(nickname + "???" + socketConfig.getMsg());
                    }
                } else {  //??????????????????,??????????????????????????????
                    assert fromSession != null;
                    fromSession.getAsyncRemote().sendText("????????????????????????????????????");
                }
            } else {  //??????
                broadcast(nickname + "???" + socketConfig.getMsg());
            }
        } catch (Exception e) {
            log.error("??????????????????");
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("????????????");
        error.printStackTrace();
    }

    /**
     * ?????????????????????
     *
     * @param message
     */
    public static void broadcast(String message) {
        for (MyWebsocket websocket : clients) {
            //??????????????????
            //websocket.session.getAsyncRemote().sendText(message);
            try {
                websocket.session.getBasicRemote().sendText(message);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }

        }
    }
}

