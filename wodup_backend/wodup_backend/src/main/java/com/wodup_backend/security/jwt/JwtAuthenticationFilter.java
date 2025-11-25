package com.wodup_backend.security.jwt;

import com.wodup_backend.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 1. Extraer el token de la petición
            String jwt = parseJwt(request);
            // 2. Validar el token
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // 3. Obtener el email
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                // 4. Cargar detalles del usuario
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 5. Autenticar el usuario
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Establecer el usuario en el contexto de seguridad (permite el acceso)
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            System.err.println("No se pudo establecer la autenticación del usuario: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    // Extrae el JWT del header "Authorization: Bearer <token>"
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Quitamos "Bearer "
        }
        return null;
    }
}
