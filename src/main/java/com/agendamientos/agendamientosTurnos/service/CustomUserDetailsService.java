package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.entity.Funcionario;
import com.agendamientos.agendamientosTurnos.entity.Rol;
import com.agendamientos.agendamientosTurnos.repository.FuncionarioRepository;
import com.agendamientos.agendamientosTurnos.repository.RolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        logger.info("Intentando autenticar usuario con correo: {}", correo);

        Funcionario funcionario = funcionarioRepository.findByCorreo(correo);
        if (funcionario == null) {
            logger.warn("Usuario no encontrado con correo: {}", correo);
            throw new UsernameNotFoundException("User Not Found with correo: " + correo);
        }

        logger.info("Usuario encontrado: {}", funcionario);
        logger.info("idRol del usuario: {}", funcionario.getIdRol());

        Optional<Rol> rolOptional = rolRepository.findById(funcionario.getIdRol());
        if (rolOptional.isEmpty()) {
            logger.warn("Rol no encontrado para idRol: {}", funcionario.getIdRol());
            throw new UsernameNotFoundException("Rol Not Found for funcionario with correo: " + correo);
        }

        Rol rol = rolOptional.get();
        logger.info("Rol encontrado: {}", rol);

        return new org.springframework.security.core.userdetails.User(
                funcionario.getCorreo(),
                funcionario.getContrasena(),
                Collections.singletonList(new SimpleGrantedAuthority(rol.getNombre()))
        );
    }
}