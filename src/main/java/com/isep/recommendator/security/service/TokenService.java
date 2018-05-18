package com.isep.recommendator.security.service;

import com.isep.recommendator.app.model.User;
import com.isep.recommendator.security.config.TokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import com.isep.recommendator.app.custom_object.TokenResponseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Service
public class TokenService {

    private static final String SECRET = TokenProperties.getSecret();
    private static final long EXPIRATION_TIME = TokenProperties.getExpirationTime();
    private static final String TOKEN_PREFIX = TokenProperties.getTokenPrefix();

    public static String buildToken(String username, ArrayList<String> roles){
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
    }

    public static String buildToken(User user){
        ArrayList<String> roles = new ArrayList<>();
        roles.add("USER");
        if (user.isAdmin())
            roles.add("ADMIN");

        return buildToken(user.getUsername(), roles);
    }

    public static Claims parseToken(String token){
        return Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody();
    }

    public static ArrayList<SimpleGrantedAuthority> getRolesFromClaims(Claims claims){
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        ArrayList<String> roles = (ArrayList<String>) claims.get("roles");
        if (roles != null) {
            for (String role : roles) {
                SimpleGrantedAuthority auth = new SimpleGrantedAuthority(role);
                authorities.add(auth);
            }
        }
        return authorities;
    }


    public static ArrayList<String> getRolesList(Authentication auth){
        ArrayList<String> roles = new ArrayList<>();
        for (GrantedAuthority role : auth.getAuthorities()){
            roles.add(role.getAuthority());
        }
        return roles;
    }

    public static TokenResponseObject getSuccessResponse(String token){
//        HashMap<String, Object> resp = new HashMap<>();
//        resp.put("token", token);
//        resp.put("token_type", TOKEN_PREFIX);
//        resp.put("token_expiration", EXPIRATION_TIME);
//        resp.put("full_token", TOKEN_PREFIX + token);
        return new TokenResponseObject(token, EXPIRATION_TIME, TOKEN_PREFIX, TOKEN_PREFIX + token);
    }

    public static HashMap<String, Object> getBadCredentialsResponse(String msg){
        HashMap<String, Object> resp = new HashMap<>();
        resp.put("status", 401);
        resp.put("error", "Bad Credentials");
        resp.put("message", msg);
        return resp;
    }


}
