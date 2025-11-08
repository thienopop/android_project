package com.example.demo.controller;

import com.example.demo.entity.Chat;
import com.example.demo.entity.Message;
import com.example.demo.repository.ChatRepository;
import com.example.demo.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    @GetMapping("/{chatId}/messages")
    public List<Message> getChatMessages(@PathVariable int chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        return messageRepository.findAll().stream()
                .filter(m -> m.getChatId() == chat.getId())
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .toList();
    }
}
