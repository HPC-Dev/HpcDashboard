package com.results.HpcDashboard.services;

import com.results.HpcDashboard.dto.UserRegistrationDto;
import com.results.HpcDashboard.models.PasswordResetToken;
import com.results.HpcDashboard.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;


public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User findByUserName(String uname);

    User save(UserRegistrationDto registration);

    void update(String uname, String roles);

    void createPasswordResetTokenForUser(User user, String token);

    PasswordResetToken getPasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    Optional<User> getUserByID(long id);

    void changeUserPassword(User user, String password);

    boolean checkIfValidOldPassword(User user, String password);

}
