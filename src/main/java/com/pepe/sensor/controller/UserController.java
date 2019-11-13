package com.pepe.sensor.controller;

import com.pepe.sensor.dto.PersonDTO;
import com.pepe.sensor.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.net.URI;
import java.security.Principal;
import java.util.Map;

@Slf4j
@Controller
@AllArgsConstructor
public class UserController {

	public static final String PUBLIC_LOGIN_URL = "/public/login";
	public static final String PUBLIC_USERNAME_URL = "/public/username";
	public static final String USER_USERPROFILE_URL = "/user/userprofile";
	public static final String PUBLIC_CREATEUSER_URL = "/public/createuser";
	public static final String PUBLIC_ACTIVATEUSER_URL = "/public/activateuser";
	public static final String USER_REGENERATEPERSONALTOKEN_URL = "/user/regeneratepersonaltoken";
	public static final String PUBLIC_GENERATEPASSWORDTOKEN_URL = "/public/generatepasswordtoken";
	public static final String PUBLIC_RESETPASSWORDFORM_URL = "/public/resetpasswordform";
	public static final String PUBLIC_RESETPASSWORD_URL = "/public/resetpassword";

	private final UserService userService;

	/**
	 * Generate Login form
	 *
	 * @param error Indicates that user have already tried to log in (optional)
	 * @param model
	 * @return Login HTML form
	 */
	@RequestMapping(value = PUBLIC_LOGIN_URL)
	public String loginForm(@RequestParam(name = "error", required = false) String error,
							Map<String, Object> model) {

		if (error != null) {
			model.put("loginError", true);
		}
		return "login";
	}

	/**
	 * Get current logged username
	 *
	 * @param principal Automatically filled when user is logged
	 * @return Logged username or 403 forbidden if not logged
	 */
	@RequestMapping(value = PUBLIC_USERNAME_URL, method = RequestMethod.GET)
	public ResponseEntity<String> getUsername(Principal principal) {

		if (principal == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		} else {
			return ResponseEntity.ok(userService.getUserProfile(principal.getName()).getUsername());
		}
	}

	/**
	 * Get current logged user profile
	 *
	 * @param principal Automatically filled when user is logged
	 * @return Logged user (Person object) or 403 (forbidden)if not logged
	 */
	@RequestMapping(value = USER_USERPROFILE_URL, method = RequestMethod.GET)
	public ResponseEntity<PersonDTO> getUserProfile(Principal principal) {

		if (principal == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		} else {
			return ResponseEntity.ok(userService.getUserProfile(principal.getName()));
		}
	}

	/**
	 * Create user
	 *
	 * @param user User to save in database
	 * @return 409 (conflict) in case username or email already exists, or sign
	 * up is disabled by admin or 201 (created) if user is successfully created
	 */
	@RequestMapping(value = PUBLIC_CREATEUSER_URL, method = RequestMethod.POST)
	public ResponseEntity<String> createUser(@RequestBody PersonDTO userDTO) {

		if ("true".equals(System.getProperty("sign-up-enabled"))) {
			if (userService.getUserProfile(userDTO.getUsername()) != null) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre de usuario ya existe, por favor elija otro.");
			} else if (userService.getByEmail(userDTO.getEmail()) != null) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya se ha utilizado este email para registrarse, por favor utilice otro.");
			} else {
				userService.createUser(userDTO);
				return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado. Necesita activarlo utilizando un link que se ha enviado a su email.");
			}
		} else {
			log.info("Trying to create user but sign up is disabled: " + userDTO.getUsername() + " - " + userDTO.getEmail());
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Registro deshabilitado. Contacte con el administrador.");
		}
	}

	/**
	 * Activate user. This finishes user creation process
	 *
	 * @param email User email
	 * @param token User token
	 * @param model
	 * @return User created HTML
	 */
	@Transactional
	@RequestMapping(value = PUBLIC_ACTIVATEUSER_URL)
	public String activateUser(@RequestParam("email") String email,
							   @RequestParam("token") String token, Map<String, Object> model) {

		PersonDTO user = userService.activateUser(email, token);

		// If token is ok we remove it and activate user otherwise we show an error
		if (user != null) {
			model.put("name", user.getFirstName());
			model.put("username", user.getUsername());
		} else {
			model.put("error", true);
		}
		return "activate_user";
	}

	/**
	 * Regenerate user Personal Token for API calls
	 *
	 * @param principal Automatically filled when user is logged
	 * @return Logged user (Person object) or 403 forbidden if not logged
	 */
	@RequestMapping(value = USER_REGENERATEPERSONALTOKEN_URL, method = RequestMethod.POST)
	public ResponseEntity regeneratePersonalToken(Principal principal) {

		if (principal == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		} else {
			userService.regeneratePersonalToken(principal.getName());
			return ResponseEntity.ok().build();
		}
	}

	/**
	 * Generate a reset password token and send to user email.
	 *
	 * @param email User email
	 * @return 200 if token generated or 404 if email not found
	 */
	@Transactional
	@RequestMapping(value = PUBLIC_GENERATEPASSWORDTOKEN_URL)
	public ResponseEntity generatePasswordToken(@RequestParam("email") String email) {

		userService.generatePasswordToken(email);

		// Always redirect to login page
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(URI.create("/public/login"));
		return new ResponseEntity(httpHeaders, HttpStatus.SEE_OTHER);
	}

	/**
	 * Generate reset password form
	 *
	 * @param email User email
	 * @param token User reset token (needs to be generated previously)
	 * @param model
	 * @return Reset Password HTML form
	 */
	@RequestMapping(value = PUBLIC_RESETPASSWORDFORM_URL)
	public String resetPasswordForm(@RequestParam("email") String email,
									@RequestParam("token") String token, Map<String, Object> model) {

		// If token is Ok we print the form otherwise we show an error
		if (userService.checkTemporaryToken(email, token)) {
			model.put("email", email);
			model.put("token", token);
		} else {
			model.put("error", true);
		}

		return "reset_password";
	}

	/**
	 * Reset user password
	 *
	 * @param email       User email
	 * @param token       User reset token (needs to be generated previously)
	 * @param newPassword New user password
	 * @return 200 if password changed or 404 if email or token not valid
	 */
	@Transactional
	@RequestMapping(value = PUBLIC_RESETPASSWORD_URL)
	public ResponseEntity resetPassword(@RequestParam("email") String email,
										@RequestParam("token") String token,
										@RequestParam("newPassword") String newPassword) {

		userService.resetPassword(email, token, newPassword);

		// Always redirect to login page
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(URI.create("/public/login"));
		return new ResponseEntity(httpHeaders, HttpStatus.SEE_OTHER);
	}
}
