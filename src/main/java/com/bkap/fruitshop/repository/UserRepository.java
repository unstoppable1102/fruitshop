package com.bkap.fruitshop.repository;

import com.bkap.fruitshop.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByRoles_Id(Long roleId);
    Page<User> findUsersByUsernameContainingIgnoreCase(String keyword, Pageable pageable);
    Optional<User> findByVerificationToken(String verificationToken);

    // ✅ Sửa trong Repository — fetch luôn roles trong 1 query
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE (:keyword IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<User> findUsersWithRoles(@Param("keyword") String keyword, Pageable pageable);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findById(Long id);
}