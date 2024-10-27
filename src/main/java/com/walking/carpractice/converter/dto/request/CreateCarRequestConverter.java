package com.walking.carpractice.converter.dto.request;

import com.walking.carpractice.converter.Converter;
import com.walking.carpractice.domain.Car;
import com.walking.carpractice.model.request.CreateCarRequest;

public class CreateCarRequestConverter implements Converter<CreateCarRequest, Car> {
    @Override
    public Car convert(CreateCarRequest request) {
        var car = new Car();

        car.setNumber(request.getNumber());
        car.setYear(request.getYear());
        car.setColor(request.getColor());
        car.setActualTechnicalInspection(request.isActualTechnicalInspection());

        return car;
    }
}
