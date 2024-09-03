package com.sk.ecs.application.admin.filter;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sk.ecs.application.admin.auth.AdminAuthToken;
import com.sk.ecs.application.common.dto.ResponseStatus;
import com.sk.ecs.application.common.exception.StandardRespondException;
import com.sk.ecs.application.admin.dto.LoginDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StringUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public AdminLoginFilter(String processesUrl, ObjectMapper objectMapper) {
        super(processesUrl);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        LoginDto loginDto;
        MediaType contextType;

        try {
            contextType = MediaType.valueOf(request.getHeader(HttpHeaders.CONTENT_TYPE));
        } catch (Exception exception) {
            throw new BadCredentialsException("无法识别的内容类型");
        }

        if (contextType.isCompatibleWith(MediaType.APPLICATION_FORM_URLENCODED)) {
            loginDto = new LoginDto(request.getParameter("username"), request.getParameter("password"));
        } else if (contextType.isCompatibleWith(MediaType.MULTIPART_FORM_DATA)) {
            loginDto = new LoginDto(request.getParameter("username"), request.getParameter("password"));
        } else if (contextType.isCompatibleWith(MediaType.APPLICATION_JSON)) {
            try {
                loginDto = this.objectMapper.readValue(request.getInputStream(), LoginDto.class);
            } catch (JacksonException e) {
                throw new BadCredentialsException("", new StandardRespondException(ResponseStatus.PARAMETER_ERROR, "JSON格式不正确"));
            }
        } else {
            throw new BadCredentialsException("", new StandardRespondException(ResponseStatus.PARAMETER_ERROR, "无法识别的内容类型"));
        }

        if (!StringUtils.hasText(loginDto.getUsername()) || !StringUtils.hasText(loginDto.getPassword())) {
            throw new BadCredentialsException("", new StandardRespondException(ResponseStatus.PARAMETER_ERROR, "用户名或者密码不能为空"));
        }

        AdminAuthToken authRequest = new AdminAuthToken(loginDto.getUsername(), loginDto.getPassword());
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
