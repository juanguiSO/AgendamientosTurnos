package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.entity.Funcionario;
import com.agendamientos.agendamientosTurnos.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Funcionario funcionario = funcionarioRepository.findByCorreo(correo);
        if (funcionario == null) {
            throw new UsernameNotFoundException("User Not Found with correo: " + correo);
        }

        return new org.springframework.security.core.userdetails.User(
                funcionario.getCorreo(),
                funcionario.getContrasena(),
                funcionario.getRoles().stream()
                        .map(rol -> new SimpleGrantedAuthority(rol.getName()))
                        .collect(Collectors.toList())
        );
    }
}
