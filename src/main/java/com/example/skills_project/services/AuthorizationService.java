package com.example.skills_project.services;

import com.example.skills_project.users.User;
import com.example.skills_project.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    UserRepository repository;
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLogin(username).get();
    }
}
