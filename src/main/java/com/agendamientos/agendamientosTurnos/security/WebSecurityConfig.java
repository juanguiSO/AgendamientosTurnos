package com.agendamientos.agendamientosTurnos.security;

import com.agendamientos.agendamientosTurnos.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig {

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
       return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF
                .cors(cors -> cors.configure(http)) // Configurar CORS si es necesario
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configuración sin estado
                                .maximumSessions(1) // Número máximo de sesiones (opcional, considera si es necesario con JWT stateless)
                                .expiredUrl("/login") // Redirige en caso de expiración (considera si es relevante con JWT stateless)
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // Rutas existentes que ya permitías
                                .requestMatchers("/api/auth/**", "/api/test/all").permitAll()
                                // *****************************************************************
                                // ************** NUEVAS RUTAS PARA SWAGGER UI Y OPENAPI **********
                                // *****************************************************************
                                .requestMatchers(
                                        "/swagger-ui.html",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**", // Esta ruta es para la especificación JSON
                                        "/v3/api-docs.yaml", // Esta ruta es para la especificación YAML
                                        "/webjars/**"        // Necesario para cargar los recursos estáticos de Swagger UI
                                ).permitAll()
                                // *****************************************************************
                                // ************** FIN DE LA CONFIGURACIÓN DE SWAGGER UI ***********
                                // *****************************************************************
                                .anyRequest().authenticated() // Todas las demás solicitudes requieren autenticación
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout") // URL de cierre de sesión
                                .invalidateHttpSession(true) // Invalidar sesión HTTP (considera si es relevante con JWT stateless)
                                .deleteCookies("JSESSIONID") // Borrar cookies al cerrar sesión (considera si es relevante con JWT stateless)
                );

        // Agregar el filtro JWT antes del UsernamePasswordAuthenticationFilter
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}