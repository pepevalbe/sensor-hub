package com.pepe.sensor;

import com.pepe.sensor.controller.UserController;
import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.persistence.TemporaryToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String SENDER_EMAIL;

    @Value("${pepe-sensores.app_base_url}")
    private String APP_BASE_URL;

    private final String activateUserLinkSubject = "Creación de cuenta en pepe-sensores";
    private final String activateUserLinkTemplate = "Hola %s, por favor utiliza el siguiente link para finalizar la creación de tu cuenta: %s?email=%s&token=%s";
    private final String welcomeSubject = "Bienvenido a pepe-sensores";
    private final String welcomeTemplate = "Hola %s, ya puedes entrar a tu cuenta con tu nombre de usuario (%s) y contraseña: %s/public/login\nTu Token Personal es: %s. Utilízalo para enviar información desde tus sensores y no lo compartas.";
    private final String newPersonalTokenSubject = "Token Personal regenerado en pepe-sensores";
    private final String newPersonalTokenTemplate = "Hola %s, se ha regenerado tu Token Personal. El nuevo Token es: %s";
    private final String newPasswordLinkSubject = "Solicitud regeneración de contraseña en pepe-sensores";
    private final String newPasswordLinkTemplate = "Hola %s, por favor utiliza el siguiente link para crear una nueva contraseña: %s?email=%s&token=%s";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Async
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SENDER_EMAIL);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
        logger.info("Email sent to: " + to);
    }

    @Async
    public void sendActivateUserLinkEmail(Person user) {
        String text = String.format(activateUserLinkTemplate,
                user.getFirstName(),
                APP_BASE_URL + UserController.PUBLIC_ACTIVATEUSER_URL,
                user.getEmail(),
                user.getTemporaryToken().getToken());
        logger.info("Sending activate user link email to: " + user.getUsername() + " - " + user.getEmail());
        sendEmail(user.getEmail(), activateUserLinkSubject, text);
    }

    @Async
    public void sendWelcomeEmail(Person user) {
        String text = String.format(welcomeTemplate,
                user.getFirstName(),
                user.getUsername(),
                APP_BASE_URL,
                user.getToken());
        logger.info("Sending welcome email to: " + user.getUsername() + " - " + user.getEmail());
        sendEmail(user.getEmail(), welcomeSubject, text);
    }

    @Async
    public void sendNewPersonalTokenEmail(Person user) {
        String text = String.format(newPersonalTokenTemplate, user.getFirstName(), user.getToken());
        logger.info("Sending new personal token email to: " + user.getUsername() + " - " + user.getEmail());
        sendEmail(user.getEmail(), newPersonalTokenSubject, text);
    }

    @Async
    public void sendNewPasswordLinkEmail(TemporaryToken token) {
        Person user = token.getPerson();
        String text = String.format(newPasswordLinkTemplate, user.getFirstName(),
                APP_BASE_URL + UserController.PUBLIC_RESETPASSWORDFORM_URL,
                user.getEmail(),
                token.getToken());
        logger.info("Sending reset password link email to: " + user.getUsername() + " - " + user.getEmail());
        sendEmail(user.getEmail(), newPasswordLinkSubject, text);
    }
}
