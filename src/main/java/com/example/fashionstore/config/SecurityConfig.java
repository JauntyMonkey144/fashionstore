package com.example.fashionstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1) 
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        HttpSessionSecurityContextRepository adminContextRepo = new HttpSessionSecurityContextRepository();
        adminContextRepo.setSpringSecurityContextKey("ADMIN_SECURITY_CONTEXT");

        http
            .securityMatcher("/admin/**") 
            .securityContext(context -> context.securityContextRepository(adminContextRepo))
            .csrf(csrf -> csrf.disable())
            // XÓA DÒNG requiresChannel Ở ĐÂY
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/login").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
            )
            .formLogin(form -> form
                .loginPage("/admin/login")              
                .defaultSuccessUrl("/admin/home", true) 
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/admin/logout")             
                .logoutSuccessUrl("/admin/login?logout=true") 
                .permitAll()
            );

        return http.build();
    }

    @Bean
    @Order(2) 
    public SecurityFilterChain userFilterChain(HttpSecurity http) throws Exception {
        HttpSessionSecurityContextRepository userContextRepo = new HttpSessionSecurityContextRepository();
        userContextRepo.setSpringSecurityContextKey("USER_SECURITY_CONTEXT");

        http
            .securityContext(context -> context.securityContextRepository(userContextRepo))
            .csrf(csrf -> csrf.disable())
            // XÓA DÒNG requiresChannel Ở ĐÂY
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/profile/**", "/checkout/**").authenticated() 
                .anyRequest().permitAll() 
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/home")
                .permitAll()
            );

        return http.build();
    }
}
