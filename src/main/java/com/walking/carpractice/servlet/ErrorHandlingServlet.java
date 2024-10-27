package com.walking.carpractice.servlet;

import com.walking.carpractice.exception.CommonAppException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ErrorHandlingServlet extends HttpServlet {
    public static final String ERROR_ATTRIBUTE_KEY = "jakarta.servlet.error.exception";
    public static final String ERROR_MESSAGE_ATTRIBUTE_KEY = "jakarta.servlet.error.message";
    public static final String STATUS_CODE_ATTRIBUTE_KEY = "jakarta.servlet.error.status_code";
    public static final String REQUEST_URI_ATTRIBUTE_KEY = "jakarta.servlet.error.request_uri";

    private static final Logger log = LogManager.getLogger(ErrorHandlingServlet.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var statusCode = (Integer) request.getAttribute(STATUS_CODE_ATTRIBUTE_KEY);

        if (statusCode == 404) {
            log.error("Ресурс не найден. Адрес: {}", request.getAttribute(REQUEST_URI_ATTRIBUTE_KEY));

            request.getRequestDispatcher("/notFound").forward(request, response);
            return;
        }

        if (statusCode == 400) {
            log.error("Невалидное тело запроса. Адрес: {}", request.getAttribute(REQUEST_URI_ATTRIBUTE_KEY));

            request.getRequestDispatcher("/badRequest").forward(request, response);
            return;
        }

        var exception = (Throwable) request.getAttribute(ERROR_ATTRIBUTE_KEY);

        log.error("Ошибка обработки запроса", exception);

        // Скрываем детали об ошибке, если не контролируем ее сообщение.
        // Или проставляем, если контейнер не заполнил данный атрибут самостоятельно
        var errorMessage = exception instanceof CommonAppException
                ? exception.getMessage()
                : "Неизвестная ошибка";

        request.setAttribute(ERROR_MESSAGE_ATTRIBUTE_KEY, errorMessage);

        request.getRequestDispatcher("/internalError").forward(request, response);
    }
}
