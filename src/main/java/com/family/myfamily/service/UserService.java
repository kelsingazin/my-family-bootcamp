package com.family.myfamily.service;

import com.family.myfamily.payload.response.UserData;

import java.util.UUID;

public interface UserService {
    UserData getUserData(UUID uuid);
}
