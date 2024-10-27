package com.walking.carpractice.service;

import com.walking.carpractice.domain.Car;
import com.walking.carpractice.repository.CarRepository;

public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car getById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Машина с id '%d' не существует".formatted(id)));
    }

    public Car create(Car car) {
        return carRepository.create(car);
    }

    public Car update(Car car) {
        return carRepository.update(car);
    }

    public void delete(Long id) {
        carRepository.deleteById(id);
    }
}
