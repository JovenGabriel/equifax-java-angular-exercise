package com.equifax.users.service;

import com.equifax.users.dto.UserDTO;
import com.equifax.users.dto.UserLoginDTO;
import com.equifax.users.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();
    List<UserDTO> importUsersFromExcel(MultipartFile file);
    User login(UserLoginDTO userLoginDTO);

}
