package com.smartcity.smartcity.repository;

import com.smartcity.smartcity.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface SubscriptionRepository
        extends JpaRepository<Subscription, Long> {

    Optional<Subscription>
        findByUserUsernameAndServiceTypeAndIsActive(
            String username,
            String serviceType,
            boolean isActive);

    @Modifying
    @Query("DELETE FROM Subscription s " +
           "WHERE s.user.id = :userId")
    void deleteByUserId(
        @Param("userId") Long userId);
}