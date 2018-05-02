package com.isep.recommendator.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isep.recommendator.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private TokenService tokenService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        response.setStatus(401);
        response.setContentType("application/json");
        HashMap<String, Object> respObject = tokenService.getBadCredentialsResponse(exception.getMessage());
        String resp = new ObjectMapper().writeValueAsString(respObject);
        response.getWriter().append(resp);
    }


}

