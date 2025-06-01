
package finnancy.back;

import finnancy.back.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import static org.mockito.Mockito.*;

class EmailServiceTest {
    @Mock
    JavaMailSender mailSender;
    @InjectMocks
    EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendRecoveryCode() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        emailService.sendRecoveryCode("test@mail.com", "123456");
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}
