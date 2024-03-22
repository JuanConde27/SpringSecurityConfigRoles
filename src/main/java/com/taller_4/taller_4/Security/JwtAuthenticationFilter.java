package com.taller_4.taller_4.Security;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JsonWebToken jsonWebToken;
    private final CustomUserDetailsService userDetailsService;


    public JwtAuthenticationFilter(JsonWebToken jsonWebToken, CustomUserDetailsService userDetailsService) {
        this.jsonWebToken = jsonWebToken;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(request);
            if (token != null && jsonWebToken.validateJwtToken(token)) {
                Long id = jsonWebToken.getIdFromJwtToken(token);
                UserDetails userDetails = userDetailsService.loadUserById(id); // Cargar detalles del usuario utilizando CustomUserDetailsService
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("Usuario autenticado: " + SecurityContextHolder.getContext().getAuthentication());
                System.out.println("Usuario autenticado: " + userDetails.getAuthorities());
            }
        } catch (Exception e) {
            logger.error("No se puede establecer la autenticación del usuario: ", e);
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null) {
            return header;
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/api/user/register") || request.getServletPath().equals("/api/user/login");
    }
}
