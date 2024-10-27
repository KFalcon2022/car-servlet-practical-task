package com.walking.carpractice.converter.dto.user.request;

import com.walking.carpractice.converter.Converter;
import com.walking.carpractice.domain.User;
import com.walking.carpractice.model.user.request.CreateUserRequest;

public class CreateUserRequestConverter implements Converter<CreateUserRequest, User> {
    @Override
    public User convert(CreateUserRequest source) {
        var user = new User();

        user.setFirstName(source.getFirstName());
        user.setSecondName(source.getSecondName());
        user.setUsername(source.getUsername());
        user.setPassword(source.getPassword());

        return user;
    }
}
