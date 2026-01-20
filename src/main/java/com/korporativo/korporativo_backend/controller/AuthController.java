package com.korporativo.korporativo_backend.controller;

import com.korporativo.korporativo_backend.dto.LoginRequestDTO;
import com.korporativo.korporativo_backend.dto.LoginResponseDTO;
import com.korporativo.korporativo_backend.model.User;
import com.korporativo.korporativo_backend.repository.UserRepository;
import com.korporativo.korporativo_backend.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de autenticación JWT
 * Endpoints: POST /auth/login
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class AuthController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                        JwtTokenProvider jwtTokenProvider,
                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Login endpoint que valida credenciales y devuelve JWT token
     * POST /auth/login
     *
     * Request:
     * {
     *   "username": "admin",
     *   "password": "admin123"
     * }
     *
     * Response:
     * {
     *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     *   "type": "Bearer",
     *   "username": "admin",
     *   "role": "ADMIN"
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        // Buscar usuario por username
        User user = userRepository.findByUsername(loginRequest.getUsername())
            .orElse(null);

        if (user == null) {
            return ResponseEntity.status(401)
                .body("Usuario o contraseña incorrectos");
        }

        // Validar contraseña
        // En desarrollo usamos NoOp, en producción será BCrypt
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401)
                .body("Usuario o contraseña incorrectos");
        }

        // Generar JWT token
        String token = jwtTokenProvider.generateToken(
            user.getUsername(),
            user.getRole().name().replace("ROLE_", "")
        );

        // Devolver response con token
        LoginResponseDTO response = new LoginResponseDTO(
            token,
            user.getUsername(),
            user.getRole().name().replace("ROLE_", "")
        );

        return ResponseEntity.ok(response);
    }
}
