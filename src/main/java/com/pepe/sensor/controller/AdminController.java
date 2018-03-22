package com.pepe.sensor.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private Environment environment;

    /**
     * Get config vars
     *
     * @return true sign up enabled, false sign up disabled
     */
    @RequestMapping(ADMIN_CONFIGVARS_URL)
    @ResponseBody
    public String[] getConfigVars() {
        String[] returnValue = new String[3];
        returnValue[0] = environment.getProperty("pepe-sensores.app_base_url");        
        returnValue[1] = environment.getProperty("pepe-sensores.weather_url");
        returnValue[2] = environment.getActiveProfiles()[0];
        
        return returnValue;
    }

    /**
     * Enable signing up
     */
    @RequestMapping(ADMIN_SIGNUPENABLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public void enableSignup() {
        System.setProperty("sign-up-enabled", "1");
    }

    /**
     * Disable signing up
     */
    @RequestMapping(ADMIN_SIGNUPDISABLE_URL)
    @ResponseStatus(HttpStatus.OK)
    public void disableSignup() {
        System.setProperty("sign-up-enabled", "0");
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
