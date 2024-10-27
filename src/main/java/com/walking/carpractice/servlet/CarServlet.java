package com.walking.carpractice.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walking.carpractice.constant.ContextAttributeNames;
import com.walking.carpractice.converter.dto.CarDtoConverter;
import com.walking.carpractice.converter.dto.request.CreateCarRequestConverter;
import com.walking.carpractice.converter.dto.request.UpdateCarRequestConverter;
import com.walking.carpractice.model.CarDto;
import com.walking.carpractice.model.request.CreateCarRequest;
import com.walking.carpractice.model.request.UpdateCarRequest;
import com.walking.carpractice.service.CarService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class CarServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(CarServlet.class);

    private ObjectMapper objectMapper;

    private CarService carService;

    private CarDtoConverter carDtoConverter;
    private CreateCarRequestConverter createCarRequestConverter;
    private UpdateCarRequestConverter updateCarRequestConverter;

    @Override
    public void init(ServletConfig config) {
        var servletContext = config.getServletContext();

        this.objectMapper = (ObjectMapper) servletContext.getAttribute(ContextAttributeNames.OBJECT_MAPPER);
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var id = Long.valueOf(request.getParameter("id"));

        var car = carService.getById(id);
        var carDto = carDtoConverter.convert(car);

        writeResponseBody(carDto, response);
    }

    /**
     * Используется для создания машины. Данные передаются в теле запроса.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var carRequest = getRequest(request, CreateCarRequest.class);
        var car = createCarRequestConverter.convert(carRequest);

        var createdCar = carService.create(car);
        var carDto = carDtoConverter.convert(createdCar);

        writeResponseBody(carDto, response);
    }

    /**
     * Используется для обновления машины. Данные передаются в теле запроса в виде полностью актуального состояния
     * машины. Включая неизмененные поля.
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var carRequest = getRequest(request, UpdateCarRequest.class);
        var car = updateCarRequestConverter.convert(carRequest);

        var createdCar = carService.update(car);
        var carDto = carDtoConverter.convert(createdCar);

        writeResponseBody(carDto, response);
    }

    /**
     * Используется для удаления машины по id. Идентификатор передается параметром запроса с именем "id".
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        var id = Long.valueOf(request.getParameter("id"));

        carService.delete(id);
    }

    private <T> T getRequest(HttpServletRequest request, Class<T> clazz) throws IOException {
        try {
            return objectMapper.readValue(request.getInputStream(), clazz);
        } catch (IOException e) {
            log.error("Ошибка десериализации тела запроса", e);
            throw e;
        }
    }

    private void writeResponseBody(CarDto carDto, HttpServletResponse response) throws IOException {
        try {
            response.getOutputStream()
                    .write(objectMapper.writeValueAsBytes(carDto));

            response.setContentType("application/json");
        } catch (IOException e) {
            log.error("Ошибка формирования тела ответа", e);
            throw e;
        }
    }
}
