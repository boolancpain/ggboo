package com.fyo.ggboo.infra.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

/**
 * Date Validator
 * 
 * @author boolancpain
 */
public class DateValidator implements ConstraintValidator<DateConstraint, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            if(StringUtils.hasText(value)) {
                // yyyy-MM-dd
                LocalDate.parse(value, DateTimeFormatter.ISO_DATE);
            }
        } catch (Exception e) {
            return false;
        }
        
        return true;
    }
}