package com.walking.carpractice.servlet;

import com.walking.carpractice.constant.ContextAttributeNames;
import com.walking.carpractice.exception.AuthException;
import com.walking.carpractice.service.UserService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AuthServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init(ServletConfig config) {
        var servletContext = config.getServletContext();

        this.userService = (UserService) servletContext.getAttribute(ContextAttributeNames.USER_SERVICE);
    }

    /**
     * Используется для получения формы авторизации пользователем
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login").forward(request, response);
    }

    /**
     * Используется для авторизации пользователей в системе.
     * Т.к. предполагается получение данных из формы, используется API для работы с параметрами запроса
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var username = request.getParameter("username");
        var password = request.getParameter("password");

        try {
            var user = userService.auth(username, password);

            var session = request.getSession(true);
//        Если в дальнейшем нам потребуется получить информации о пользователе, отправившем запрос, внутри приложения,
//        всю информацию мы сможем получить из атрибутов сессии
            session.setAttribute("userData", user);
            session.setAttribute("userId", user.getId());
        } catch (AuthException e) {
            handleError(request, response);
        }
    }

    /**
     * Используется для выхода пользователя из системы
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect("./login");
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(401);
        request.getRequestDispatcher("/login").forward(request, response);
    }
}
