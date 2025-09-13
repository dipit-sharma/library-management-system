package com.spry.library_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spry.library_service.dto.UserDto;
import com.spry.library_service.entity.User;
import com.spry.library_service.repository.UserRepository;

@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByEmailAndDeletedFalse(userDto.getEmail())) {
            throw new IllegalArgumentException("User with email " + userDto.getEmail() + " already exists");
        }
        
        User user = new User(userDto.getName(), userDto.getEmail());
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }
    
    private UserDto convertToDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
