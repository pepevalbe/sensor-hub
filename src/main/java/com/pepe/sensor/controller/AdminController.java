package com.pepe.sensor.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * Controller for Admin operations
 *
 */
@Controller
public class AdminController {

    public static final String ADMIN_CONFIGVARS_URL = "/admin/configvars";
    public static final String ADMIN_SIGNUPENABLE_URL = "/admin/signupenable";
    public static final String ADMIN_SIGNUPDISABLE_URL = "/admin/signupdisable";
    public static final String ADMIN_SIGNUPSTATUS_URL = "/admin/signupstatus";

    private final Environment environment;

    @Autowired
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
        List<String> configVars = new ArrayList<>();

        for (String profile : environment.getActiveProfiles()) {
            configVars.add(profile);
        }
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
    }

    /**
     * Disable signing up
     */
    @RequestMapping(ADMIN_SIGNUPDISABLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public void disableSignup() {
        System.setProperty("sign-up-enabled", "false");
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
