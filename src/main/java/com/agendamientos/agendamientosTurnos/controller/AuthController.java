

package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.security.JwtUtil;
import com.agendamientos.agendamientosTurnos.entity.Funcionario;
import com.agendamientos.agendamientosTurnos.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication .*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation .*;

import java.util.Map;

    @RestController
    @RequestMapping("/api/auth")
    public class AuthController {

        @Autowired
        AuthenticationManager authenticationManager;

        @Autowired
        FuncionarioRepository funcionarioRepository;

        @Autowired
        PasswordEncoder encoder;

        @Autowired
        JwtUtil jwtUtils;

        @PostMapping("/signin")
        public String authenticateUser(@RequestBody Funcionario funcionario) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            funcionario.getCorreo(),
                            funcionario.getContrasena()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return jwtUtils.generateToken(userDetails.getUsername());
        }

        @PostMapping("/signup")
        public String registerUser(@RequestBody Funcionario funcionario) {
            if (funcionarioRepository.existsByCorreo(funcionario.getCorreo())) {
                return "Error: El correo ya está en uso!";
            }

            // Crear nuevo usuario con contraseña encriptada
            Funcionario nuevoFuncionario = new Funcionario();
            nuevoFuncionario.setNombre(funcionario.getNombre());
            nuevoFuncionario.setApellido(funcionario.getApellido());
            nuevoFuncionario.setCorreo(funcionario.getCorreo());
            nuevoFuncionario.setCedula(funcionario.getCedula());
            nuevoFuncionario.setTelefono(funcionario.getTelefono());
            nuevoFuncionario.setContrasena(encoder.encode(funcionario.getContrasena()));

            funcionarioRepository.save(nuevoFuncionario);
            return "Usuario registrado exitosamente!";
        }

        @PutMapping("/update-password")
        public String updatePassword(@RequestBody Map<String, String> request) {
            String correo = request.get("correo");
            String nuevaContrasena = request.get("nuevaContrasena");

            Funcionario funcionario = funcionarioRepository.findByCorreo(correo);
            if (funcionario == null) {
                return "Error: Usuario no encontrado.";
            }

            // Encriptar la nueva contraseña antes de guardarla
            funcionario.setContrasena(encoder.encode(nuevaContrasena));
            funcionarioRepository.save(funcionario);

            return "Contraseña actualizada correctamente.";
        }
    }

