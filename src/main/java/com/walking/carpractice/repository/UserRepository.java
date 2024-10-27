package com.walking.carpractice.repository;

import com.walking.carpractice.converter.db.UserConverter;
import com.walking.carpractice.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepository {
    private final DataSource datasource;
    private final UserConverter converter;

    public UserRepository(DataSource datasource, UserConverter converter) {
        this.datasource = datasource;
        this.converter = converter;
    }

    public Optional<User> findByUsername(String username) {
        try (Connection connection = datasource.getConnection()) {
            return findByUsername(username, connection);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении пользователя", e);
        }
    }

    public Optional<User> findByUsername(String username, Connection connection) {
        var sql = "select * from \"user\" where username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            ResultSet result = statement.executeQuery();

            return converter.convert(result);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении пользователя", e);
        }
    }

    public User create(User user) {
        try (Connection connection = datasource.getConnection()) {
            return create(user, connection);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании пользователя", e);
        }
    }

    public User create(User user, Connection connection) {
        var sql = """
                insert into "user" (username, first_name, second_name, password)
                values (?, ?, ?, ?)
                returning id
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            setParameters(user, statement);
            ResultSet result = statement.executeQuery();

            result.next();
            user.setId(result.getLong(1));

            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании пользователя", e);
        }
    }

    private void setParameters(User user, PreparedStatement statement) throws SQLException {
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getFirstName());
        statement.setString(3, user.getSecondName());
        statement.setString(4, user.getPassword());
    }
}
