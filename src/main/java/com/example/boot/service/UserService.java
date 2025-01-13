package com.example.boot.service;


import com.example.boot.config.JwtTokenProvider;
import com.example.boot.entity.JwtResponse;
import com.example.boot.entity.User;
import com.example.boot.extra.CustomUserDetails;
import com.example.boot.payload.LoginRequest;
import com.example.boot.payload.RegisterRequest;
import com.example.boot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Register a new user
    public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {
        // Check if user already exists
        if (userRepository.findByUsername(registerRequest.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }

        // Create a new User entity
        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setEmail(registerRequest.getEmail());
        newUser.setRole("USER"); // Default role can be USER or you can allow role selection

        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully!");
    }

    public ResponseEntity<?> loginUser(LoginRequest loginRequest) {
        // Authenticate the user using the AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // Generate JWT token using the JwtTokenProvider
        String jwt = jwtTokenProvider.generateToken(authentication);

        // Get user details from the authentication object (assuming user details include username and email)
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername(); // Get username from the authenticated user
        String email = userDetails.getEmail(); // Get email from the authenticated user (if available)

        // Create the JwtResponse object with the token, username, and email
        JwtResponse jwtResponse = new JwtResponse(jwt, username,email);

        // Return the JWT response
        return ResponseEntity.ok(jwtResponse);
    }


    // Get user profile details
    public ResponseEntity<?> getUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Return user details (exclude password)
        return ResponseEntity.ok(user);
    }
}

