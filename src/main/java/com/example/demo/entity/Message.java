package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    @JsonIgnore
    private Chat chat;

    @Column(name = "chat_id", insertable = false, updatable = false)
    private Integer chatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonIgnore
    private User sender;

    @Column(name = "sender_id", insertable = false, updatable = false)
    private Integer senderId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String messageText;

    @Column(length = 500)
    private String attachmentUrl;

    @Column(nullable = false)
    private Boolean isRead = false;

    private LocalDateTime readAt;
}
