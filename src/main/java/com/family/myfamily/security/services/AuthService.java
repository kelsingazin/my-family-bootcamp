package com.family.myfamily.security.services;

import com.family.myfamily.payload.request.LoginRequest;
import com.family.myfamily.payload.response.JwtResponse;

public interface AuthService {

    JwtResponse authenticateUser(LoginRequest loginRequest);
}
