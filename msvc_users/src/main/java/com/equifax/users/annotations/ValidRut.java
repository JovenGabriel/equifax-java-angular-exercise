package com.equifax.users.annotations;

import com.equifax.users.utils.RutValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RutValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRut {
    String message() default "Invalid RUT";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}