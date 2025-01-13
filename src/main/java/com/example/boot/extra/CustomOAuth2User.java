package com.example.boot.extra;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User extends User implements OAuth2User {

    private final OAuth2User oAuth2User;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(OAuth2User oAuth2User, Collection<SimpleGrantedAuthority> authorities, Map<String, Object> attributes) {
        super(oAuth2User.getName(), "", authorities);
        this.oAuth2User = oAuth2User;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public String getName() {
        return oAuth2User.getName();
    }

    // You can add any custom logic you need here, for example, mapping attributes to your User model
    public String getEmail() {
        return (String) attributes.get("email");
    }

    // You can add additional methods to access more attributes like name, profile picture, etc.
    public String getFullName() {
        return (String) attributes.get("name");
    }
}

