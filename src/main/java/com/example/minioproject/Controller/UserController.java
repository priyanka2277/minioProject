package com.example.minioproject.Controller;

import com.example.minioproject.dto.UserDto;
import com.example.minioproject.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        UserDto registeredUser = userService.registerUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(String.format("User '%s' registered successfully.",
                        registeredUser.getEmail()
                        ));
    }
@GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId){
        try{
            UserDto userDto=userService.getUserById(userId);
            return ResponseEntity.ok(userDto);
        }catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("User not found with Id"));
        }
}
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(updatedUser);
    }

@GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> users=userService.getAllUsers();
        return ResponseEntity.ok(users);
}
@DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId){
        try{
            userService.deleteUser(userId);
            return ResponseEntity.ok(String.format("User with given Id deleted successfully."));
        }catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("User not found with given Id"));

        }
}

}
