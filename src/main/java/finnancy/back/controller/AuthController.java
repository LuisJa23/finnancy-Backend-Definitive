package finnancy.back.controller;

import com.google.firebase.auth.FirebaseAuthException;
import finnancy.back.service.FirebaseAuthService;
import finnancy.back.service.EmailService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final FirebaseAuthService firebaseAuthService;
    private final EmailService emailService;

    public AuthController(FirebaseAuthService firebaseAuthService, EmailService emailService) {
        this.firebaseAuthService = firebaseAuthService;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    @Operation(
        summary = "Registrar usuario",
        description = "Registra un nuevo usuario proporcionando email y contraseña."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error de validación o usuario ya existe")
    })
    public ResponseEntity<String> registerUser(
            @Parameter(description = "Correo electrónico del usuario") @RequestParam String email,
            @Parameter(description = "Contraseña del usuario") @RequestParam String password
    ) throws Exception {
        try {
            String uid = firebaseAuthService.registerUser(email, password);
            return ResponseEntity.ok("User registered with UID: " + uid);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    @Operation(
        summary = "Inicia sesión de usuario",
        description = "Este endpoint permite a un usuario iniciar sesión proporcionando sus credenciales. Devuelve un token JWT si la autenticación es exitosa."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Map<String, String>> loginUser(
            @Parameter(description = "Correo electrónico del usuario") @RequestParam String email,
            @Parameter(description = "Contraseña del usuario") @RequestParam String password
    ) {
        try {
            Map<String, String> authResponse = firebaseAuthService.authenticateUser(email, password);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "User authenticated successfully");
            response.put("token", authResponse.get("token"));
            response.put("uid", authResponse.get("uid"));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Authentication failed: " + e.getMessage());
            return ResponseEntity.status(401).body(errorResponse);
        }
    }

    @PostMapping("/sendRecoveryCode")
    @Operation(summary = "Enviar código de recuperación", description = "Envía un código de recuperación al correo indicado")
    public ResponseEntity<String> sendRecoveryCode(@RequestParam String email, @RequestParam String code) {
        emailService.sendRecoveryCode(email, code);
        return ResponseEntity.ok("Código enviado correctamente a " + email);
    }

    @PostMapping("/sendValidationCode")
    @Operation(summary = "Enviar código de validación", description = "Envía un código de validación al correo indicado")
    public ResponseEntity<String> sendValidationCode(@RequestParam String email, @RequestParam String code) {
        emailService.sendValidationCode(email, code);
        return ResponseEntity.ok("Código de validación enviado correctamente a " + email);
    }

    @PostMapping("/forgotPassword")
    @Operation(summary = "Recuperar contraseña", description = "Envía un correo de recuperación de contraseña usando Firebase")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        try {
            firebaseAuthService.sendPasswordResetEmail(email);
            return ResponseEntity.ok("Correo de recuperación enviado a " + email);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al enviar el correo de recuperación: " + e.getMessage());
        }
    }
}

