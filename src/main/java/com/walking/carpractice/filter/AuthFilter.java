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
            "/signUp", "/auth"
    );

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (UNSECURED_API.contains(request.getServletPath())) {
            chain.doFilter(request, response);
            return;
        }

        var session = request.getSession(false);

        if (session == null) {
            response.sendError(401);
            return;
        }

        chain.doFilter(request, response);
    }
}
