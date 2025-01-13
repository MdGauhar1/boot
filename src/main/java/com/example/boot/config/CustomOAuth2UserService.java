package com.example.boot.config;


import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;

import com.example.boot.extra.CustomOAuth2User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // Use the default OAuth2UserService to load the user info from the provider (e.g., Google)
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // Map authorities (roles)
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");

        // Return a CustomOAuth2User with the authorities and attributes
        return new CustomOAuth2User(oAuth2User, Collections.singletonList(authority), attributes);
    }
}
