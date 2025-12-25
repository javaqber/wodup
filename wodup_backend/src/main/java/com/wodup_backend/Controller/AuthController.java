package com.wodup_backend.Controller;

import com.wodup_backend.model.Usuario;
import com.wodup_backend.payload.request.LoginRequest;
import com.wodup_backend.payload.request.RegisterRequest;
import com.wodup_backend.payload.response.JwtResponse;
import com.wodup_backend.payload.response.MessageResponse;
import com.wodup_backend.repository.UsuarioRepository;
import com.wodup_backend.security.jwt.JwtUtils;
import com.wodup_backend.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600) // CORS para Angular
@RestController
@RequestMapping("/api/auth")
public class AuthController {

        private final AuthService authService;
        private final AuthenticationManager authenticationManager;
        private final JwtUtils jwtUtils;
        private final UsuarioRepository usuarioRepository;

        // Inyección por constructor (incluyendo el repositorio)
        public AuthController(
                        AuthService authService,
                        AuthenticationManager authenticationManager,
                        JwtUtils jwtUtils,
                        UsuarioRepository usuarioRepository // Inyección del repositorio
        ) {
                this.authService = authService;
                this.authenticationManager = authenticationManager;
                this.jwtUtils = jwtUtils;
                this.usuarioRepository = usuarioRepository;
        }

        @PostMapping("/login")
        public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
                try {
                        // 1. Autenticar las credenciales
                        Authentication authentication = authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                                                        loginRequest.getPassword()));

                        // 2. Establecer la autenticación en el contexto de seguridad
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        // 3. Generar el JWT
                        String jwt = jwtUtils.generateJwtToken(authentication);

                        // 4. Obtener los detalles del usuario de Spring Security (solo tiene email,
                        // password, roles)
                        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                        // 5. OBTENER EL OBJETO USUARIO REAL DE LA BASE DE DATOS
                        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
                                        .orElseThrow(() -> new UsernameNotFoundException(
                                                        "Usuario no encontrado después de la autenticación."));

                        // 6. Determinar el rol principal para la respuesta
                        String rol = userDetails.getAuthorities().stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .findFirst()
                                        .orElse("ROLE_ATHLETE");

                        // 7. Devolver el token y los datos completos del usuario
                        return ResponseEntity.ok(new JwtResponse(
                                        jwt,
                                        usuario.getId(),
                                        usuario.getEmail(),
                                        usuario.getNombre(),
                                        usuario.getApellido(),
                                        rol));

                } catch (AuthenticationException e) {
                        // Captura cualquier fallo de autenticación (credenciales incorrectas)
                        return ResponseEntity
                                        .status(HttpStatus.UNAUTHORIZED)
                                        .body(new MessageResponse(
                                                        "Error de autenticación: Correo electrónico o contraseña incorrectos."));
                }
        }

        @PostMapping("/register")
        public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
                try {
                        authService.registerNewUser(registerRequest);
                        return ResponseEntity.ok(new MessageResponse("Usuario Atleta registrado exitosamente."));
                } catch (ResponseStatusException ex) {
                        return ResponseEntity.status(ex.getStatusCode())
                                        .body(new MessageResponse(ex.getReason()));
                }
        }
}