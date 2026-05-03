package com.smartcity.smartcity.service;

import com.smartcity.smartcity.dto.UserRegistrationDto;
import com.smartcity.smartcity.model.Role;
import com.smartcity.smartcity.model.User;
import com.smartcity.smartcity.repository.RoleRepository;
import com.smartcity.smartcity.repository.SubscriptionRepository;
import com.smartcity.smartcity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
            .orElseThrow(() ->
                new UsernameNotFoundException(
                    "User not found: " + username));

        System.out.println("=================================");
        System.out.println("LOGIN USER : " + user.getUsername());
        System.out.println("EMAIL      : " + user.getEmail());
        System.out.println("ROLES SIZE : " + user.getRoles().size());

        List<SimpleGrantedAuthority> authorities =
            new ArrayList<>();

        for (Role role : user.getRoles()) {
            System.out.println("ROLE FOUND : " + role.getName());
            authorities.add(
                new SimpleGrantedAuthority(role.getName()));
        }

        if (authorities.isEmpty()) {
            System.out.println("WARNING: No roles found!" +
                " Adding default ROLE_USER");
            authorities.add(
                new SimpleGrantedAuthority("ROLE_USER"));
        }

        System.out.println("AUTHORITIES: " + authorities);
        System.out.println("=================================");

        return new org.springframework.security.core.userdetails
            .User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true, true, true,
                authorities
            );
    }

    // ── Register New User ─────────────────────────────────
    @Transactional
    public void registerUser(UserRegistrationDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(
            passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setCity(dto.getCity());
        user.setUserType(dto.getUserType());

        // Auto assign ROLE_USER on registration
        Optional<Role> userRole =
            roleRepository.findByName("ROLE_USER");
        userRole.ifPresent(role ->
            user.getRoles().add(role));

        userRepository.save(user);
    }

    // ── Delete User (with subscriptions) ─────────────────
    @Transactional
    public void deleteUser(Long id) {
        // Delete subscriptions first
        subscriptionRepository.deleteByUserId(id);
        // Then delete user
        userRepository.deleteById(id);
    }

    // ── Utility Methods ───────────────────────────────────
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}