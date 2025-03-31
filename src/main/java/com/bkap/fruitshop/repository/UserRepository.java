package com.bkap.fruitshop.repository;

import com.bkap.fruitshop.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByRoles_Id(Long roleId);
    Page<User> findAll(Pageable pageable);
    Page<User> findUsersByUsernameContainingIgnoreCase(String keyword, Pageable pageable);
    Optional<User> findByVerificationToken(String verificationToken);
}