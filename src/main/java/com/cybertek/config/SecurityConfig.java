package com.cybertek.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //ctrl+O    to override //choose only one method configure(HttpSecurity http)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
 //       super.configure(http);  //coming from Spring but we dont need super.configure(http);

        http
                .authorizeRequests()

                //First provide public ACCESS
                .antMatchers(
                            "/",          //everyone can have access after localhost:8080
                            "/login",              //everyone can have access to login
                            "/fragments/**",      //everyone can have access to fragments
                            "/assets/**",        //everyone can have access to assets
                            "/images/**"        //everyone can have access to images
                ).permitAll()

                //LOGIN
                .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/welcome")
                    .failureUrl("/login?error=true")
                    .permitAll()

                //LOGOUT
                .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login");



    }
}
