package com.sk.ecs.application.admin.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sk.ecs.application.common.dto.ResponseStatus;
import com.sk.ecs.application.common.exception.StandardRespondException;
import com.sk.ecs.application.common.handler.CustomRestHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import java.io.IOException;

@Component
public class AdminAccessDeniedHandler extends CustomRestHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    public AdminAccessDeniedHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public void commence(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        respondException(response, new StandardRespondException(ResponseStatus.NOT_LOGGED));
    }

    @Override
    public void handle(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        respondException(response, new StandardRespondException(ResponseStatus.ACCESS_DENIED));
    }
}
