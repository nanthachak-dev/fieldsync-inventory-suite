package com.example.fieldsync_inventory_backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.exception.ErrorResponse;
import com.example.fieldsync_inventory_backend.repository.auth.TokenRepository;
import com.example.fieldsync_inventory_backend.service.auth.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String username;

            // Return if authHeader is null nor not start with words "Bearer "
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Extract token from header which is after "Bearer "
            jwt = authHeader.substring(7);
            // Get username from token
            username = jwtService.extractUsername(jwt);

            // Verify username if not authenticated
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Check if user found in database (implemented in AppConfig class)
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // Check if token is valid in database
                boolean isTokenValid = tokenRepository.findByToken(jwt)
                        .map(t -> !t.isExpired() && !t.isRevoked())
                        .orElse(false);

                // If loadUserByUsername failed next code won't be executed
                // Check if token sent from frontend is valid then set as authenticated in
                // SecurityContextHolder
                // Check if token sent from frontend is valid then set as authenticated in
                // SecurityContextHolder
                if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid && userDetails.isEnabled()) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));

                    // Update security holder as authenticated
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.UNAUTHORIZED.value(),
                    "Invalid Token: " + e.getMessage());

            objectMapper.writeValue(response.getOutputStream(), errorResponse);
        }
    }
}
