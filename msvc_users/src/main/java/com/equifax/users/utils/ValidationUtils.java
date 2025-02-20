package com.equifax.users.utils;

import com.equifax.users.dto.UserDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class ValidationUtils {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    /**
     * Validates the provided UserDto object against defined constraints.
     * Logs validation errors and returns false if any violations are found.
     *
     * @param userDto the UserDto object to be validated
     * @return true if the UserDto object is valid and passes all constraints, false otherwise
     */
    public static boolean validate(UserDTO userDto) {
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDto);
        if (!violations.isEmpty()) {
            violations.forEach(v -> log.warn("Validation error: {}", v.getMessage()));
            return false;
        }
        return true;
    }
}
