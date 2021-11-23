package com.github.arkronzxc.chat.server.session;

import io.netty.channel.ChannelHandlerContext;

public class Session {
    private final ChannelHandlerContext ctx;
    private final String userName;
    private volatile String currentTopic;

    public Session(ChannelHandlerContext ctx, String userName) {
        this.ctx = ctx;
        this.userName = userName;
    }

    public void send(String msg) {
        ctx.writeAndFlush(msg);
    }

    public void terminate() {
        ctx.close();
    }

    public String getUserName() {
        return userName;
    }

    public String getCurrentTopic() {
        return currentTopic;
    }

    public void setCurrentTopic(String currentTopic) {
        this.currentTopic = currentTopic;
    }

    public void cleanTopic() {
        currentTopic = null;
    }

    public Boolean hasTopic() {
        return currentTopic != null;
    }

}
