package simple.blog.backend.validation.provider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProviderValidator.class)
public @interface ValidProvider {
    String message() default "Invalid provider";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
