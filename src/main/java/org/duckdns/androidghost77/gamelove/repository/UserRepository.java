package org.duckdns.androidghost77.gamelove.repository;

import org.duckdns.androidghost77.gamelove.repository.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findUserByUsername(String username);

    void deleteByUsername(String username);
}
