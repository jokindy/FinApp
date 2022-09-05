package com.jokindy.finapp.user;

import com.jokindy.finapp.exception.UserAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    public void registerNewUser(UserDto userDto) {
        if (checkUsername(userDto.getUsername())) {
            throw new UserAlreadyExistsException();
        }
        User user = modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }

    private boolean checkUsername(String username) {
        return userRepository.findUserByUsername(username) != null;
    }
}
