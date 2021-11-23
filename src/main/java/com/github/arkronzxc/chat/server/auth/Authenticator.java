package com.github.arkronzxc.chat.server.auth;

public interface Authenticator {

    boolean validateCredentials(String user, String pass);

}
