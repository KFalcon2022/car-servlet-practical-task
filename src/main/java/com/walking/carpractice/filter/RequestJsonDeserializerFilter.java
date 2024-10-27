package com.walking.carpractice.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walking.carpractice.constant.ContextAttributeNames;
import com.walking.carpractice.model.user.request.CreateUserRequest;
import com.walking.carpractice.model.user.request.LoginRequest;
import com.walking.carpractice.model.car.request.CreateCarRequest;
import com.walking.carpractice.model.car.request.UpdateCarRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestJsonDeserializerFilter extends HttpFilter {
    public static final String POJO_REQUEST_BODY = "pojoRequestBody";

    private static final Logger log = LogManager.getLogger(RequestJsonDeserializerFilter.class);

    private Map<String, TypeReference<?>> targetTypes;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        this.objectMapper = (ObjectMapper) getServletContext().getAttribute(ContextAttributeNames.OBJECT_MAPPER);

        initTargetTypes();
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!"application/json".equals(request.getContentType()) || request.getContentLength() == 0) {
            chain.doFilter(request, response);
            return;
        }

        byte[] jsonBody = request.getInputStream().readAllBytes();

        try {
            var targetType = getTargetType(request);
            Object pojoBody = objectMapper.readValue(jsonBody, targetType);

            request.setAttribute(POJO_REQUEST_BODY, pojoBody);
        } catch (IOException e) {
            log.error("Ошибка десериализации тела запроса", e);
            throw e;
        }

        chain.doFilter(request, response);
    }

    // Такое решение не слишком гибко и требует ручной актуализации при каждом обновлении API. Но фактически оно может
    // служить прототипом аналогичных решений в современных подходах. Задачу автоматизации сбора данных о целевых типах
    // можно решить как самостоятельную, суть работы фильтра от этого не изменится.
    // При желании логика определения целевого типа может быть вынесена в отдельный класс
    private TypeReference<?> getTargetType(HttpServletRequest request) {
        var key = "%s&&%s".formatted(request.getServletPath(), request.getMethod());

        return targetTypes.getOrDefault(key, new TypeReference<>() {
        });
    }

    private void initTargetTypes() {
        targetTypes = new ConcurrentHashMap<>();

        targetTypes.put("/car&&POST", new TypeReference<CreateCarRequest>() {});
        targetTypes.put("/car&&PUT", new TypeReference<UpdateCarRequest>() {});
        targetTypes.put("/auth&&POST", new TypeReference<LoginRequest>() {});
        targetTypes.put("/signUp&&POST", new TypeReference<CreateUserRequest>() {});
    }
}
