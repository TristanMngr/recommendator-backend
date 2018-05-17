package com.isep.recommendator.swagger_doc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.isep.recommendator.security.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;

@RestController
@Api(description="Authentication Endpoint")
public class DocController {

    @Autowired
    TokenService tokenService;
    // this route is never called, but used for Swagger documentation
    @PostMapping("/users/auth")
    @ApiOperation(value = "Get a JWT token (login) [PUBLIC]", response=AuthModel.class)
    public HashMap<String, Object> auth(@RequestParam("username") String username, @RequestParam("password") String password) throws JsonProcessingException {
        return tokenService.getSuccessResponse("exempletoken");
    }

    @GetMapping("/")
    @ApiOperation(value = "redirection de l'url '/' sur la documentation")
    public RedirectView redirectFromRootToDoc() {
        return new RedirectView("/swagger-ui.html");
    }
}