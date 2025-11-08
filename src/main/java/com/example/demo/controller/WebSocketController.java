package com.example.demo.controller;

import com.example.demo.entity.Chat;
import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import com.example.demo.repository.ChatRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @MessageMapping("/chats/{chatId}")
    public void sendMessage(@DestinationVariable int chatId, @Valid @Payload Message incomingMessage) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new NoSuchElementException("Không tìm thấy đoạn chat với ID: " + chatId));
        User sender = userRepository.findById(incomingMessage.getSenderId()).orElseThrow(() -> new NoSuchElementException("Không tìm thấy người gửi với ID: " + incomingMessage.getSenderId()));

        incomingMessage.setChat(chat);
        incomingMessage.setSender(sender);

        Message saved = messageRepository.save(incomingMessage);
        messagingTemplate.convertAndSend("/topic/chats/" + chatId, saved);
    }
}
