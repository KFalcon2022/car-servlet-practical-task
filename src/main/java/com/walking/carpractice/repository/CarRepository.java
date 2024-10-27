package com.walking.carpractice.repository;

import com.walking.carpractice.converter.db.CarConverter;
import com.walking.carpractice.domain.Car;
import com.walking.carpractice.exception.DataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CarRepository {
    private final DataSource datasource;
    private final CarConverter converter;

    public CarRepository(DataSource datasource, CarConverter converter) {
        this.datasource = datasource;
        this.converter = converter;
    }

    public Optional<Car> findById(Long id) {
        try (Connection connection = datasource.getConnection()) {
            return findById(id, connection);
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка при получении машины", e);
        }
    }

    public Optional<Car> findById(Long id, Connection connection) {
        var sql = "select * from car where id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();

            return converter.convert(result);
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка при получении машины", e);
        }
    }

    public Car create(Car car) {
        try (Connection connection = datasource.getConnection()) {
            return create(car, connection);
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка при создании машины", e);
        }
    }

    public Car create(Car car, Connection connection) {
        var sql = """
                insert into car (number, year, color, actual_technical_inspection)
                values (?, ?, ?, ?)
                returning id
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            setParameters(car, statement);
            ResultSet result = statement.executeQuery();

            result.next();
            car.setId(result.getLong(1));

            return car;
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка при создании машины", e);
        }
    }

    public Car update(Car car) {
        try (Connection connection = datasource.getConnection()) {
            return update(car, connection);
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка при создании машины", e);
        }
    }

    public Car update(Car car, Connection connection) {
        String sql = """
                update car
                    set number  = ?,
                        year = ?,
                        color = ?,
                        actual_technical_inspection = ?
                    where id = ?""";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            setParameters(car, statement);
            statement.setLong(5, car.getId());
            statement.executeUpdate();

            return car;
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка при обновлении машины", e);
        }
    }

    public void deleteById(Long id) {
        String sql = "delete from car where id = ?";

        try (Connection connection = datasource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка при удалении машины", e);
        }
    }

    private void setParameters(Car car, PreparedStatement statement) throws SQLException {
        statement.setString(1, car.getNumber());
        statement.setInt(2, car.getYear());
        statement.setString(3, car.getColor());
        statement.setBoolean(4, car.isActualTechnicalInspection());
    }
}
