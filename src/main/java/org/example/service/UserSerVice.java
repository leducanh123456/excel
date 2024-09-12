package org.example.service;

import org.example.dto.UserDto;

import java.util.List;

public interface UserSerVice {
    List<UserDto> getAllUserAndRoleId();
}
