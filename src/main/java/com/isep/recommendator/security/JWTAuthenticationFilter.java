package com.isep.recommendator.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isep.recommendator.app.model.User;
import com.isep.recommendator.app.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.isep.recommendator.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

@Configurable
public class JWTAuthenticationFilter
        extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private CustomUserDetailsService customUserDetailsService;
    private WebSecurityConfig config;

    public JWTAuthenticationFilter(
            AuthenticationManager authenticationManager,
            CustomUserDetailsService customUserDetailsService,
            WebSecurityConfig config) {
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.config = config;
    }
//
//    @Override
//    public Authentication attemptAuthentication(
//            HttpServletRequest req,
//            HttpServletResponse res) throws AuthenticationException {
//
//        boolean hasCreds = req.getParameter("email") != null && req.getParameter("password") != null;
//
//        if (!hasCreds){
////            // auth
////            String v = req.getHeader("Authorization");
////            if ( v != null && v.startsWith("Basic ") ) {
////                v = v.substring(6);
////                v = new String(Base64.decode(v.getBytes()));
////                StringTokenizer st = new StringTokenizer(v,":");
////                if ( st.countTokens() == 2 ) {
////                    creds = new AccountCredentials();
////                    creds.setUsername(st.nextToken());
////                    creds.setPassword(st.nextToken());
////                }
//            //}
//            System.out.println("yo");
//
//        }
//        if (hasCreds) {
//            System.out.println("yope");
//            // The Username & Password are passed to authenticate
//            // method. This one will verify the compliance
//            UserDetails user = customUserDetailsService.loadUserByUsername(req.getParameter("email"));
//            return authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            user.getUsername(), user.getPassword(), user.getAuthorities()
//                    )
//            );
//            // In any error case, return something that will be invalid
//        } else return authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        "none",
//                        "none",
//                        new ArrayList<>()
//                )
//        );
//    }
//
//    @Override
//    protected void successfulAuthentication(
//            HttpServletRequest req,
//            HttpServletResponse res,
//            FilterChain chain,
//            Authentication auth
//    ) throws IOException, ServletException {
//
//        // The Auth Mechanism stores the Username the Principal.
//        // The username is stored in the Subject field of the Token
//        String login = ((UserDetails)auth.getPrincipal()).getUsername();
//        Claims claims = Jwts.claims().setSubject(login);
//
//        long expirationTime = config.getExpirationTime();
//        //if ( login != null && login.length() > 0 ) {
//            // From the user name we can retreive the User in the DB
////            User e = customUserDetailsService.findByEmail(login);
////            ArrayList<String> Roles
////            claims.put("roles", customUserDetailsService.getAclForExistingUser(login));
//       // }
//        // Now we can generate the token
//        String token = Jwts.builder()
//                .setClaims(claims)
//                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
//                .signWith(SignatureAlgorithm.HS512, config.getSecret().getBytes())
//                .compact();
//        res.addHeader(
//                WebSecurityConfig.HEADER_STRING, // Basically "Authorization"
//                WebSecurityConfig.TOKEN_PREFIX + token // "Bearer "
//        );
//    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");



//        return authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        email,
//                        password,
//                        new ArrayList<>())
//        );
        UserDetails user = customUserDetailsService.loadUserByUsername(email);
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password,
                       user.getAuthorities()
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        ArrayList<String> roles = new ArrayList<>();
        for (GrantedAuthority role : auth.getAuthorities()){
            System.out.println(role.getAuthority());
            roles.add(role.getAuthority());
        }
        String token = Jwts.builder()
                .setSubject(auth.getName())
                .claim("roles", roles)
                .setExpiration(new Date(System.currentTimeMillis() + config.getExpirationTime()))
                .signWith(SignatureAlgorithm.HS512, config.getSecret().getBytes())
                .compact();
        res.addHeader(config.HEADER_STRING, config.TOKEN_PREFIX + token);
        PrintWriter out = res.getWriter();
        out.println(token);
    }
}
