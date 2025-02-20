package com.equifax.users.serviceImpl;

import com.equifax.users.dto.UserDTO;
import com.equifax.users.dto.UserLoginDTO;
import com.equifax.users.entities.User;
import com.equifax.users.exceptions.NotFoundException;
import com.equifax.users.repositories.UserRepository;
import com.equifax.users.service.UserService;
import com.equifax.users.utils.ExcelParserHelper;
import com.equifax.users.utils.JwtTokenUtil;
import com.equifax.users.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * Retrieves all users from the database and maps them to a list of UserDTO objects.
     *
     * @return a list of UserDTO objects representing all users.
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(user -> UserDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .rut(user.getRut())
                .build()).toList();
    }

    /**
     * Imports and processes a list of users from a provided Excel file. The method parses the file,
     * validates user data, checks for duplicate entries in the database, and saves valid users.
     *
     * @param file the Excel file containing user data to import
     * @return a list of UserDTO objects representing the successfully imported users
     */
    @Override
    @Transactional
    public List<UserDTO> importUsersFromExcel(MultipartFile file) {
        return ExcelParserHelper.parseExcel(file).stream()
                .map(row -> UserDTO.builder()
                        .name(row.get("name"))
                        .rut(row.get("rut"))
                        .email(row.get("email"))
                        .build())
                .filter(ValidationUtils::validate)
                .filter(userDTO -> !userRepository.existsByEmail(userDTO.getEmail()) && !userRepository.existsByRut(userDTO.getRut()))
                .map(validUserDTO -> userRepository.save(User.builder()
                        .name(validUserDTO.getName())
                        .rut(validUserDTO.getRut())
                        .email(validUserDTO.getEmail())
                        .password(passwordEncoder.encode(validUserDTO.getName().substring(0, 1).toUpperCase() + validUserDTO.getRut().substring(0, 4))) // Replace with appropriate logic to generate a password
                        .build()))
                .map(savedUser -> UserDTO.builder()
                        .name(savedUser.getName())
                        .rut(savedUser.getRut())
                        .email(savedUser.getEmail())
                        .build())
                .toList();
    }

    /**
     * Authenticates a user based on their email and password, and generates a JWT token
     * if the credentials are valid. The token is saved in the User entity.
     *
     * @param userLoginDTO contains the email and password entered by the user for authentication.
     * @return the authenticated User object with the generated JWT token included.
     * @throws NotFoundException if the email is not found or the password does not match.
     */
    @Override
    @Transactional
    public User login(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByEmail(userLoginDTO.getEmail())
                .orElseThrow(() -> new NotFoundException("Invalid email or password"));

        if (passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            String token = jwtTokenUtil.generateToken(user.getEmail());
            user.setToken(token);
            return userRepository.save(user);
        } else {
            throw new NotFoundException("Invalid email or password");
        }
    }

}
