package com.example.demo.controller;

import com.example.demo.entity.Chat;
import com.example.demo.entity.Message;
import com.example.demo.repository.ChatRepository;
import com.example.demo.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatController {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<?> getChatMessages(@PathVariable int chatId) {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isEmpty()) {
            return ResponseEntity.status(404).body("Không tìm thấy đoạn chat với ID = " + chatId);
        }
        List<Message> messages = messageRepository.findByChatIdOrderByCreatedAtAsc(chatId);
        return ResponseEntity.ok(messages);
    }
}
