package com.yupi.yupao.config;

import org.springframework.stereotype.Component;

import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

@Component
public class HttpSessionConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        // 取出 http 里的 session
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        if (httpSession != null) {
            // 放到 ServerEndpointConfig 里面
            sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
        }
        super.modifyHandshake(sec, request, response);
    }

}
