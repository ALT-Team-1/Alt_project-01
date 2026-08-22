package com.alt.project.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table
@Builder


public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false ,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    public void updateNickname(String nickname) {
        if (nickname != null && !nickname.isBlank()) {
            this.nickname = nickname;
        }
    }

    public void updatePassword(String encodedPassword) {
        if (encodedPassword != null && !encodedPassword.isBlank()) {
            this.password = encodedPassword;
        }
    }
}
