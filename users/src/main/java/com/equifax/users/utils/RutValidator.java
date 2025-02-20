package com.equifax.users.utils;

import com.equifax.users.annotations.ValidRut;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class RutValidator implements ConstraintValidator<ValidRut, String> {

    /**
     * A regular expression pattern used to validate the format of a Chilean RUT (Rol Único Tributario).
     * The pattern ensures that the RUT:
     * - Contains 7 or 8 digits followed by a hyphen ('-').
     * - Ends with a check digit, which can be a number (0-9) or the letter 'k'/'K'.
     *
     * This pattern is primarily used to perform a structural validation of the RUT
     * before further validation of its check digit.
     */
    private static final Pattern RUT_PATTERN = Pattern.compile("^\\d{7,8}-[\\dkK]$");

    /**
     * Initializes the validator for a RUT constraint.
     *
     * @param constraintAnnotation the ValidRut constraint annotation instance
     */
    @Override
    public void initialize(ValidRut constraintAnnotation) {
    }

    /**
     * Validates a Chilean RUT (Rol Único Tributario) to ensure it conforms to the standard
     * format and includes a correct verification digit (DV).
     *
     * @param rut the RUT string to be validated, expected to follow the pattern "NNNNNNNN-DV"
     *            where N is a numeric digit and DV is a numeric digit or 'K'
     * @param context the context in which the constraint is evaluated
     * @return true if the RUT is valid according to the format and its DV is correctly
     *         calculated; false otherwise
     */
    @Override
    public boolean isValid(String rut, ConstraintValidatorContext context) {
        if (rut == null || rut.isBlank()) {
            return false;
        }

        if (!RUT_PATTERN.matcher(rut).matches()) {
            return false;
        }

        try {
            String[] parts = rut.split("-");
            int rutBody = Integer.parseInt(parts[0]);
            char dv = parts[1].toUpperCase().charAt(0);

            int m = 0, s = 1;
            for (; rutBody != 0; rutBody /= 10) {
                s = (s + rutBody % 10 * (9 - m++ % 6)) % 11;
            }
            char calculatedDv = (char) (s != 0 ? s + 47 : 75);

            return dv == calculatedDv;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

