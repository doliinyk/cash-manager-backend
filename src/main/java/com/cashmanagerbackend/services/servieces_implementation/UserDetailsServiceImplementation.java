package com.cashmanagerbackend.services.servieces_implementation;

import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImplementation  implements UserDetailsService {
    private final UserRepository userRepository;
    @Autowired
    public UserDetailsServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository
                .findByLogin(username);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("User name not found: " + username);
        }
        return user.get();
    }
}
