package com.olgun.bisiApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class GuvenlikYapilandirma {

    @Bean
    public SecurityFilterChain guvenlikSiralamaZinciri(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(istekler -> istekler
                        .requestMatchers("/", "/giris", "/kayit", "/ilanlar/*", "/gorseller/*").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/giris")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(cikis -> cikis
                        .logoutSuccessUrl("/")
                )
                .rememberMe(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder sifreKodlayici() {
        return new BCryptPasswordEncoder();
    }
}
