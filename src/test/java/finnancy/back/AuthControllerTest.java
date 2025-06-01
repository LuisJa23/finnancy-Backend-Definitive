
package finnancy.back;

import finnancy.back.service.FirebaseAuthService;
import finnancy.back.service.EmailService;
import finnancy.back.controller.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {
    @Mock
    FirebaseAuthService firebaseAuthService;
    @Mock
    EmailService emailService;
    @InjectMocks
    AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() throws Exception {
        when(firebaseAuthService.registerUser(anyString(), anyString())).thenReturn("uid123");
        ResponseEntity<String> response = authController.registerUser("test@mail.com", "pass");
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("uid123"));
    }

    @Test
    void testLoginUserSuccess() throws Exception {
        Map<String, String> authResponse = Map.of("token", "abc", "uid", "uid123");
        when(firebaseAuthService.authenticateUser(anyString(), anyString())).thenReturn(authResponse);
        ResponseEntity<Map<String, String>> response = authController.loginUser("test@mail.com", "pass");
        assertEquals("success", response.getBody().get("status"));
        assertEquals("uid123", response.getBody().get("uid"));
    }
}
