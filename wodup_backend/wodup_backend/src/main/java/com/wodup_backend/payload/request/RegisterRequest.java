package com.wodup_backend.payload.request;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

@Data
public class RegisterRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String nombre;

    @NotBlank
    @Size(min = 3, max = 150)
    private String apellido;

    @NotBlank
    @Size(max = 150)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    // Si no se proporciona, el servicio asignará el rol por defecto (ATHLETE)
    private Set<String> rol;
}
