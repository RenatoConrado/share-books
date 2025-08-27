package com.renatoconrado.share_books.security.jwt;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
        @Nonnull HttpServletRequest request,
        @Nonnull HttpServletResponse response,
        @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String email;
        String jwt = authHeader.substring(7);
        try {
            email = this.jwtService.extractUsername(jwt);
        }
        catch (Exception e) {
            log.warn("Erro ao extrair usuário do token JWT", e);
            filterChain.doFilter(request, response);
            return;
        }

        var context = SecurityContextHolder.getContext();
        boolean logged = (email == null) || (context.getAuthentication() != null);
        if (logged) {
            filterChain.doFilter(request, response);
            return;
        }

        var userDetails = this.userDetailsService.loadUserByUsername(email);

        if (this.jwtService.isTokenValid(jwt, userDetails)) {
            var authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
            );
            authenticationToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
            );

            context.setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
