package com.pepe.sensor.controller;

import com.pepe.sensor.ConfigVarHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

	public static final String SIGN_UP_ENABLED_SYSTEM_PROPERTY = "sign-up-enabled";

	private static final String ADMIN_CONFIGVARS_URL = "/admin/configvars";
	private static final String ADMIN_SIGNUPENABLE_URL = "/admin/signupenable";
	private static final String ADMIN_SIGNUPDISABLE_URL = "/admin/signupdisable";
	private static final String ADMIN_SIGNUPSTATUS_URL = "/admin/signupstatus";

	@Autowired
	private ConfigVarHolder configVarHolder;
	@Autowired
	private Environment environment;

	public AdminController(ConfigVarHolder configVarHolder) {
		System.setProperty(SIGN_UP_ENABLED_SYSTEM_PROPERTY, configVarHolder.getUserRegistryEnabled());
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
		configVars.add(configVarHolder.getEmailUsername());
		configVars.add(configVarHolder.getEmailPassword());
		configVars.add(configVarHolder.getUserRegistryEnabled());
		configVars.add(configVarHolder.getAppBaseUrl());
		configVars.add(configVarHolder.getWeatherUrl());
		configVars.add(configVarHolder.getDemoUser().toString());
		return configVars;
	}

	/**
	 * Enable signing up
	 */
	@RequestMapping(ADMIN_SIGNUPENABLE_URL)
	@ResponseStatus(HttpStatus.OK)
	public void enableSignup() {
		System.setProperty(SIGN_UP_ENABLED_SYSTEM_PROPERTY, "true");
		log.info("Sign up enabled");
	}

	/**
	 * Disable signing up
	 */
	@RequestMapping(ADMIN_SIGNUPDISABLE_URL)
	@ResponseStatus(HttpStatus.OK)
	public void disableSignup() {
		System.setProperty(SIGN_UP_ENABLED_SYSTEM_PROPERTY, "false");
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
