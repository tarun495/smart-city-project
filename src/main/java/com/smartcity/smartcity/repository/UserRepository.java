package com.smartcity.smartcity.repository;

import com.smartcity.smartcity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    // Load user WITH roles in one query
    @Query("SELECT u FROM User u " +
           "LEFT JOIN FETCH u.roles " +
           "WHERE u.username = :username")
    Optional<User> findByUsername(
        @Param("username") String username);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}