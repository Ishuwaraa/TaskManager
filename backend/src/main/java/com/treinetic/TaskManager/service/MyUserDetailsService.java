package com.treinetic.TaskManager.service;

import com.treinetic.TaskManager.model.MyUser;
import com.treinetic.TaskManager.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser existingUser = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.builder()
                .username(existingUser.getUsername())
                .password(existingUser.getPassword())
                .build();
    }
}
