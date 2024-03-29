package org.example.hibernate.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.hibernate.listener.UserChatListener;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.Instant;


@Data
@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name = "users_chat")
@EqualsAndHashCode(callSuper = false)
@EntityListeners(UserChatListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserChat extends AuditableEntity<Long> {

    @Builder
    public UserChat(
            Long id,
            User user,
            Chat chat,
            Instant createdAt,
            String createdBy,
            Instant updatedAt,
            String updatedBy
    ) {
        super(createdAt, createdBy, updatedAt, updatedBy);
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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Chat chat;
}
