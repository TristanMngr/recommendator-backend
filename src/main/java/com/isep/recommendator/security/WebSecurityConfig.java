package com.isep.recommendator.security;

import com.isep.recommendator.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String SECRET = "BRUH";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/register";

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private WebSecurityConfig webSecurityConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Create the Authentication filter
        JWTAuthenticationFilter authFilter =
                new JWTAuthenticationFilter(
                        authenticationManager(),
                        customUserDetailsService,
                        webSecurityConfig
                );

        // Specify the login url handler (default is /login)
        // this line allows to change it.
        authFilter.setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher("/users/auth", "POST")
        );

        // Create the Authorization Filter
        JWTAuthorizationFilter authoFilter =
                new JWTAuthorizationFilter(
                        authenticationManager()
                );

        // Inject the configuration Class into the Filter
        authoFilter.setWebSecurityConfig(webSecurityConfig);

        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET).permitAll()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(authFilter)
                .addFilter(authoFilter)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception
    {
        auth.userDetailsService(customUserDetailsService);
    }

    public static String getSecret() {
        return SECRET;
    }

    public static long getExpirationTime(){
        return EXPIRATION_TIME;
    }
}