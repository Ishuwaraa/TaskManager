package com.treinetic.TaskManager.config;

import com.treinetic.TaskManager.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    public JWTFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        //checking token exists
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            //getting access token and extracting user email
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwt, false);

            //context is request scoped and this gets cleared after each req
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (userEmail != null && authentication == null) {
                UserDetails userDetails = jwtService.extractUserDetails(jwt, false);

                //if jwt is valid, create the auth token and setting it in the security context
                //to access auth details in other code parts
                //no user validation w the db happens here. validity of the token only
                //checking user w the db in the refresh token end point only to reduce server load
                if (jwtService.isTokenValid(jwt, userDetails, false)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            //telling spring security to move on w the rest of the filters
            filterChain.doFilter(request, response);
        } catch (ResponseStatusException e) {
            //sending response here cuz throwing an error here will be override by other filters and sends 401
            response.setStatus(e.getStatusCode().value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + e.getReason() + "\"}");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
