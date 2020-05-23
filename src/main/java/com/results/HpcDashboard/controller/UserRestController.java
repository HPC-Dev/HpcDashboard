package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    UserServiceImpl userService;

    @PutMapping("/role/{uname}/{roles}")
    public void updateUserRoles(@PathVariable("uname") String uname, @PathVariable("roles") String roles)
    {
        if(uname != null || uname.length() > 1){
            userService.update(uname,roles);
        }

    }

}


