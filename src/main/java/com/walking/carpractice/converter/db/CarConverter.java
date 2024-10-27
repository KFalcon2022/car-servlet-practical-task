package com.walking.carpractice.converter.db;

import com.walking.carpractice.domain.Car;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CarConverter implements ResultSetConverter<Optional<Car>> {
    @Override
    public Optional<Car> convert(ResultSet rs) throws SQLException {
        return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
    }

    private Car mapRow(ResultSet rs) throws SQLException {
        var car = new Car();

        car.setId(rs.getLong("id"));
        car.setNumber(rs.getString("number"));
        car.setYear(rs.getInt("year"));
        car.setColor(rs.getString("color"));
        car.setActualTechnicalInspection(rs.getBoolean("actual_technical_inspection"));

        return car;
    }
}
