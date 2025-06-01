package finnancy.back.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.HashMap;

@Service
public class FirebaseAuthService {

    private static final String FIREBASE_AUTH_LOGIN_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyC6u-nUU113shojID3w4DcxBWSNaw0C7H4";
    private static final String FIREBASE_AUTH_REGISTER_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=AIzaSyC6u-nUU113shojID3w4DcxBWSNaw0C7H4";

    // Clase interna para representar la respuesta de autenticación
    public static class AuthResponse {
        private String token;
        private String uid;

        public AuthResponse(String token, String uid) {
            this.token = token;
            this.uid = uid;
        }

        // Getters
        public String getToken() {
            return token;
        }

        public String getUid() {
            return uid;
        }
    }

    // Modified to return both UID and token
    public Map<String, String> authenticateUser(String email, String password) throws Exception {
        // Create the request body
        Map<String, String> requestBody = Map.of(
                "email", email,
                "password", password,
                "returnSecureToken", "true"
        );

        // Configure the HTTP request
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        // Send the request to Firebase REST API
        ResponseEntity<Map> response = restTemplate.exchange(
                FIREBASE_AUTH_LOGIN_URL,
                HttpMethod.POST,
                request,
                Map.class
        );

        // Validate the response
        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("idToken") && responseBody.containsKey("localId")) {
                Map<String, String> authResponse = new HashMap<>();
                authResponse.put("token", (String) responseBody.get("idToken"));
                authResponse.put("uid", (String) responseBody.get("localId"));
                return authResponse;
            }
        }

        throw new Exception("Invalid email or password");
    }

    // Resto de los métodos permanecen igual...
    public String registerUser(String email, String password) throws Exception {
        // Create the request body
        Map<String, String> requestBody = Map.of(
                "email", email,
                "password", password,
                "returnSecureToken", "true"
        );

        // Configure the HTTP request
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        // Send the request to Firebase REST API (REGISTER URL)
        ResponseEntity<Map> response = restTemplate.exchange(
                FIREBASE_AUTH_REGISTER_URL,
                HttpMethod.POST,
                request,
                Map.class
        );

        // Validate the response
        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("localId")) {
                return (String) responseBody.get("localId");
            }
        }

        throw new Exception("Registration failed");
    }

    public Object getUserById(String uid) throws Exception {
        // Create the request URL
        String url = "https://identitytoolkit.googleapis.com/v1/accounts:lookup?key=AIzaSyC6u-nUU113shojID3w4DcxBWSNaw0C7H4";

        // Create the request body
        Map<String, String> requestBody = Map.of(
                "idToken", uid
        );

        // Configure the HTTP request
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        // Send the request to Firebase REST API
        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Map.class
        );

        // Validate the response
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }

        throw new Exception("User not found");
    }

    public void changePassword(String idToken, String newPassword) throws Exception {
        String url = "https://identitytoolkit.googleapis.com/v1/accounts:update?key=AIzaSyC6u-nUU113shojID3w4DcxBWSNaw0C7H4";
        Map<String, Object> requestBody = Map.of(
                "idToken", idToken,
                "password", newPassword,
                "returnSecureToken", true
        );
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Map.class
        );
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("No se pudo cambiar la contraseña");
        }
    }

    public void sendPasswordResetEmail(String email) throws Exception {
        String url = "https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key=AIzaSyC6u-nUU113shojID3w4DcxBWSNaw0C7H4";
        Map<String, Object> requestBody = Map.of(
                "requestType", "PASSWORD_RESET",
                "email", email
        );
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Map.class
        );
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("No se pudo enviar el correo de recuperación de contraseña");
        }
    }




}