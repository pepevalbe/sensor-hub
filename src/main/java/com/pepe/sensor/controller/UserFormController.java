package com.pepe.sensor.controller;

import com.pepe.sensor.service.UserService;
import com.pepe.sensor.service.dto.PersonDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Slf4j
@Controller
@AllArgsConstructor
public class UserFormController {

	public static final String PUBLIC_LOGIN_URL = "/public/login";
	public static final String PUBLIC_ACTIVATEUSER_URL = "/public/activateuser";
	public static final String PUBLIC_RESETPASSWORDFORM_URL = "/public/resetpasswordform";

	private final UserService userService;

	/**
	 * Generate Login form
	 *
	 * @param error Indicates that user have already tried to log in (optional)
	 * @param model
	 * @return Login HTML form
	 */
	@RequestMapping(value = PUBLIC_LOGIN_URL)
	public String loginForm(@RequestParam(name = "error", required = false) String error, Map<String, Object> model) {

		if (error != null) {
			model.put("loginError", true);
		}
		return "login";
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
	public String activateUser(@RequestParam("email") String email, @RequestParam("token") String token, Map<String, Object> model) {

		PersonDto user = userService.activateUser(email, token);

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
	 * Generate reset password form
	 *
	 * @param email User email
	 * @param token User reset token (needs to be generated previously)
	 * @param model
	 * @return Reset Password HTML form
	 */
	@RequestMapping(value = PUBLIC_RESETPASSWORDFORM_URL)
	public String resetPasswordForm(@RequestParam("email") String email, @RequestParam("token") String token, Map<String, Object> model) {

		// If token is Ok we print the form otherwise we show an error
		if (userService.checkTemporaryToken(email, token)) {
			model.put("email", email);
			model.put("token", token);
		} else {
			model.put("error", true);
		}

		return "reset_password";
	}
}
