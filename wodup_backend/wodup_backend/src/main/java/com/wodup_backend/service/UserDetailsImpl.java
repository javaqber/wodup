package com.wodup_backend.service;

import com.wodup_backend.model.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Clase que implementa UserDetails para almacenar información adicional
 * del usuario, como el ID (Long), que es crucial para enlazar la clase
 * con el Coach que la creó.
 */
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String email;

    // @JsonIgnore para evitar exponer la contraseña en la respuesta JSON (si se
    // serializara)
    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String email, String password,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    // Método estático para construir UserDetailsImpl a partir de la entidad Usuario
    public static UserDetailsImpl build(Usuario usuario) {
        List<GrantedAuthority> authorities = usuario.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getNombre()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getPassword(),
                authorities);
    }

    // --- Getters requeridos por Spring Security ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email; // Usamos el email como nombre de usuario
    }

    // --- Getter CUSTOM para el ID del usuario ---
    public Long getId() {
        return id;
    }

    // Estos métodos controlan el estado de la cuenta
    @Override
    public boolean isAccountNonExpired() {
        return true; // Podrías usar usuario.isActivo() si lo deseas
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}