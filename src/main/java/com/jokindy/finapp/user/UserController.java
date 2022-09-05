package com.jokindy.finapp.user;

import com.jokindy.finapp.exception.UserAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "reg";
    }

    @PostMapping("/registration")
    public String registerUserAccount(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result,
                                      Model model) {
        if (result.hasErrors()) {
            for (FieldError fieldError : result.getFieldErrors()) {
                model.addAttribute("message", fieldError.getDefaultMessage());
            }
            for (ObjectError objectError : result.getGlobalErrors()) {
                model.addAttribute("message", objectError.getDefaultMessage());
            }
            model.addAttribute("redirect", "/registration");
            return "error/400";
        }
        try {
            userService.registerNewUser(userDto);
            return "redirect:/login";
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("message", "Пользователь с таким логином уже зарегистрирован");
            return "error/400";
        }
    }
}