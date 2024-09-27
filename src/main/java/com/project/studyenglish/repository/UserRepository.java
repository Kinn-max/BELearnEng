package com.project.studyenglish.repository;

import com.project.studyenglish.models.RoleEntity;
import com.project.studyenglish.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
    Boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phone);
    Optional<UserEntity> findByEmail(String email);;
    List<UserEntity> findByRoleEntity(RoleEntity roleEntity);
    List<UserEntity> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String keyword);
    List<UserEntity> findByRoleEntityOrRoleEntity(RoleEntity roleEntity1, RoleEntity roleEntity2);

}
