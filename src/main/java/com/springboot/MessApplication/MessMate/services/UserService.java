package com.springboot.MessApplication.MessMate.services;

import com.springboot.MessApplication.MessMate.dto.SignupDto;
import com.springboot.MessApplication.MessMate.dto.UserDto;
import com.springboot.MessApplication.MessMate.entities.User;
import com.springboot.MessApplication.MessMate.entities.enums.Role;
import com.springboot.MessApplication.MessMate.exceptions.ResourceNotFoundException;
import com.springboot.MessApplication.MessMate.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    public UserDto signup(SignupDto signupDto) {
        Optional<User> user = userRepository.findByEmail(signupDto.getEmail());
        if(user.isPresent()){
            throw new BadCredentialsException("User with email " +  signupDto.getEmail() + " already exists");
        }

        User toBeCreatedUser = modelMapper.map(signupDto, User.class);
        toBeCreatedUser.setRole(Role.STUDENT);
        toBeCreatedUser.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        return modelMapper.map(userRepository.save(toBeCreatedUser), UserDto.class);
    }
}
