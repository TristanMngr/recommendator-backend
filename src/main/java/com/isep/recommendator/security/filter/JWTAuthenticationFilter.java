package com.isep.recommendator.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isep.recommendator.security.config.TokenProperties;
import com.isep.recommendator.security.service.CustomUserDetailsService;
import com.isep.recommendator.security.handler.JWTAuthenticationFailureHandler;
import com.isep.recommendator.security.service.TokenService;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Configurable
@EnableConfigurationProperties(TokenProperties.class)
public class JWTAuthenticationFilter
        extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private CustomUserDetailsService customUserDetailsService;
    private TokenService tokenService;

    private static final String TOKEN_PREFIX = TokenProperties.getTokenPrefix();
    private static final String HEADER_STRING = TokenProperties.getHeaderString();

    public JWTAuthenticationFilter(
            AuthenticationManager authenticationManager,
            CustomUserDetailsService customUserDetailsService, TokenService tokenService) {
        super.setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.tokenService = tokenService;
    }

    // (voir issue pour handler)
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        UserDetails user = customUserDetailsService.loadUserByUsername(email);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    email,
                    password,
                    user.getAuthorities()
        );
        try {
            return authenticationManager.authenticate(token);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("invalid password");
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        ArrayList<String> roles = tokenService.getRolesList(auth);
        String token = tokenService.buildToken(auth.getName(), roles);

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);

        HashMap<String, Object> respObject = tokenService.getSuccessResponse(token);
        String resp = new ObjectMapper().writeValueAsString(respObject);

        res.getWriter().println(resp);
    }
}
