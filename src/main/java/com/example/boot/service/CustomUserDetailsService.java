package com.example.boot.service;

import com.example.boot.entity.User;
import com.example.boot.extra.CustomUserDetails;  // Import CustomUserDetails
import com.example.boot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }





    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch the user from the database by their username
        User user = userRepository.findByUsername(username);

        // If the user does not exist, throw UsernameNotFoundException
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Return a CustomUserDetails object (not the default Spring Security User)
        //return new CustomUserDetails(user);  // Using your custom user details class
        return CustomUserDetails.fromUserEntityToCustomUserDetails(user);
    }
}
