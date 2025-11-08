package com.example.demo.config.seeder;

import com.example.demo.entity.Chat;
import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import com.example.demo.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MessageSeeder {

    @Autowired
    private MessageRepository messageRepository;

    public Message createMessage(Chat chat, User sender, String messageText,
                              String attachmentUrl, boolean isRead, LocalDateTime readAt) {

        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setMessageText(messageText);
        message.setAttachmentUrl(attachmentUrl);
        message.setIsRead(isRead);
        message.setReadAt(readAt);

        messageRepository.save(message);
        return message;
    }
}
