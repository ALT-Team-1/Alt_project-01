package com.alt.project.auth.repository;

import com.alt.project.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
