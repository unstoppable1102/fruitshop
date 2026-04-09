package com.bkap.fruitshop.repository;

import com.bkap.fruitshop.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // ✅ Sửa trong Repository — fetch luôn users và roles trong 1 query
    @Query("SELECT DISTINCT r FROM Role r LEFT JOIN FETCH r.users u LEFT JOIN FETCH u.roles")
    List<Role> findAllWithUsers();

    Optional<Role> findByName(String name);

    boolean existsByName(String name);
}