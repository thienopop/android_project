package com.example.demo.repository;

import com.example.demo.entity.entity_design.ChatWithUserDetail;
import com.example.demo.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    @Query("SELECT new com.example.demo.entity.entity_design.ChatWithUserDetail(" +
            "c.id, " +
            "CASE WHEN c.user1.id = :userId THEN c.user2.id ELSE c.user1.id END, " +
            "CASE WHEN c.user1.id = :userId THEN c.user2.username ELSE c.user1.username END, " +
            "CASE WHEN c.user1.id = :userId THEN c.user2.email ELSE c.user1.email END, " +
            "CASE WHEN c.user1.id = :userId THEN c.user2.role ELSE c.user1.role END, " +
            "COALESCE(s.fullName, t.fullName), " +
            "(SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END " +
            " FROM Message m WHERE m.chat = c AND m.sender.id <> :userId AND m.isRead = false) " +
            ") " +
            "FROM Chat c " +
            "LEFT JOIN Student s ON ((c.user1.id = :userId AND s.user.id = c.user2.id) " +
            "                      OR (c.user2.id = :userId AND s.user.id = c.user1.id)) " +
            "LEFT JOIN Tutor t ON ((c.user1.id = :userId AND t.user.id = c.user2.id) " +
            "                    OR (c.user2.id = :userId AND t.user.id = c.user1.id)) " +
            "WHERE c.user1.id = :userId OR c.user2.id = :userId " +
            "ORDER BY (SELECT MAX(m.createdAt) FROM Message m WHERE m.chat = c) DESC")
    List<ChatWithUserDetail> findChatsWithUserDetails(@Param("userId") Integer userId);

    @Query("""
            SELECT c FROM Chat c WHERE
            (c.user1.id = :user1Id AND c.user2.id = :user2Id) OR 
            (c.user1.id = :user2Id AND c.user2.id = :user1Id)
            """)
    Optional<Chat> findChatBetweenUsers(@Param("user1Id") Integer user1Id, @Param("user2Id") Integer user2Id);
}
