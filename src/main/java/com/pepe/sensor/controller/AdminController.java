package com.pepe.sensor.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controller for Admin operations
 */
@Slf4j
@Controller
public class AdminController {

	private static final String ADMIN_CONFIGVARS_URL = "/admin/configvars";
	private static final String ADMIN_SIGNUPENABLE_URL = "/admin/signupenable";
	private static final String ADMIN_SIGNUPDISABLE_URL = "/admin/signupdisable";
	private static final String ADMIN_SIGNUPSTATUS_URL = "/admin/signupstatus";

	private final Environment environment;

	public AdminController(Environment environment, @Value("${pepe-sensores.sign_up_enabled}") String sign_up_enabled) {
		this.environment = environment;
		System.setProperty("sign-up-enabled", sign_up_enabled);
	}

	/**
	 * Get active profiles and config vars
	 *
	 * @return Active profiles and config vars
	 */
	@RequestMapping(ADMIN_CONFIGVARS_URL)
	@ResponseBody
	public List<String> getConfigVars() {

		List<String> configVars = new ArrayList<>(Arrays.asList(environment.getActiveProfiles()));
		configVars.add(environment.getProperty("pepe-sensores.app_base_url"));
		configVars.add(environment.getProperty("pepe-sensores.weather_url"));
		configVars.add(environment.getProperty("pepe-sensores.sign_up_enabled"));
		configVars.add(environment.getProperty("pepe-sensores.demo_user_role"));
		return configVars;
	}

	/**
	 * Enable signing up
	 */
	@RequestMapping(ADMIN_SIGNUPENABLE_URL)
	@ResponseStatus(HttpStatus.OK)
	public void enableSignup() {
		System.setProperty("sign-up-enabled", "true");
		log.info("Sign up enabled");
	}

	/**
	 * Disable signing up
	 */
	@RequestMapping(ADMIN_SIGNUPDISABLE_URL)
	@ResponseStatus(HttpStatus.OK)
	public void disableSignup() {
		System.setProperty("sign-up-enabled", "false");
		log.info("Sign up disabled");
	}

	/**
	 * Check if signing up status
	 *
	 * @return true sign up enabled, false sign up disabled
	 */
	@RequestMapping(ADMIN_SIGNUPSTATUS_URL)
	@ResponseBody
	public String getSignupStatus() {
		return System.getProperty("sign-up-enabled");
	}
}
