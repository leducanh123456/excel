package org.example.controller;

import org.example.dto.UserDto;
import org.example.service.UserSerVice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class UserController {
    @Autowired
    private UserSerVice userSerVice;

    @PostMapping("/get-all")
    public List<UserDto> getAllUserAndRoleId() {
        return userSerVice.getAllUserAndRoleId();
    }
}
