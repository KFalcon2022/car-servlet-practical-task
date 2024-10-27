package com.walking.carpractice.model.car;

// Реализация паттерна DTO. На данной сущности его необходимость не очевидна,
// но в следующих подзадачах станет более прозрачна
public class CarDto {
    private Long id;
    private String number;
    private int year;
    private String color;
    private boolean actualTechnicalInspection;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isActualTechnicalInspection() {
        return actualTechnicalInspection;
    }

    public void setActualTechnicalInspection(boolean actualTechnicalInspection) {
        this.actualTechnicalInspection = actualTechnicalInspection;
    }
}
