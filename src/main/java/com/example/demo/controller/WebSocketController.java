package com.example.demo.controller;

import com.example.demo.entity.Message;
import com.example.demo.repository.ChatRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @MessageMapping("/chat/{chatId}")
    public void sendMessage(@DestinationVariable int chatId, @Payload Message incomingMessage) {
        var chat = chatRepository.findById(chatId).orElseThrow();
        var sender = userRepository.findById(incomingMessage.getSenderId()).orElseThrow();

        incomingMessage.setChat(chat);
        incomingMessage.setSender(sender);

        Message saved = messageRepository.save(incomingMessage);
        messagingTemplate.convertAndSend("/topic/chat/" + chatId, saved);
    }
}
