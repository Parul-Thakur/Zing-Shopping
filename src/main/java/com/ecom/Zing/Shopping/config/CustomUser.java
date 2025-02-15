package com.ecom.Zing.Shopping.config;

import com.ecom.Zing.Shopping.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class CustomUser implements UserDetails {
    private User user;

    public CustomUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority((user.getRole()));
        return Arrays.asList(authority);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }


    @Override
    public boolean isEnabled() {
        return user.getIsEnable() != null && user.getIsEnable();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // or user.getAccountNonExpired(); if such a field exists
    }

    @Override
    public boolean isAccountNonLocked() {
        return  user.getAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // or user.getCredentialsNonExpired(); if such a field exists
    }






}