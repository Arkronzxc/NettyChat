package com.github.arkronzxc.chat.command;

import com.github.arkronzxc.chat.server.session.Session;

public interface Commander {

    void register(CommandHandler commandHandler);

    void execute(Session session, CommandRequest cmd);

    boolean contains(String cmdType);
}
