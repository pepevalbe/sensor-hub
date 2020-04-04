package com.pepe.sensor.controller;

import com.pepe.sensor.service.UserService;
import com.pepe.sensor.service.dto.PersonDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.security.Principal;

@Slf4j
@Controller
@AllArgsConstructor
public class UserController {

	public static final String PUBLIC_USERNAME_URL = "/public/username";
	public static final String USER_USERPROFILE_URL = "/user/userprofile";
	public static final String PUBLIC_CREATEUSER_URL = "/public/createuser";
	public static final String USER_REGENERATEPERSONALTOKEN_URL = "/user/regeneratepersonaltoken";
	public static final String PUBLIC_GENERATEPASSWORDTOKEN_URL = "/public/generatepasswordtoken";
	public static final String PUBLIC_RESETPASSWORDFORM_URL = "/public/resetpasswordform";
	public static final String PUBLIC_RESETPASSWORD_URL = "/public/resetpassword";

	private final UserService userService;

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
			return ResponseEntity.ok(userService.getUserByUsername(principal.getName()).getUsername());
		}
	}

	/**
	 * Get current logged user profile
	 *
	 * @param principal Automatically filled when user is logged
	 * @return Logged user (Person object) or 403 (forbidden)if not logged
	 */
	@RequestMapping(value = USER_USERPROFILE_URL, method = RequestMethod.GET)
	public ResponseEntity<PersonDto> getUserProfile(Principal principal) {

		if (principal == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		} else {
			return ResponseEntity.ok(userService.getUserByUsername(principal.getName()));
		}
	}

	/**
	 * Create user
	 *
	 * @param userDTO User to save in database
	 * @return 409 (conflict) in case username or email already exists, or sign
	 * up is disabled by admin or 201 (created) if user is successfully created
	 */
	@RequestMapping(value = PUBLIC_CREATEUSER_URL, method = RequestMethod.POST)
	public ResponseEntity<String> createUser(@RequestBody PersonDto userDTO) {

		if ("true".equals(System.getProperty("sign-up-enabled"))) {
			if (userService.getUserByUsername(userDTO.getUsername()) != null) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre de usuario ya existe, por favor elija otro.");
			} else if (userService.getUserByEmail(userDTO.getEmail()) != null) {
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
	 * Regenerate user Personal Token for API calls
	 *
	 * @param principal Automatically filled when user is logged
	 * @return Logged user (Person object) or 403 forbidden if not logged
	 */
	@RequestMapping(value = USER_REGENERATEPERSONALTOKEN_URL, method = RequestMethod.POST)
	public ResponseEntity<Void> regeneratePersonalToken(Principal principal) {

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
	public ResponseEntity<Void> generatePasswordToken(@RequestParam("email") String email) {

		userService.generatePasswordToken(email);

		// Always redirect to login page
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(URI.create("/public/login"));
		return new ResponseEntity(httpHeaders, HttpStatus.SEE_OTHER);
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
	public ResponseEntity<Void> resetPassword(@RequestParam("email") String email,
											  @RequestParam("token") String token,
											  @RequestParam("newPassword") String newPassword) {

		userService.resetPassword(email, token, newPassword);

		// Always redirect to login page
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(URI.create("/public/login"));
		return new ResponseEntity(httpHeaders, HttpStatus.SEE_OTHER);
	}
}
