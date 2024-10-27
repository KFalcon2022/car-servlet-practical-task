package com.walking.carpractice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walking.carpractice.constant.ContextAttributeNames;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ResponseJsonSerializerFilter extends HttpFilter {
    public static final String POJO_RESPONSE_BODY = "pojoResponseBody";

    private static final Logger log = LogManager.getLogger(ResponseJsonSerializerFilter.class);

    private ObjectMapper objectMapper;

    @Override
    public void init() {
        this.objectMapper = (ObjectMapper) getServletContext().getAttribute(ContextAttributeNames.OBJECT_MAPPER);
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);

        var pojoBody = request.getAttribute(POJO_RESPONSE_BODY);

        if (pojoBody == null) {
            return;
        }

        try {
            response.getOutputStream()
                    .write(objectMapper.writeValueAsBytes(pojoBody));

            response.setContentType("application/json");
        } catch (IOException e) {
            log.error("Ошибка формирования тела ответа", e);
            throw e;
        }
    }
}
