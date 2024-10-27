package com.walking.carpractice.model.car.request;

// Для DTO использование наследования является спорным подходом. Поэтому могут возникать ситуации, как здесь,
// когда несколько классов практически полностью дублируют друг друга
public class UpdateCarRequest {
    private Long id;
    private String number;
    private int year;
    private String color;
    private boolean actualTechnicalInspection;

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public int getYear() {
        return year;
    }

    public String getColor() {
        return color;
    }

    public boolean isActualTechnicalInspection() {
        return actualTechnicalInspection;
    }
}
