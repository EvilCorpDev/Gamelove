package org.duckdns.androidghost77.gamelove.repository;

import org.duckdns.androidghost77.gamelove.repository.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {

    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId")
    Optional<UserRole> findByUserId(@Param("userId") String userId);

}
