package com.pepe.sensor;

import com.pepe.sensor.controller.UserController;
import com.pepe.sensor.persistence.Person;
import com.pepe.sensor.persistence.TemporaryToken;
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

    @Async
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Async
    public void sendActivateUserLinkEmail(Person user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(activateUserLinkSubject);
        String text = String.format(activateUserLinkTemplate, user.getFirstName(),
                APP_BASE_URL + UserController.PUBLIC_ACTIVATEUSER_URL, user.getEmail(), user.getTemporaryToken().getToken());
        message.setText(text);
        javaMailSender.send(message);
    }

    @Async
    public void sendWelcomeEmail(Person user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(welcomeSubject);
        String text = String.format(welcomeTemplate, user.getFirstName(), user.getUsername(), APP_BASE_URL, user.getToken());
        message.setText(text);
        javaMailSender.send(message);
    }

    @Async
    public void sendNewPersonalTokenEmail(Person user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(newPersonalTokenSubject);
        message.setText(String.format(newPersonalTokenTemplate, user.getFirstName(), user.getToken()));
        javaMailSender.send(message);
    }

    @Async
    public void sendNewPasswordLinkEmail(TemporaryToken token) {
        SimpleMailMessage message = new SimpleMailMessage();
        Person user = token.getPerson();
        message.setTo(user.getEmail());
        message.setSubject(newPasswordLinkSubject);
        message.setText(String.format(newPasswordLinkTemplate, user.getFirstName(),
                APP_BASE_URL + UserController.PUBLIC_RESETPASSWORDFORM_URL, user.getEmail(), token.getToken()));
        javaMailSender.send(message);
    }
}
