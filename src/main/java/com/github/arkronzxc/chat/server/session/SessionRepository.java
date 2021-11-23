package com.github.arkronzxc.chat.server.session;

public interface SessionRepository {

    void add(Session session);

    Session get(String userName);

    void remove(String userName);

    Boolean contains(String userName);
}
