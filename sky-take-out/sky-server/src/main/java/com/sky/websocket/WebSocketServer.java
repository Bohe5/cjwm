package com.sky.websocket;

import org.springframework.stereotype.Component;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket服务
 */
@Component
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {

    //存放会话对象
    private static Map<String, Session> sessionMap = new HashMap();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        // 打印连接日志，便于排查连接状态
        System.out.println("客户端：" + sid + "建立连接");
        // 将当前客户端的会话对象存入集合，后续通过 sid 可定位到具体客户端
        sessionMap.put(sid, session);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        // 打印收到的消息日志，包含客户端标识和消息内容，用于调试和业务跟踪
        System.out.println("收到来自客户端：" + sid + "的信息:" + message);
    }

    /**
     * 连接关闭调用的方法
     *
     * @param sid
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        // 打印断开连接日志
        System.out.println("连接断开:" + sid);
        // 从会话集合中移除该客户端的会话对象，避免内存泄漏，同时确保后续无法向该客户端发送消息
        sessionMap.remove(sid);
    }

    /**
     * 群发
     *
     * @param message
     */
    public void sendToAllClient(String message) {
        // 获取所有已连接客户端的会话对象集合
        Collection<Session> sessions = sessionMap.values();
        // 遍历所有会话，向每个客户端发送消息
        for (Session session : sessions) {
            try {
                //服务器向客户端发送消息
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                // 捕获消息发送异常（如客户端已断开但未触发 onClose，导致发送失败），打印异常堆栈便于排查
                e.printStackTrace();
            }
        }
    }

}
