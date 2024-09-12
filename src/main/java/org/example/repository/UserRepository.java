package org.example.repository;

import org.example.dto.UserDto;

import java.util.List;

public interface UserRepository {
    List<UserDto> getAllUserAndRoleId();
}
