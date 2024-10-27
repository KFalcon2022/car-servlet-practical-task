package com.walking.carpractice.converter.dto.car;

import com.walking.carpractice.converter.Converter;
import com.walking.carpractice.domain.Car;
import com.walking.carpractice.model.car.CarDto;

public class CarDtoConverter implements Converter<Car, CarDto> {
    @Override
    public CarDto convert(Car car) {
        var carDto = new CarDto();

        carDto.setId(car.getId());
        carDto.setNumber(car.getNumber());
        carDto.setYear(car.getYear());
        carDto.setColor(car.getColor());
        carDto.setActualTechnicalInspection(car.isActualTechnicalInspection());

        return carDto;
    }
}
