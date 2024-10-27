package com.walking.carpractice.servlet;

import com.walking.carpractice.constant.ContextAttributeNames;
import com.walking.carpractice.converter.dto.car.CarDtoConverter;
import com.walking.carpractice.converter.dto.car.request.CreateCarRequestConverter;
import com.walking.carpractice.converter.dto.car.request.UpdateCarRequestConverter;
import com.walking.carpractice.filter.RequestJsonDeserializerFilter;
import com.walking.carpractice.filter.ResponseJsonSerializerFilter;
import com.walking.carpractice.model.car.request.CreateCarRequest;
import com.walking.carpractice.model.car.request.UpdateCarRequest;
import com.walking.carpractice.service.CarService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CarServlet extends HttpServlet {
    private CarService carService;

    private CarDtoConverter carDtoConverter;
    private CreateCarRequestConverter createCarRequestConverter;
    private UpdateCarRequestConverter updateCarRequestConverter;

    @Override
    public void init(ServletConfig config) {
        var servletContext = config.getServletContext();

        this.carService = (CarService) servletContext.getAttribute(ContextAttributeNames.CAR_SERVICE);
        this.carDtoConverter = (CarDtoConverter) servletContext.getAttribute(ContextAttributeNames.CAR_DTO_CONVERTER);

        this.createCarRequestConverter = (CreateCarRequestConverter) servletContext.getAttribute(
                ContextAttributeNames.CREATE_CAR_REQUEST_CONVERTER);

        this.updateCarRequestConverter = (UpdateCarRequestConverter) servletContext.getAttribute(
                ContextAttributeNames.UPDATE_CAR_REQUEST_CONVERTER);
    }

    /**
     * Используется для получения машины по id. Идентификатор передается параметром запроса с именем "id".
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        var id = Long.valueOf(request.getParameter("id"));

        var car = carService.getById(id);
        var carDto = carDtoConverter.convert(car);

        request.setAttribute(ResponseJsonSerializerFilter.POJO_RESPONSE_BODY, carDto);
    }

    /**
     * Используется для создания машины. Данные передаются в теле запроса.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        var carRequest = (CreateCarRequest) request.getAttribute(RequestJsonDeserializerFilter.POJO_REQUEST_BODY);
        var car = createCarRequestConverter.convert(carRequest);

        var createdCar = carService.create(car);
        var carDto = carDtoConverter.convert(createdCar);

        request.setAttribute(ResponseJsonSerializerFilter.POJO_RESPONSE_BODY, carDto);
    }

    /**
     * Используется для обновления машины. Данные передаются в теле запроса в виде полностью актуального состояния
     * машины. Включая неизмененные поля.
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        var carRequest = (UpdateCarRequest) request.getAttribute(RequestJsonDeserializerFilter.POJO_REQUEST_BODY);
        var car = updateCarRequestConverter.convert(carRequest);

        var createdCar = carService.update(car);
        var carDto = carDtoConverter.convert(createdCar);

        request.setAttribute(ResponseJsonSerializerFilter.POJO_RESPONSE_BODY, carDto);
    }

    /**
     * Используется для удаления машины по id. Идентификатор передается параметром запроса с именем "id".
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        var id = Long.valueOf(request.getParameter("id"));

        carService.delete(id);
    }
}
