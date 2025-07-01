package com.example.minioproject.Controller;

import com.example.minioproject.dto.UserDto;
import com.example.minioproject.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User APIs")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    @Operation(summary="Register the owner")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDto userDto) {
        UserDto registeredUser = userService.registerUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(String.format("User '%s' registered successfully.",
                        registeredUser.getEmail()
                        ));
    }
@GetMapping("/{userId}")
@Operation(summary="Get the information of the owner  by id")
    public ResponseEntity<?> getUserById(@PathVariable Long userId){
        try{
            UserDto userDto=userService.getUserById(userId);
            return ResponseEntity.ok(userDto);
        }catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("User not found with Id"));
        }
}
    @PutMapping("/{userId}")
    @Operation(summary="Update the information of the owner by id")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody @Valid  UserDto userDto) {
        UserDto updatedUser = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(updatedUser);
    }

@GetMapping("/all")
@Operation(summary="Get the information of all owner")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> users=userService.getAllUsers();
        return ResponseEntity.ok(users);
}
@DeleteMapping("/delete/{userId}")
@Operation(summary="Delete the owner by id")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId){
        try{
            userService.deleteUser(userId);
            return ResponseEntity.ok(String.format("User with given Id deleted successfully."));
        }catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("User not found with given Id"));

        }
}

}
