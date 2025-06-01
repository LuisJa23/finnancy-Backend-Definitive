package finnancy.back.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

   

    @Autowired
    private JavaMailSender mailSender;

    public void sendRecoveryCode(String toEmail, String recoveryCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("🔐 Recuperación de contraseña");
        message.setText(
                "Hola,\n\n" +
                        "Hemos recibido una solicitud para restablecer tu contraseña. " +
                        "Utiliza el siguiente código para continuar con el proceso de recuperación:\n\n" +
                        "🔑 Código de recuperación: *" + recoveryCode + "*\n\n" +
                        "Si no solicitaste este cambio, puedes ignorar este mensaje.\n\n" +
                        "Atentamente,\n" +
                        "El equipo de soporte");
        mailSender.send(message);
    }

    public void sendValidationCode(String toEmail, String validationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("📧 Validación de correo electrónico");
        message.setText(
                "Hola,\n\n" +
                        "Gracias por registrarte. Para validar tu correo electrónico, utiliza el siguiente código:\n\n" +
                        "🔑 Código de validación: *" + validationCode + "*\n\n" +
                        "Si no solicitaste este registro, puedes ignorar este mensaje.\n\n" +
                        "Atentamente,\n" +
                        "El equipo de soporte");
        mailSender.send(message);
    }



}