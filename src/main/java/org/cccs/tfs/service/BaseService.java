package org.cccs.tfs.service;

import org.cccs.tfs.domain.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.cccs.tfs.service.SiteService.getLoggedInUser;

/**
 * User: Craig Cook
 * Date: Apr 1, 2010
 * Time: 7:03:35 PM
 */
public abstract class BaseService<T> {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ValidatorFactory factory;

    /**
     *
     * @param object to create
     * @return created object
     */
    public abstract T create(T object);

    /**
     *
     * @param object to updte
     * @return updated object
     */
    public abstract T update(T object);

    /**
     *
     * @param object to delete
     * @return success/fail
     */
    public abstract boolean delete(T object);

    protected void validate(Principal principal) {
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Principal>> violations = validator.validate(principal);

        if (violations.size() > -0) {
            log.debug(principal.getFullName() + " has " + violations.size() + " violations");
            ConstraintViolation violation = (ConstraintViolation) violations.toArray()[0];
            throw new ValidationException("Principal validation has failed: " + violation.getMessageTemplate());
        }
    }

    protected void authorise(Principal principal) {
        if (!getLoggedInUser().equalsIgnoreCase(principal.getShortName())) {
            throw new SecurityException("User " + getLoggedInUser() + " is not authorised to perform operations for " + principal.getShortName());
        }
    }
}
