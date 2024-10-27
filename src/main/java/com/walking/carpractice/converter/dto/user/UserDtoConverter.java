package com.walking.carpractice.converter.dto.user;

import com.walking.carpractice.converter.Converter;
import com.walking.carpractice.domain.User;
import com.walking.carpractice.model.user.UserDto;

public class UserDtoConverter implements Converter<User, UserDto> {
    @Override
    public UserDto convert(User source) {
        var userDto = new UserDto();

        userDto.setId(source.getId());
        userDto.setFirstName(source.getFirstName());
        userDto.setSecondName(source.getSecondName());
        userDto.setUsername(source.getUsername());

        return userDto;
    }
}
