package com.github.arkronzxc.chat.command;

import com.github.arkronzxc.chat.server.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class CommandExecutor implements Commander {
    private static final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);

    private final Map<String, BiConsumer<Session, String[]>> commands = new HashMap<>();

    public CommandExecutor() {
    }

    @Override
    public void register(CommandHandler commandHandler) {
        commands.putAll(commandHandler.handlers());
    }

    @Override
    public void execute(Session session, CommandRequest cmd) {
        if (!commands.containsKey(cmd.getCmd())) {
            logger.error(String.format("Command %s Not found", cmd.getCmd()));
            return;
        }

        commands.get(cmd.getCmd()).accept(session, cmd.getArguments());
    }

    public boolean contains(String cmdType) {
        return commands.containsKey(cmdType);
    }
}

