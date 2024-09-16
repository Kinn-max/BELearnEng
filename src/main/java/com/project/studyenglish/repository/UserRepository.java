package com.project.studyenglish.repository;

import com.project.studyenglish.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phone);
    Optional<UserEntity> findByEmail(String email);;
}
