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

    @GetMapping("/between/{user1Id}/{user2Id}")
    public ResponseEntity<?> getChatBetweenUsers(@PathVariable int user1Id, @PathVariable int user2Id) {
        Optional<Chat> chat = chatRepository.findChatBetweenUsers(user1Id, user2Id);
        if (chat.isEmpty()) {
            return ResponseEntity.status(404).body("Không tìm thấy đoạn chat giữa user ID = " + user1Id + " và user ID = " + user2Id);
        }
        return ResponseEntity.ok(chat.get());
    }

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

    @PostMapping("/create_with_first_message")
    public ResponseEntity<?> createChatWithFirstMessage(@RequestBody Map<String, String> body) {
        Integer senderId = body.get("senderId") != null ? Integer.parseInt(body.get("senderId")) : null;
        Integer receiverId = body.get("receiverId") != null ? Integer.parseInt(body.get("receiverId")) : null;
        String firstMessage = body.get("message");

        if (senderId == null || receiverId == null) {
            return ResponseEntity.badRequest().body("Thiếu senderId hoặc receiverId trong yêu cầu");
        }

        if (senderId.equals(receiverId)) {
            return ResponseEntity.badRequest().body("senderId và receiverId không thể giống nhau");
        }

        if (!userRepository.existsById(senderId) || !userRepository.existsById(receiverId)) {
            return ResponseEntity.status(404).body("Một hoặc cả hai user không tồn tại");
        }

        Optional<Chat> existingChat = chatRepository.findChatBetweenUsers(senderId, receiverId);
        if (existingChat.isPresent()) {
            return ResponseEntity.badRequest().body("Đoạn chat giữa user ID = " + senderId + " và user ID = " + receiverId + " đã tồn tại");
        }

        if (firstMessage == null || firstMessage.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Tin nhắn không được để trống");
        }

        User sender = userRepository.findById(senderId).get();
        User receiver = userRepository.findById(receiverId).get();

        Chat newChat = new Chat();
        newChat.setUser1(sender);
        newChat.setUser1Id(senderId);
        newChat.setUser2(receiver);
        newChat.setUser2Id(receiverId);
        Chat savedChat = chatRepository.save(newChat);

        Message message = new Message();
        message.setChat(newChat);
        message.setSender(sender);
        message.setMessageText(firstMessage);
        message.setIsRead(false);
        messageRepository.save(message);

        return ResponseEntity.status(201).body(savedChat);
    }
}
