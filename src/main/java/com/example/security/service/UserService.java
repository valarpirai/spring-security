package com.example.security.service;

import com.example.security.model.MyUser;
import com.example.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MyUser createUser(MyUser user) {
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var myUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
        return User.builder()
                .username(myUser.getUsername())
                .password(myUser.getPassword())
                .authorities(myUser.getAuthorities())
                .roles(myUser.getRole())
                .build();
    }
}
