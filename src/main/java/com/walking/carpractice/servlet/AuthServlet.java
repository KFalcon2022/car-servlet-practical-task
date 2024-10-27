package com.walking.carpractice.servlet;

import com.walking.carpractice.constant.ContextAttributeNames;
import com.walking.carpractice.exception.AuthException;
import com.walking.carpractice.filter.RequestJsonDeserializerFilter;
import com.walking.carpractice.filter.ResponseJsonSerializerFilter;
import com.walking.carpractice.model.user.request.LoginRequest;
import com.walking.carpractice.service.UserService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class AuthServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init(ServletConfig config) {
        var servletContext = config.getServletContext();

        this.userService = (UserService) servletContext.getAttribute(ContextAttributeNames.USER_SERVICE);
    }

    /**
     * Используется для авторизации пользователей в системе
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        var loginRequest = (LoginRequest) request.getAttribute(RequestJsonDeserializerFilter.POJO_REQUEST_BODY);

        try {
            var user = userService.auth(loginRequest.getUsername(), loginRequest.getPassword());

            var session = request.getSession(true);
//        Если в дальнейшем нам потребуется получить информации о пользователе, отправившем запрос, внутри приложения,
//        всю информацию мы сможем получить из атрибутов сессии
            session.setAttribute("userData", user);
            session.setAttribute("userId", user.getId());
        } catch (AuthException e) {
            handleError(request, response, e);
        }
    }

    /**
     * Используется для выхода пользователя из системы
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        var session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             AuthException e) {
        response.setStatus(401);

        var errorMessage = Map.of("errorData", e.getMessage());
        request.setAttribute(ResponseJsonSerializerFilter.POJO_RESPONSE_BODY, errorMessage);
    }
}
