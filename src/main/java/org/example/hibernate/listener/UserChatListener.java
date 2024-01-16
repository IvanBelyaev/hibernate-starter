package org.example.hibernate.listener;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import org.example.hibernate.entity.UserChat;

public class UserChatListener {
    @PostPersist
    public void postPersist(UserChat userChat) {
        var chat = userChat.getChat();
        chat.setCount(chat.getCount() + 1);
    }

    @PostRemove
    public void postRemove(UserChat userChat) {
        var chat = userChat.getChat();
        chat.setCount(chat.getCount() - 1);
    }
}
