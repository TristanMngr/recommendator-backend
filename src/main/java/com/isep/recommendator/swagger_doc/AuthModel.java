package com.isep.recommendator.swagger_doc;

import io.swagger.annotations.ApiModel;

@ApiModel(value="AuthModel", description="Sample model for the documentation of auth endpoint")
class AuthModel {
    private String token;
    private String token_expiration;
    private String token_type;
    private String full_token;

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

    public String getToken_expiration() {
        return token_expiration;
    }

    public void setToken_expiration(String token_expiration) {
        this.token_expiration = token_expiration;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }
}

