package com.springboot.MessApplication.MessMate.controllers;

import com.springboot.MessApplication.MessMate.dto.UserDto;
import com.springboot.MessApplication.MessMate.entities.enums.SubscriptionStatus;
import com.springboot.MessApplication.MessMate.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestParam(value = "status" , required = false) SubscriptionStatus status
    ) {
        List<UserDto> users = userService.getAllUsersFilteredBySubscriptionStatus(status);
        return ResponseEntity.ok(users);
    }

    //TODO getUserDetailsById (admin)
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserProfileById(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUserProfileById(id));
    }

    //TODO getMyProfile (student)
    @GetMapping
    public ResponseEntity<UserDto> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    //TODO updateProfile (all)


}
