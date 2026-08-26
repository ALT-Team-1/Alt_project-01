package com.alt.project.auth.repository;

import com.alt.project.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;import java.util.Optional;

@Repository

// 이메일 중복 여부 판단하여 반환
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}