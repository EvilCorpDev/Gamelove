package org.duckdns.androidghost77.gamelove.security;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import lombok.RequiredArgsConstructor;
import org.duckdns.androidghost77.gamelove.enums.UserRoleType;
import org.duckdns.androidghost77.gamelove.exception.UserNotFoundException;
import org.duckdns.androidghost77.gamelove.repository.UserRepository;
import org.duckdns.androidghost77.gamelove.repository.UserRoleRepository;
import org.duckdns.androidghost77.gamelove.repository.model.User;
import org.duckdns.androidghost77.gamelove.repository.model.UserRole;
import org.duckdns.androidghost77.gamelove.security.dto.UserPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

@RequiredArgsConstructor
public class DbUserDetailsManager implements UserDetailsManager {

    private static final String DEFAULT_USER_NAME = "admin";
    private static final String DEFAULT_USER_PASS = "nimda";
    private static final String DEFAULT_USER_ID = "default_id";
    private static final UserRoleType DEFAULT_USER_ROLE = UserRoleType.ADMIN;
    private final UserRepository userRepo;
    private final UserRoleRepository userRoleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        long usersInDb = userRepo.count();

        if (usersInDb == 0) {
            return new UserPrincipal(DEFAULT_USER_ID, DEFAULT_USER_NAME, passwordEncoder.encode(DEFAULT_USER_PASS),
                    emptyList());
        }

        User dbUser = userRepo.findUserByUsername(username);
        if (dbUser == null) {
            throw new UsernameNotFoundException(String.format("Can't find user with username: %s", username));
        }
        return new UserPrincipal(dbUser.getId(), dbUser.getUsername(), dbUser.getPasswordHash(), emptyList());
    }


    public UserDetails loadUserById(String userId) {
        if (userId.equals(DEFAULT_USER_ID)) {
            return new UserPrincipal(DEFAULT_USER_ID, DEFAULT_USER_NAME, passwordEncoder.encode(DEFAULT_USER_PASS),
                    singletonList(new SimpleGrantedAuthority(DEFAULT_USER_ROLE.toString())));
        }

        User userById = userRepo.findById(userId)
                .orElseThrow(
                        () -> new UserNotFoundException(String.format("Can't find user with id: %s", userId), userId));
        UserRoleType role = userRoleRepository.findByUserId(userId)
                .map(UserRole::getUserRoleType)
                .orElse(UserRoleType.USER);

        return new UserPrincipal(userById.getId(), userById.getUsername(), userById.getPasswordHash(),
                singletonList(new SimpleGrantedAuthority(role.toString())));
    }

    @Override
    public void createUser(UserDetails user) {
        User dbUser = new User();
        dbUser.setUsername(user.getUsername());
        dbUser.setPasswordHash(passwordEncoder.encode(user.getPassword()));

        userRepo.save(dbUser);
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteUser(String username) {
        userRepo.deleteByUsername(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean userExists(String username) {
        return userRepo.findUserByUsername(username) != null;
    }
}
