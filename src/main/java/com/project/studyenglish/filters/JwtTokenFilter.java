package com.project.studyenglish.filters;

import com.project.studyenglish.components.JwtTokenUtil;
import com.project.studyenglish.models.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${api.prefix}")
    private String apiPrefix;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if(isBypassToken(request)) {
                filterChain.doFilter(request, response); //enable bypass
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
            final String token = authHeader.substring(7);
            final String email = jwtTokenUtil.extractEmail(token);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserEntity userDetails = (UserEntity) userDetailsService.loadUserByUsername(email);
                if(jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        }catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }
    private boolean isBypassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/search", apiPrefix), "GET"),
                Pair.of(String.format("%s/user/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/user/login", apiPrefix), "POST"),
                //category pass
                Pair.of(String.format("%s/category/vocabulary", apiPrefix), "GET"),
                Pair.of(String.format("%s/category/product", apiPrefix), "GET"),
                Pair.of(String.format("%s/category/exam", apiPrefix), "GET"),
                Pair.of(String.format("%s/category/grammar", apiPrefix), "GET"),
                Pair.of(String.format("%s/category/**", apiPrefix), "GET"),
                //vocabulary pass
                Pair.of(String.format("%s/vocabulary", apiPrefix), "GET"),
                //product
                Pair.of(String.format("%s/product", apiPrefix), "GET"),
                //exam
                Pair.of(String.format("%s/exam", apiPrefix), "GET"),
                //grammar
                Pair.of(String.format("%s/grammar", apiPrefix), "GET"),
                Pair.of(String.format("%s/grammar/random", apiPrefix), "GET"),
                //confirm account
                Pair.of(String.format("%s/user/activation", apiPrefix), "POST"),
                //comment
                Pair.of(String.format("%s/product-rating/**", apiPrefix), "GET"),
                Pair.of(String.format("%s/product-rating/**", apiPrefix), "POST"),
                //auth google
                Pair.of(String.format("%s/user/outbound/authentication", apiPrefix), "POST")
        );
        for(Pair<String, String> bypassToken: bypassTokens) {
            if (request.getServletPath().contains(bypassToken.getFirst()) &&
                    request.getMethod().equals(bypassToken.getSecond())) {
                return true;
            }
        }
        return false;
    }
}
