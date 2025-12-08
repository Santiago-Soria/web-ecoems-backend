package escom.ipn.mx.ecoems.domain.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Service
public class CorreoService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remitente;

    // 1. Método simple (solo texto)
    public void enviarCorreoSimple(String destinatario, String asunto, String contenido) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setFrom(remitente);
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(contenido, true); // true = acepta HTML básico

            mailSender.send(message);
            System.out.println("Correo enviado a: " + destinatario);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Error al enviar correo: " + e.getMessage());
        }
    }
}