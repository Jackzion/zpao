package com.ziio.zpao.wc;

import com.google.gson.Gson;
import com.ziio.zpao.config.HttpSessionConfigurator;
import com.ziio.zpao.model.vo.MessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@ServerEndpoint(value = "/websocket/{userId}",configurator = HttpSessionConfigurator.class)
public class WebSocket {

    // 线程安全性 ： 因为endpoint只有一个，多线程会导致修改数据不一致
    private static ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) throws IOException {
        sessions.put(userId, session);
        System.out.println(userId);
        MessageVo messageVo = new MessageVo();
        messageVo.setText("joined the chat");
        messageVo.setUserID(userId);
        // 转为json格式进行传输
        String toJson = new Gson().toJson(messageVo);
        broadcastMessage(toJson);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("userId") String userId) throws IOException {
        System.out.println(userId);
        System.out.println(message);
        // 封装 MessageVo 广播出去
        // 转为json格式进行传输
        String toJson = new Gson().toJson(message);
        System.out.println(toJson);
        broadcastMessage(toJson);
    }

    @OnClose
    public void onClose(Session session, @PathParam("userId") String userId) throws IOException {
        System.out.println(userId);
        MessageVo messageVo = new MessageVo();
        sessions.remove(userId);
        messageVo.setUserID(userId);
        messageVo.setUserID("leave ... ");
        // 转为json格式进行传输
        String toJson = new Gson().toJson(messageVo);
        broadcastMessage(toJson);
    }

    /**
     * 对 Session 列表进行广播 send message
     * @param message
     * @throws IOException
     * @throws IOException
     */
    private void broadcastMessage(String message) throws IOException, IOException {
        // session 可以看成websocket容器user的标识
        // 给每个session发送最新的信息
        for (Session session : sessions.values()) {
            if (session.isOpen()) {
                session.getBasicRemote().sendText(message);
            }
        }
    }
}
