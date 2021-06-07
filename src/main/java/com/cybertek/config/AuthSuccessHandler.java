package com.cybertek.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Configuration
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    //ctrl+O
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        //We need to get roles. Then we will arrange the custom landing pages in accordance with the roles
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities()); //authentication.getAuthorities() coming from UsrePrincipal

        if(roles.contains("Admin")){
            httpServletResponse.sendRedirect("/user/create");
        }
        if(roles.contains("Manager")){
            httpServletResponse.sendRedirect("/task/create");
        }
        if(roles.contains("Employee")){
            httpServletResponse.sendRedirect("/task/employee");
        }

    }
}
