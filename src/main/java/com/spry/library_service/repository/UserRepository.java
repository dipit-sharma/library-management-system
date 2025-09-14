package com.spry.library_service.repository;

import com.spry.library_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmailAndDeletedFalse(String email);
    
    boolean existsByEmailAndDeletedFalse(String email);
}
