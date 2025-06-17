

package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.Funcionario;
import com.agendamientos.agendamientosTurnos.security.JwtUtil;
import com.agendamientos.agendamientosTurnos.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication .*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation .*;

import java.util.HashMap;
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
        public ResponseEntity<?> authenticateUser(@RequestBody Funcionario loginRequest) {
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getCorreo(),
                                loginRequest.getContrasena()
                        )
                );

                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String token = jwtUtils.generateToken(userDetails.getUsername());

                // Obtener el funcionario para incluir el nombre en la respuesta
                Funcionario funcionario = funcionarioRepository.findByCorreo(userDetails.getUsername());

                if (funcionario != null) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("token", token);
                    response.put("nombre", funcionario.getNombre());
                    response.put("apellido", funcionario.getApellido());
                    response.put("correo", funcionario.getCorreo());
                    response.put("requiereActualizacion", funcionario.getEsContrasenaPorDefecto());

                    if (funcionario.getIdRol() != null) {
                        // Asumiendo que tu entidad 'Rol' tiene un campo 'id'
                        response.put("idRol", funcionario.getIdRol());
                    } else {
                        // Manejar el caso si el rol es nulo (ej. asignar un valor por defecto o un error)
                        response.put("idRol", null); // O podrías lanzar una excepción si siempre se espera un rol
                    }

                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener la información del usuario.");
                }

            } catch (AuthenticationException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas.");
            }
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
        public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> request) {
            String correo = request.get("correo");
            String nuevaContrasena = request.get("nuevaContrasena");

            Funcionario funcionario = funcionarioRepository.findByCorreo(correo);
            if (funcionario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Usuario no encontrado.");
            }

            funcionario.setContrasena(encoder.encode(nuevaContrasena));
            funcionario.setEsContrasenaPorDefecto(false); // Marcamos que la contraseña ya no es la por defecto
            funcionarioRepository.save(funcionario);

            return ResponseEntity.ok("Contraseña actualizada correctamente.");
        }
    }

