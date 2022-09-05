package com.jokindy.finapp.user;

import com.jokindy.finapp.user.validation.PasswordMatches;
import com.jokindy.finapp.user.validation.ValidEmail;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@PasswordMatches(message = "Пароли не совпадают")
public class UserDto {

    @NotEmpty(message = "Заполните логин")
    private String username;

    @NotEmpty(message = "Заполните пароль")
    private String password;

    @NotEmpty(message = "Заполните пароль")
    private String matchingPassword;

    @ValidEmail(message = "Неверный формат электронной почты")
    @NotEmpty(message = "Заполните почту")
    private String email;
}
