package com.isep.recommendator.security;

import com.isep.recommendator.security.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthorizationFilter
        extends BasicAuthenticationFilter {

    private WebSecurityConfig webSecurityConfig;

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    // Inject the configuration into the Filter
    public void setWebSecurityConfig(WebSecurityConfig webSecurityConfig) {
        this.webSecurityConfig = webSecurityConfig;
    }

    // Process the Request to extract the Token
    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain
    ) throws IOException, ServletException {

        String header = req.getHeader(WebSecurityConfig.HEADER_STRING);
        if (header == null
                || !header.startsWith(WebSecurityConfig.TOKEN_PREFIX)) {
            // Token not found - leave
            chain.doFilter(req, res);
            return;
        }
        System.out.println(header);
        // Token Found - GetIt and Authenticate
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(
            HttpServletRequest request
    ) {
        System.out.println("YOOO");
        String token = request.getHeader(WebSecurityConfig.HEADER_STRING);
        if (token != null) {
            // parse the token.
            Claims claims = Jwts.parser()
                    .setSigningKey(webSecurityConfig.getSecret().getBytes())
                    .parseClaimsJws(token.replace(WebSecurityConfig.TOKEN_PREFIX, ""))
                    .getBody();
            // Extract the UserName
            String username = claims.getSubject();
            System.out.println(username);


            // Extract the Roles
            ArrayList<String> roles = (ArrayList<String>) claims.get("roles");
            System.out.println(roles);

            ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
            if (roles != null) {
                for (String role : roles) {
                    System.out.println(role);
                    SimpleGrantedAuthority auth = new SimpleGrantedAuthority(role);
                    authorities.add(auth);
                }
            }

            // Return an Authenticated user with the list of Roles attached
            if (username != null) {
                //UserDetails userD = customUserDetailsService.loadUserByUsername(username);
                //return new UsernamePasswordAuthenticationToken(username, null, authorities);
                return new UsernamePasswordAuthenticationToken(username, null, authorities);
            }
            return null;
        }
        return null;
    }
}