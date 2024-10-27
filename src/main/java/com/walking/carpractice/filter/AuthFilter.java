package com.walking.carpractice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

public class AuthFilter extends HttpFilter {
    private static final Set<String> UNSECURED_API = Set.of(
            "/signUp", "/auth", "/login"
    );

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (UNSECURED_API.contains(request.getServletPath())) {
            chain.doFilter(request, response);
            return;
        }

        var session = request.getSession(false);

//        Опираться только на наличие сессии на самом деле опасно. Сессия может быть создана неявно.
//        Например, JSP-страницей. При этом атрибутов сессии, характерных для нашей логики аутентификации, в такой
//        сессии, очевидно, не будет
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("./login");
            return;
        }

        chain.doFilter(request, response);
    }
}
