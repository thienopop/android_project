package com.example.demo.controller;

import com.example.demo.entity.entity_design.ChatWithUserDetail;
import com.example.demo.entity.Chat;
import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import com.example.demo.repository.ChatRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatController {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<?> getChatMessages(@PathVariable int chatId) {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isEmpty()) {
            return ResponseEntity.status(404).body("Không tìm thấy đoạn chat với ID = " + chatId);
        }
        List<Message> messages = messageRepository.findByChatIdOrderByCreatedAtAsc(chatId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/my_chats")
    public ResponseEntity<?> getMyChats() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username).orElse(null);

        if (currentUser == null) {
            return ResponseEntity.status(404).body("Không tìm thấy đoạn chat cho user: " + username);
        }

        List<ChatWithUserDetail> result = chatRepository.findChatsWithUserDetails(currentUser.getId());
        return ResponseEntity.ok(result);
    }
}
