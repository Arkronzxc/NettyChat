package com.github.arkronzxc.chat.command;

public class CommandRequest {
    private final String cmd;
    private final String[] arguments;

    public CommandRequest(String cmd, String[] arguments) {
        this.cmd = cmd;
        this.arguments = arguments;
    }

    public String getCmd() {
        return cmd;
    }

    public String[] getArguments() {
        return arguments;
    }
}
