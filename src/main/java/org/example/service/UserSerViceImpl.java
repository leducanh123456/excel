package org.example.service;

import org.example.dto.UserDto;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSerViceImpl implements UserSerVice{
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserDto> getAllUserAndRoleId() {
        return userRepository.getAllUserAndRoleId();
    }
}
