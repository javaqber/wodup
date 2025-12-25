package com.wodup_backend.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        // Envía una respuesta 401 (Unauthorized) al cliente
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: No autorizado. Requiere JWT.");
    }
}