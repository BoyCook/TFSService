package org.cccs.tfs.service;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * User: boycook
 * Date: 13/02/2011
 * Time: 16:03
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=UniqueConstraintValidator.class)
@Documented
public @interface Unique {
    String message() default "Unique constraint violation has occurred";
    Class<?> entity();
    String idField();
    String[] uniqueFields();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
