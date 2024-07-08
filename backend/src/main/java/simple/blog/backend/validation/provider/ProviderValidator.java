package simple.blog.backend.validation.provider;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import simple.blog.backend.enums.Provider;

public class ProviderValidator implements ConstraintValidator<ValidProvider, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext arg1) {
        if (value == null) {
            return false;
        }
        try {
            Provider.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
