package com.pepe.sensor.security;

import com.pepe.sensor.controller.DoorEventRestController;
import com.pepe.sensor.controller.SensorReadingRestController;
import com.pepe.sensor.controller.TempHumidityRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomUserDetailsService customeUserDetailsService;

    private final int REMEMBERME_VALIDITY = 15 * 24 * 60 * 60;    // 15 Days
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/public/**",
                        TempHumidityRestController.PUBLIC_TEMPHUMIDITY_URL,
                        DoorEventRestController.PUBLIC_DOOREVENT_URL,
                        SensorReadingRestController.PUBLIC_GENERICSENSOR_URL).permitAll()
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/**").hasRole("ADMIN")
                .and().formLogin().loginPage("/public/login").loginProcessingUrl("/public/login").failureUrl("/public/login?error")
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/public/logout")).logoutSuccessUrl("/")
                .and().rememberMe().rememberMeParameter("remember-me").key("cookie-secret").tokenValiditySeconds(REMEMBERME_VALIDITY);
        
        http.headers().frameOptions().sameOrigin();     // Needed for H2 console
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customeUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
