# NettyChat IRC chat

## Commands:
  - login: if user does not exist create profile else login.
    -  /login name password
  - join: try to join chat room (max 10 active clients per chat room).
 If chat room does not exist - create it first. If client’s limit exceeded - send an error, otherwise join chat room and send last N messages of activity. Server should support many chat rooms.
    -  /join chat_room
  -  leave: disconnect client.
        - /leave
  - users — show users in the channel.
    -  /users
 - publish: text message terminated with CR - sends message to current channel.
 
 ## Usage
```
 telnet localhost 9999

 /login qwe

 /join zxc

 /publish hello

```
