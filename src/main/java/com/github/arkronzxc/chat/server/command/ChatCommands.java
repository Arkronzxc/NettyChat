package com.github.arkronzxc.chat.server.command;

import com.github.arkronzxc.chat.command.CommandHandler;
import com.github.arkronzxc.chat.server.Router;
import com.github.arkronzxc.chat.server.chat.Chat;
import com.github.arkronzxc.chat.server.chat.Entry;
import com.github.arkronzxc.chat.server.session.Session;
import com.github.arkronzxc.chat.server.session.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;


public class ChatCommands implements CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(Router.class);

    public static String NO_TOPIC_ASSIGNED = "No session topic!";
    public static String JOIN_COMMAND = "join";
    public static String LEAVE_COMMAND = "leave";
    public static String USERS_COMMAND = "users";
    public static String PUBLISH_COMMAND = "publish";

    private final Chat broker;
    private final SessionRepository repository;

    public ChatCommands(Chat broker, SessionRepository repository) {
        this.broker = broker;
        this.repository = repository;
    }

    public Map<String, BiConsumer<Session, String[]>> handlers() throws RuntimeException {
        Map<String, BiConsumer<Session, String[]>> handlers = new HashMap<>();
        handlers.put(JOIN_COMMAND, this::join);
        handlers.put(LEAVE_COMMAND, this::leave);
        handlers.put(USERS_COMMAND, this::users);
        handlers.put(PUBLISH_COMMAND, this::publish);

        return handlers;
    }

    private void join(Session session, String[] arguments) {
        if (arguments.length != 1) {
            throw new RuntimeException("Unexpected command arguments size");
        }
        String topic = arguments[0];
        logger.info("Join ", topic);

        try {
            broker.subscribe(topic, session.getUserName());
        } catch (Exception e) {
            session.send(String.format("Rejected: %s", e.getMessage()));
            return;
        }

        session.setCurrentTopic(topic);

        session.send(String.format("Joined room %s", topic));
        for (Entry msg : broker.getHistory(topic)) {
            session.send(String.format("[%s]: [%s] || at %s", session.getUserName(), msg.getMessage(), msg.getTime().format(DateTimeFormatter.ofPattern("HH:mm"))));
        }
    }

    private void leave(Session session, String[] arguments) {
        logger.info("Leave ");

        if (!session.hasTopic()) {
            return;
        }

        broker.unsubscribe(session.getCurrentTopic(), session.getUserName());
        session.cleanTopic();
        session.send("Bye Bye!");

        session.terminate();
    }

    private void users(Session session, String[] arguments) {
        logger.info("Users");

        if (!session.hasTopic()) {
            session.send(NO_TOPIC_ASSIGNED);
            return;
        }

        session.send(String.format("Users on topic %s", session.getCurrentTopic()));
        for (String userName : broker.getSubscribers(session.getCurrentTopic())) {
            session.send(String.format(" -%s", userName));
        }
    }

    private void publish(Session session, String[] arguments) {
        if (arguments.length != 1) {
            throw new RuntimeException("Unexpected command arguments size");
        }

        if (!session.hasTopic()) {
            session.send(NO_TOPIC_ASSIGNED);
            return;
        }

        String message = arguments[0];
        for (String userName : broker.getSubscribers(session.getCurrentTopic())) {
            if (userName.equals(session.getUserName())) {
                continue;
            }
            send(userName, message);
        }
        broker.addToHistory(session.getCurrentTopic(), message);
    }

    private void send(String userName, String msg) {
        Session session = repository.get(userName);
        if (session == null) {
            logger.error("Session not found");
            return;
        }
        String out = String.format("[%s]: %s\n", userName, msg);
        session.send(out);
    }

}
