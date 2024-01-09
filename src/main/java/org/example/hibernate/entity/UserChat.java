package org.example.hibernate.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Data
@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name = "users_chat")
@EqualsAndHashCode(callSuper = false)
public class UserChat extends AuditableEntity<Long> {

    @Builder
    public UserChat(Long id, User user, Chat chat, Instant createdAt, String createdBy) {
        super(createdAt, createdBy);
        this.id = id;
        this.user = user;
        this.chat = chat;

        if (user != null) {
            user.getUserChats().add(this);
        }
        if (chat != null) {
            chat.getUserChats().add(this);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Chat chat;
}
