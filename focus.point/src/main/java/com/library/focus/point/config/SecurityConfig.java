package com.library.focus.point.config;

import com.library.focus.point.service.CustomUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    LoginSuccessHandler successHandler;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http

            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                        "/",
                        "/register-page",
                        "/register",
                        "/login-page",
                        "/api/resend-otp",
                        "/send-message",
                        "/api/send-otp",
                        "/api/verify-otp",
                        "/css/**",
                        "/js/**"
                ).permitAll()

                .requestMatchers("/admin/**")
                .hasRole("ADMIN")

                .anyRequest()
                .authenticated()
            )

            .formLogin(form -> form

            	    .loginPage("/login-page")

            	    .loginProcessingUrl("/login")

            	    .successHandler(successHandler)

            	    .failureUrl("/login-page?error=true")

            	    .permitAll()

            	)

            	.sessionManagement(session -> session

            	    .invalidSessionUrl(
            	        "/"
            	    )
            	)
            .logout(logout -> logout

                .logoutUrl("/logout")

                .logoutSuccessUrl("/")

                .permitAll()
            );

        return http.build();
    }
}