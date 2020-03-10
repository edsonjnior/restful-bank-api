package com.github.edsonjnior.bankapi.controllers;

import com.github.edsonjnior.bankapi.domains.SecurityConstants;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SecurityController {

    @Secured({SecurityConstants.ROLE_ADMIN, SecurityConstants.ROLE_CLIENT})
    @ResponseBody
    @GetMapping(value = "/user-auth")
    public User user(){
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

}
