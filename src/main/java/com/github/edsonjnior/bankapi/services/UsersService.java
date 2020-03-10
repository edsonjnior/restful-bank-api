package com.github.edsonjnior.bankapi.services;

import com.github.edsonjnior.bankapi.entities.UserEntity;
import com.github.edsonjnior.bankapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

public interface UsersService extends UserDetailsService {
    @Service
    class UsersServiceImpl implements UsersService {

        @Autowired
        private UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
            UserEntity userStored = userRepository.findUserByLogin(login);
            if (userStored == null) throw new UsernameNotFoundException(login);

            return new User(userStored.getLogin(), userStored.getPassword(), getUserAuthorities(userStored));
        }

        private Collection<? extends GrantedAuthority> getUserAuthorities(UserEntity user) {
            return user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());
        }
    }
}
