package com.family.myfamily.security.services.impl;

import com.family.myfamily.model.entities.IndividualEntity;
import com.family.myfamily.model.entities.UserEntity;
import com.family.myfamily.payload.request.LoginRequest;
import com.family.myfamily.payload.response.JwtResponse;
import com.family.myfamily.repository.IndividualRepository;
import com.family.myfamily.repository.UserRepository;
import com.family.myfamily.security.jwt.JwtUtils;
import com.family.myfamily.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getPhoneNumber(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();
        UserEntity user = userRepository.findByPhoneNumber(userDetails.getUsername());

        return JwtResponse.builder()
                .id(user.getId())
                .token(jwt)
                .build();
    }
}
