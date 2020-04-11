package com.pepe.sensor;

import com.pepe.sensor.controller.UserFormController;
import com.pepe.sensor.persistence.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.pepe.sensor.VarKeeper.APP_BASE_URL_KEY;
import static com.pepe.sensor.VarKeeper.EMAIL_USERNAME_KEY;

/* Component responsible of sending email to users */
@Slf4j
@Component
public class EmailSender {

	private static final String ACTIVATE_USER_EMAIL_SUBJECT = "Creación de cuenta en pepe-sensores";
	private static final String ACTIVATE_USER_EMAIL_TEMPLATE = "Hola %s, por favor utiliza el siguiente link para finalizar la creación de tu cuenta: %s?email=%s&token=%s";
	private static final String WELCOME_EMAIL_SUBJECT = "Bienvenido a pepe-sensores";
	private static final String WELCOME_EMAIL_TEMPLATE = "Hola %s, ya puedes entrar a tu cuenta con tu nombre de usuario (%s) y contraseña: %s\nTu Token Personal es: %s. Utilízalo para enviar información desde tus sensores y no lo compartas.";
	private static final String PERSONAL_TOKEN_GENERATION_EMAIL_SUBJECT = "Token Personal regenerado en pepe-sensores";
	private static final String PERSONAL_TOKEN_GENERATION_EMAIL_TEMPLATE = "Hola %s, se ha regenerado tu Token Personal. El nuevo Token es: %s";
	private static final String FORGOTTEN_CREDENTIALS_EMAIL_SUBJECT = "Solicitud de recuerdo de credenciales en pepe-sensores";
	private static final String FORGOTTEN_CREDENTIALS_EMAIL_TEMPLATE = "Hola %s, su usuario en pepe-sensores es: %s\nPuede utilizar el siguiente enlace si desea crear una nueva contraseña: %s";

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private VarKeeper varKeeper;

	@Async
	public void sendEmail(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(varKeeper.get(EMAIL_USERNAME_KEY));
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		javaMailSender.send(message);
		log.info("Email sent to: " + to);
	}

	@Async
	public void sendActivateUserLinkEmail(Person user) {
		String text = String.format(ACTIVATE_USER_EMAIL_TEMPLATE,
				user.getFirstName(),
				varKeeper.get(APP_BASE_URL_KEY) + UserFormController.PUBLIC_ACTIVATEUSER_URL,
				user.getEmail(),
				user.getTemporaryToken().getToken());
		log.info("Sending activate user link email to: " + user.getUsername() + " - " + user.getEmail());
		sendEmail(user.getEmail(), ACTIVATE_USER_EMAIL_SUBJECT, text);
	}

	@Async
	public void sendWelcomeEmail(Person user) {
		String text = String.format(WELCOME_EMAIL_TEMPLATE,
				user.getFirstName(),
				user.getUsername(),
				varKeeper.get(APP_BASE_URL_KEY) + UserFormController.PUBLIC_LOGIN_URL,
				user.getToken());
		log.info("Sending welcome email to: " + user.getUsername() + " - " + user.getEmail());
		sendEmail(user.getEmail(), WELCOME_EMAIL_SUBJECT, text);
	}

	@Async
	public void sendNewPersonalTokenEmail(Person user) {
		String text = String.format(PERSONAL_TOKEN_GENERATION_EMAIL_TEMPLATE, user.getFirstName(), user.getToken());
		log.info("Sending new personal token email to: " + user.getUsername() + " - " + user.getEmail());
		sendEmail(user.getEmail(), PERSONAL_TOKEN_GENERATION_EMAIL_SUBJECT, text);
	}

	@Async
	public void sendForgottenCredentialsEmail(Person user) {
		String newPasswordLink = varKeeper.get(APP_BASE_URL_KEY) + UserFormController.PUBLIC_RESETPASSWORDFORM_URL
				+ "?email=" + user.getEmail() + "&token=" + user.getTemporaryToken().getToken();
		String text = String.format(FORGOTTEN_CREDENTIALS_EMAIL_TEMPLATE,
				user.getFirstName(),
				user.getUsername(),
				newPasswordLink);
		log.info("Sending forgotten credentials email to: " + user.getUsername() + " - " + user.getEmail());
		sendEmail(user.getEmail(), FORGOTTEN_CREDENTIALS_EMAIL_SUBJECT, text);
	}
}
