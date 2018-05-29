package com.isep.recommendator.security.config;

import com.isep.recommendator.security.filter.JWTAuthenticationFilter;
import com.isep.recommendator.security.filter.JWTAuthorizationFilter;
import com.isep.recommendator.security.service.CustomUserDetailsService;
import com.isep.recommendator.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private TokenService tokenService;

    private static final String SIGN_UP_URL = TokenProperties.getSignUpUrl();
    private static final String AUTH_URL = TokenProperties.getAuthUrl();


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Create the Authentication filter
        JWTAuthenticationFilter authFilter = new JWTAuthenticationFilter(
                        authenticationManager(),
                        customUserDetailsService,
                        tokenService
        );

        // Specify the login url handler (default is /login)
        authFilter.setRequiresAuthenticationRequestMatcher( new AntPathRequestMatcher(AUTH_URL, "POST"));

        // Create the Authorization Filter
        JWTAuthorizationFilter authoFilter = new JWTAuthorizationFilter(authenticationManager());

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

    @Bean
    // configuring cross origin for our front end
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://127.0.0.1:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}