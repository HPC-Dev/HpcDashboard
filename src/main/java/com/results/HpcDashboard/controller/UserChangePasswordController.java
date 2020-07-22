package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.FormCommand;
import com.results.HpcDashboard.dto.PasswordDto;
import com.results.HpcDashboard.models.User;
import com.results.HpcDashboard.security.UserSecurityService;
import com.results.HpcDashboard.services.UserService;
import com.results.HpcDashboard.services.UserServiceImpl;
import com.results.HpcDashboard.util.GenericResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserChangePasswordController {

    @Autowired
    UserSecurityService userSecurityService;

    @Autowired
    MessageSource messages;

    @Autowired
    UserService userService;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
     JavaMailSender mailSender;

    @Autowired
    MailController mailController;


    @ModelAttribute("passwordDto")
    public PasswordDto passwordDto() {
        return new PasswordDto();
    }


    @PostMapping("/savePassword")
    public String savePassword(@ModelAttribute("passwordDto") @Valid PasswordDto passwordDto, final Locale locale, RedirectAttributes redirectAttrs) {

        final String result = userSecurityService.validatePasswordResetToken(passwordDto.getToken());

        if(result != null) {
            redirectAttrs.addFlashAttribute("tokenError", messages.getMessage("auth.message." + result, null, locale));
            return "redirect:/login";
        }

        Optional<User> user = userService.getUserByPasswordResetToken(passwordDto.getToken());
        if(user.isPresent()) {
            try {
                userService.changeUserPassword(user.get(), passwordDto.getNewPassword());
                redirectAttrs.addFlashAttribute("resetSuccess", messages.getMessage("message.resetPasswordSuc", null, locale));
            }
            catch (Exception e)
            {
                redirectAttrs.addFlashAttribute("failMessage", ExceptionUtils.getRootCauseMessage(e));
            }

        } else {
            redirectAttrs.addFlashAttribute("invalidUser", messages.getMessage("auth.message.invalid", null, locale));
        }
        return "redirect:/login";
    }

    @GetMapping("/changePassword")
    public String showChangePasswordPage(final Locale locale,@RequestParam("token") final String token, RedirectAttributes redirectAttrs, Model model) {

        final String result = userSecurityService.validatePasswordResetToken(token);

        if(result != null) {
            redirectAttrs.addFlashAttribute("tokenError", messages.getMessage("auth.message." + result, null, locale));
            return "redirect:/login";
        }
        else {
            model.addAttribute("token", token);
            model.addAttribute("password", new PasswordDto());
            return "updatePassword";
        }
    }

    @PostMapping("/resetPassword")
    public String resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail, RedirectAttributes redirectAttributes) {
        final User user = userService.findByEmail(userEmail);
        if (user != null) {
            final String token = UUID.randomUUID().toString();
            try {
                userServiceImpl.createPasswordResetTokenForUser(user, token);
                mailSender.send(mailController.constructResetTokenEmail(mailController.getAppUrl(request), request.getLocale(), token, user));
                redirectAttributes.addFlashAttribute("reset", messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
             }
            catch (Exception e){
                redirectAttributes.addFlashAttribute("failMessage", ExceptionUtils.getRootCauseMessage(e));
                return "redirect:/forgotPassword";
            }
            return "redirect:/login";
        }

        else{
            redirectAttributes.addFlashAttribute("error", messages.getMessage("message.resetPasswordError",null, request.getLocale()));
            return "redirect:/forgotPassword";
        }
    }

}
