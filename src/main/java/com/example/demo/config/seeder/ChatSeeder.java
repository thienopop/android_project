package com.example.demo.config.seeder;

import com.example.demo.entity.Chat;
import com.example.demo.entity.User;
import com.example.demo.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatSeeder {

    @Autowired
    private ChatRepository chatRepository;

    public Chat createChat(User user1, User user2) {
        Chat chat = new Chat();
        chat.setUser1(user1);
        chat.setUser2(user2);

        chatRepository.save(chat);
        return chat;
    }
}
