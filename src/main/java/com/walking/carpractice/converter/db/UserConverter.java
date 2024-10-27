package com.walking.carpractice.converter.db;

import com.walking.carpractice.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserConverter implements ResultSetConverter<Optional<User>> {
    @Override
    public Optional<User> convert(ResultSet rs) throws SQLException {
        return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
    }

    private User mapRow(ResultSet rs) throws SQLException {
        var user = new User();

        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setFirstName(rs.getString("first_name"));
        user.setSecondName(rs.getString("second_name"));
        user.setPassword(rs.getString("password"));

        return user;
    }
}
