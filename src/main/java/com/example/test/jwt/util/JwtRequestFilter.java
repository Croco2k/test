package com.example.test.jwt.util;

import com.example.test.model.entity.User;
import com.example.test.service.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl jwtUserDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    private static final String AUTH = "Authorization";
    private static final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException, ExpiredJwtException {

        final String requestTokenHeader = request.getHeader(AUTH);

        if (requestTokenHeader != null && requestTokenHeader.startsWith(BEARER)) {
            String jwtToken = requestTokenHeader.substring(7);
            try {
                String login = jwtUtils.getLoginFromToken(jwtToken);
                if (login != null) {
                    User user = this.jwtUserDetailsService.loadUserByUsername(login);

                    if (jwtUtils.validateToken(jwtToken, user)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                user, null, user.getAuthorities());
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get JWT Token");
            }
        }

        chain.doFilter(request, response);
    }
}
