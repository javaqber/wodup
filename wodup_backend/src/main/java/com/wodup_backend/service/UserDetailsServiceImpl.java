// Spring Security necesita una clase que sepa como cargar un usuario para el proceso de autenticación
package com.wodup_backend.service;

import com.wodup_backend.model.Usuario;
import com.wodup_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

        @Autowired
        private UsuarioRepository usuarioRepository;

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

                // 1. Buscar el usuario por email
                Usuario usuario = usuarioRepository.findByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "Usuario no encontrado con email: " + email));

                // 2. Devolver nuestro UserDetailsImpl personalizado
                return UserDetailsImpl.build(usuario);
        }
}