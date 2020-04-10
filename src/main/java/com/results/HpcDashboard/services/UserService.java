package com.results.HpcDashboard.services;

import com.results.HpcDashboard.dto.UserRegistrationDto;
import com.results.HpcDashboard.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User findByUserName(String uname);

    User save(UserRegistrationDto registration);
}
