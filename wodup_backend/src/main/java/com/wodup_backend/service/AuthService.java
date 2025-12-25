package com.wodup_backend.service;

import com.wodup_backend.model.Rol;
import com.wodup_backend.model.Usuario;
import com.wodup_backend.payload.request.RegisterRequest;
import com.wodup_backend.repository.RolRepository;
import com.wodup_backend.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository; // Debes tener un repositorio de roles
    private final PasswordEncoder encoder;

    public AuthService(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder encoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.encoder = encoder;
    }

    public void registerNewUser(RegisterRequest registerRequest) {
        // 1. Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error: El email ya está en uso.");
        }

        // 2. Crear el nuevo Usuario
        Usuario usuario = new Usuario(
                registerRequest.getNombre(),
                registerRequest.getApellido(),
                registerRequest.getEmail(),
                encoder.encode(registerRequest.getPassword()),
                LocalDate.now(),
                true // activo por defecto
        );

        // 3. Determinar y asignar los Roles
        Set<String> strRoles = registerRequest.getRol();
        Set<Rol> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            // Asigna el rol por defecto (ATHLETE)
            Rol athleteRole = rolRepository.findByNombre("ROLE_ATHLETE") // Usa el string del rol
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Error: El Rol 'ROLE_ATHLETE' no fue encontrado en la DB."));
            roles.add(athleteRole);
        } else {
            // Asigna los roles especificados por el usuario
            strRoles.forEach(rol -> {
                // Convertimos el rol de entrada al formato DB
                String finalRoleName;
                switch (rol.toUpperCase()) {
                    case "COACH":
                        finalRoleName = "ROLE_COACH";
                        break;
                    case "ADMIN":
                        finalRoleName = "ROLE_ADMIN";
                        break;
                    default:
                        // Si se envía un rol desconocido, por defecto es ATHLETE
                        finalRoleName = "ROLE_ATHLETE";
                }

                Rol assignedRole = rolRepository.findByNombre(finalRoleName)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Error: El Rol '" + finalRoleName + "' no fue encontrado en la DB."));
                roles.add(assignedRole);
            });
        }

        usuario.setRoles(roles);

        // 4. Guardar el usuario
        usuarioRepository.save(usuario);
    }
}
