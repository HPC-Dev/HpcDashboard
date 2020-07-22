package com.results.HpcDashboard.services;

import com.results.HpcDashboard.dto.UserRegistrationDto;
import com.results.HpcDashboard.models.PasswordResetToken;
import com.results.HpcDashboard.models.Role;
import com.results.HpcDashboard.models.User;
import com.results.HpcDashboard.repo.PasswordResetTokenRepository;
import com.results.HpcDashboard.repo.RoleRepository;
import com.results.HpcDashboard.repo.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

import com.results.HpcDashboard.security.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;


    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public User save(UserRegistrationDto register) {
        User user = new User();
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setUserName(register.getUserName());
        user.setEmail(register.getEmail());
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER"), roleRepository.findByName("ROLE_TEAM")));
        return userRepository.save(user);
    }

    public void update(String uname, String roles){
      User  user = userRepository.findByUserName(uname);

      if(roles != null || roles.length() >1)
      {
      String[] s = roles.split(":");
      if(s.length == 1 && s[0].equals("USER")){
          user.setRoles(new ArrayList<>(Arrays.asList(roleRepository.findByName("ROLE_USER"))));
      }
      else if(s.length == 1 && s[0].equals("ADMIN") ){
          user.setRoles(new ArrayList<>(Arrays.asList(roleRepository.findByName("ROLE_ADMIN"))));
      }
      else if(s.length == 1 && s[0].equals("TEAM") ){
          user.setRoles(new ArrayList<>(Arrays.asList(roleRepository.findByName("ROLE_TEAM"))));
      }
      else if(s.length > 1){
          user.setRoles(new ArrayList<>(Arrays.asList(roleRepository.findByName("ROLE_TEAM"), roleRepository.findByName("ROLE_USER"))));
      }

      userRepository.save(user);
      }

    }


    @Override
    public UserDetails loadUserByUsername(String uname) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(uname);
        if (user == null) {
            user = userRepository.findByUserName(uname);
        }
        if (user == null) {
            throw new UsernameNotFoundException("Username is null");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection <? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }


    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);
    }

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(final String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token) .getUser());
    }

    @Override
    public Optional<User> getUserByID(final long id) {
        return userRepository.findById(id);
    }

    @Override
    public void changeUserPassword(final User user, final String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }



}
