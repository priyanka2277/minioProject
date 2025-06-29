package com.example.minioproject.service;

import com.example.minioproject.dto.UserDto;
import com.example.minioproject.entity.User;
import com.example.minioproject.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    public UserDto registerUser(UserDto userDto){
        if(userRepository.findByEmail(userDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("User with email "+userDto.getEmail()+"already exists");
        }
        User user=modelMapper.map(userDto, User.class);
        User savedUser=userRepository.save(user);
        return modelMapper.map(savedUser,UserDto.class);
    }
    public UserDto updateUser(Long userId,UserDto userDto){
        User existingUser=userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("User not found with ID:"+userId));
        if(userDto.getName()!=null){
            existingUser.setName(userDto.getName());
        }
        if(userDto.getEmail()!=null){
            existingUser.setEmail(userDto.getEmail());
        }
        User updatedUser=userRepository.save(existingUser);
        return modelMapper.map(updatedUser,UserDto.class);

    }
    public UserDto getUserById(Long userId){
        User user=userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("User not found with Id:'id'"+userId));
        return modelMapper.map(user,UserDto.class);
    }
    public List<UserDto> getAllUsers(){
        List<User> users=userRepository.findAll();
        return users.stream()
                .map(user->modelMapper.map(user,UserDto.class))
                .toList();

    }
    public void deleteUser(Long userId){
        User existingUser=userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("User not found with Id:"+userId));
        userRepository.delete(existingUser);
    }

}
