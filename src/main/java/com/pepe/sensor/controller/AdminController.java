package com.pepe.sensor.controller;

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

    public static final String ADMIN_SIGNUPENABLE_URL = "/admin/signupenable";
    public static final String ADMIN_SIGNUPDISABLE_URL = "/admin/signupdisable";
    public static final String ADMIN_SIGNUPSTATUS_URL = "/admin/signupstatus";

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
