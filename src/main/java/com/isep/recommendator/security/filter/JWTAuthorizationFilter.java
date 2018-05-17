package com.isep.recommendator.security.filter;

import com.isep.recommendator.security.config.TokenProperties;
import com.isep.recommendator.security.service.TokenService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthorizationFilter
        extends BasicAuthenticationFilter {

    private static final String TOKEN_PREFIX = TokenProperties.getTokenPrefix();
    private static final String HEADER_STRING = TokenProperties.getHeaderString();

    @Autowired
    private TokenService tokenService;

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    // Process the Request to extract the Token
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        if (tokenNotFound(req)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private boolean tokenNotFound(HttpServletRequest req){
        String header = req.getHeader(HEADER_STRING);
        if (header == null || !header.startsWith(TOKEN_PREFIX))
            return true;
        else
            return false;
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            Claims claims = tokenService.parseToken(token);
            String username = claims.getSubject();
            ArrayList<SimpleGrantedAuthority> authorities = tokenService.getRolesFromClaims(claims);

            if (username != null) {
                return new UsernamePasswordAuthenticationToken(username, null, authorities);
            }
            return null;
        }
        return null;
    }
}