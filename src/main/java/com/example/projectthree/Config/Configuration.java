package com.example.projectthree.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@org.springframework.context.annotation.Configuration
@EnableWebSecurity
public class Configuration {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests( auth -> auth
                        .requestMatchers("/api/v1/customer/register").permitAll()
                        .requestMatchers("/api/v1/customer/update/**", "/api/v1/customer/delete/**").hasAnyAuthority("CUSTOMER", "ADMIN")
                        .requestMatchers("/api/v1/auth/get-all", "/api/v1/auth/get/**",
                                "/api/v1/customer/get-all", "/api/v1/customer/get/**",
                                "/api/v1/employee/register",
                                "/api/v1/employee/get-all", "/api/v1/employee/get/**",
                                "/api/v1/employee/update/**", "/api/v1/employee/delete/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/account/add").hasAuthority("CUSTOMER")
                        .requestMatchers("/api/v1/account/deposit/**", "/api/v1/account/withdraw/**", "/api/v1/account/transfer/**").hasAuthority("CUSTOMER")
                        .requestMatchers("/api/v1/account/get-all").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/v1/account/activate/**", "/api/v1/account/block/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/v1/account/update/**", "/api/v1/account/delete/**").hasAnyAuthority("CUSTOMER", "ADMIN")
                        .requestMatchers("/api/v1/account/customer/**").hasAnyAuthority("CUSTOMER", "ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/v1/account/get/**").hasAnyAuthority("CUSTOMER", "ADMIN", "EMPLOYEE")
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .invalidateHttpSession(true)
                )
                .httpBasic( basic -> {})
                .build();


    }
}
