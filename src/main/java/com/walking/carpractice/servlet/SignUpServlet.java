package com.walking.carpractice.servlet;

import com.walking.carpractice.constant.ContextAttributeNames;
import com.walking.carpractice.converter.dto.user.UserDtoConverter;
import com.walking.carpractice.converter.dto.user.request.CreateUserRequestConverter;
import com.walking.carpractice.filter.RequestJsonDeserializerFilter;
import com.walking.carpractice.filter.ResponseJsonSerializerFilter;
import com.walking.carpractice.model.user.request.CreateUserRequest;
import com.walking.carpractice.service.UserService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SignUpServlet extends HttpServlet {
    private UserService userService;

    private UserDtoConverter userDtoConverter;
    private CreateUserRequestConverter createUserRequestConverter;

    @Override
    public void init(ServletConfig config) {
        var servletContext = config.getServletContext();

        this.userService = (UserService) servletContext.getAttribute(ContextAttributeNames.USER_SERVICE);

        this.userDtoConverter = (UserDtoConverter) servletContext.getAttribute(
                ContextAttributeNames.USER_DTO_CONVERTER);

        this.createUserRequestConverter = (CreateUserRequestConverter) servletContext.getAttribute(
                ContextAttributeNames.CREATE_USER_REQUEST_CONVERTER);
    }

    /**
     * Используется для регистрации пользователей в системе. Доступность этого запроса для авторизованного
     * пользователя - упрощение. В зависимости от специфики системы это может быть как допустимо, так и нет.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        var userRequest = (CreateUserRequest) request.getAttribute(RequestJsonDeserializerFilter.POJO_REQUEST_BODY);

        var user = createUserRequestConverter.convert(userRequest);

        var createdUser = userService.create(user);
        var userDto = userDtoConverter.convert(createdUser);

        request.setAttribute(ResponseJsonSerializerFilter.POJO_RESPONSE_BODY, userDto);

    }
}
