package com.family.myfamily.security.services;

import com.family.myfamily.model.UserEntity;
import com.family.myfamily.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service("userDetails")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByPhoneNumber(username);
        if (user != null) {
            return new User(user.getPhoneNumber()
                    , user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
        } else {
            throw new UsernameNotFoundException("Invalid phone number or password");
        }
    }
}
