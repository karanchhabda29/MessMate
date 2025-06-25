package com.springboot.MessApplication.MessMate.filters;

import com.springboot.MessApplication.MessMate.entities.User;
import com.springboot.MessApplication.MessMate.services.JwtService;
import com.springboot.MessApplication.MessMate.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorizationHeader.split("Bearer ")[1];
        Long userId = jwtService.getUserIdFromToken(token);

        if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userService.getUserById(userId);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, null);

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
