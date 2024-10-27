package com.walking.carpractice.converter.dto.request;

import com.walking.carpractice.converter.Converter;
import com.walking.carpractice.domain.Car;
import com.walking.carpractice.model.request.UpdateCarRequest;

public class UpdateCarRequestConverter implements Converter<UpdateCarRequest, Car> {
    @Override
    public Car convert(UpdateCarRequest request) {
        var car = new Car();

        car.setId(request.getId());
        car.setNumber(request.getNumber());
        car.setYear(request.getYear());
        car.setColor(request.getColor());
        car.setActualTechnicalInspection(request.isActualTechnicalInspection());

        return car;
    }
}
