package com.pepe.sensor.controller;

import com.pepe.sensor.VarKeeper;
import com.pepe.sensor.service.UserService;
import com.pepe.sensor.service.dto.PersonDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.security.Principal;

@Slf4j
@Controller
@AllArgsConstructor
public class UserController {

	private static final String PUBLIC_USERNAME_URL = "/public/username";
	private static final String USER_USERPROFILE_URL = "/user/userprofile";
	private static final String PUBLIC_CREATEUSER_URL = "/public/createuser";
	private static final String PUBLIC_MODIFYUSER_URL = "/public/modifyuser";
	private static final String USER_REGENERATEPERSONALTOKEN_URL = "/user/regeneratepersonaltoken";
	private static final String PUBLIC_GENERATEPASSWORDTOKEN_URL = "/public/generatepasswordtoken";
	private static final String PUBLIC_RESETPASSWORD_URL = "/public/resetpassword";

	private final VarKeeper varKeeper;
	private final UserService userService;

	/**
	 * Get current logged username
	 *
	 * @param principal Automatically filled when user is logged
	 * @return Logged username or 403 forbidden if not logged
	 */
	@GetMapping(PUBLIC_USERNAME_URL)
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
	@GetMapping(USER_USERPROFILE_URL)
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
	@PostMapping(PUBLIC_CREATEUSER_URL)
	public ResponseEntity<String> createUser(@RequestBody PersonDto userDTO) {

		if (varKeeper.isUserRegistryEnabled()) {
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
	 * Modify user profile
	 *
	 * @param userDTO User to modify in database
	 * @return 409 (conflict) in case username or email already exists, or 200 if user is successfully saved
	 */
	@PostMapping(PUBLIC_MODIFYUSER_URL)
	public ResponseEntity<String> modifyUser(@RequestBody PersonDto userDTO) {

		if (userService.getUserByUsername(userDTO.getUsername()) != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre de usuario ya existe, por favor elija otro.");
		} else {
			userService.modifyUser(userDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body("Usuario modificado.");
		}
	}

	/**
	 * Regenerate user Personal Token for API calls
	 *
	 * @param principal Automatically filled when user is logged
	 * @return Logged user (Person object) or 403 forbidden if not logged
	 */
	@PostMapping(USER_REGENERATEPERSONALTOKEN_URL)
	public ResponseEntity<Void> regeneratePersonalToken(Principal principal) {

		if (principal == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		} else {
			userService.regeneratePersonalToken(principal.getName());
			return ResponseEntity.ok().build();
		}
	}

	/**
	 * Generate a reset password token and send forgotten credentials email to user.
	 *
	 * @param email User email
	 * @return 200 if token generated or 404 if email not found
	 */
	@Transactional
	@PostMapping(PUBLIC_GENERATEPASSWORDTOKEN_URL)
	public ResponseEntity<Void> rememberCredentials(@RequestParam("email") String email) {

		userService.rememberCredentials(email);

		// Always redirect to login page
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(URI.create("/public/login"));
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
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
	@PostMapping(PUBLIC_RESETPASSWORD_URL)
	public ResponseEntity<Void> resetPassword(@RequestParam("email") String email,
											  @RequestParam("token") String token,
											  @RequestParam("newPassword") String newPassword) {

		userService.resetPassword(email, token, newPassword);

		// Always redirect to login page
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(URI.create("/public/login"));
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}
}
