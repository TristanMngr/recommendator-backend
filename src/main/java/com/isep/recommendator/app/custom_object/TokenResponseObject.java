package com.isep.recommendator.app.custom_object;

import io.swagger.annotations.ApiModel;

@ApiModel(value="TokenResponseObject", description="Sample model for the documentation of auth endpoint")
public class TokenResponseObject {
    private String token;
    private Long token_expiration;
    private String token_type;
    private String full_token;

    public TokenResponseObject(String token, Long token_expiration, String token_type, String full_token){
        this.token = token;
        this.token_expiration = token_expiration;
        this.token_type = token_type;
        this.full_token = full_token;
    }

    public String getFull_token() {
        return full_token;
    }

    public void setFull_token(String full_token) {
        this.full_token = full_token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getToken_expiration() {
        return token_expiration;
    }

    public void setToken_expiration(Long token_expiration) {
        this.token_expiration = token_expiration;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }
}

