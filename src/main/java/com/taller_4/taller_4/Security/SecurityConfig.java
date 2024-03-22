package com.taller_4.taller_4.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                // Deshabilita la protección CSRF para simplificar el ejemplo
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                // Configura el filtro de autenticación JWT antes del filtro de autenticación de usuario y contraseña
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // Permite el acceso sin autenticación a estas rutas
                                .requestMatchers(HttpMethod.POST, "/api/user/register").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/user/login").permitAll()
                                // Requiere el rol "ADMIN_ROLE" para acceder a estas rutas
                                .requestMatchers(HttpMethod.GET, "/api/user/findAll").hasAuthority("ADMIN_ROLE")
                                .requestMatchers(HttpMethod.GET, "/api/user/findById/**").hasAuthority("ADMIN_ROLE")
                                // Todas las demás solicitudes requieren autenticación
                                .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
