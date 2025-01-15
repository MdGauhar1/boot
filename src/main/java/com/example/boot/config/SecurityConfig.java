package com.example.boot.config;

import com.example.boot.config.CustomOAuth2UserService;
import com.example.boot.config.JwtAuthenticationFilter;
import com.example.boot.config.JwtTokenProvider;
import com.example.boot.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for REST API
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/api/users/register", "/api/users/login", "/oauth2/authorization/google")
                                .permitAll()  // Public access to these endpoints
                                .requestMatchers("/api/orders/**").authenticated() // Only authenticated users can access orders
                                .requestMatchers("/api/products/**").authenticated() // Only authenticated users can access products
                                .anyRequest().authenticated()  // All other requests require authentication
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Ensure the JWT filter is in place
                .oauth2Login(oauth2Login ->
                        oauth2Login.userInfoEndpoint(userInfoEndpoint ->
                                userInfoEndpoint.userService(oauth2UserService()) // Custom OAuth2 service, if needed
                        )
                );

        return http.build();
    }

    @Bean
    public OAuth2UserService oauth2UserService() {
        return new CustomOAuth2UserService(); // This is needed if you're handling OAuth2 login
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Password encoder for regular login
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)  // Custom user details service
                .passwordEncoder(passwordEncoder());  // Ensure password is encoded

        return authenticationManagerBuilder.build();  // Build and return the AuthenticationManager
    }
}
