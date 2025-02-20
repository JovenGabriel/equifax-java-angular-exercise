package com.equifax.users.controllers;

import com.equifax.users.dto.UserDTO;
import com.equifax.users.dto.UserLoginDTO;
import com.equifax.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get all users", description = "Fetches a list of all users in the system.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of users", content = @Content(mediaType = "application/json"))
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
   
    @Operation(summary = "Import users from Excel", description = "Uploads an Excel file and imports users into the system.")
    @ApiResponse(responseCode = "200", description = "Users successfully imported", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Invalid file or format", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Bad Request\", \"message\": \"Invalid file format\"}")))
    @PostMapping("/import")
    public ResponseEntity<List<UserDTO>> importUsersFromExcel(MultipartFile file) {
        return ResponseEntity.ok(userService.importUsersFromExcel(file));
    }

    @Operation(summary = "User login", description = "Authenticates a user based on their email and password")
    @ApiResponse(responseCode = "200", description = "User successfully authenticated")
    @ApiResponse(responseCode = "404", description = "Not Found if the user with email and/or password not matches", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Not found\" , \n \"message\": \"Invalid email or password\"}")))
    @PostMapping("/login")
    private ResponseEntity<Map<String, String>> loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        return ResponseEntity.ok(Map.of("token", userService.login(userLoginDTO).getToken()));
    }

}
