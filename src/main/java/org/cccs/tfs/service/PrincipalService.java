package org.cccs.tfs.service;

import org.cccs.tfs.domain.Location;
import org.cccs.tfs.domain.Principal;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * User: boycook
 * Date: 10/02/2011
 * Time: 21:06
 */
public class PrincipalService extends BaseService<Principal>{

    @Override
    public Principal create(Principal principal) {
        validate(principal);

        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction txn = entityManager.getTransaction();
        final Session session = (Session) entityManager.getDelegate();

        try {
            txn.begin();
            session.saveOrUpdate(principal);
            txn.commit();

        } catch (Exception e) {
            log.error("Error creating Principal " + principal.getFullName());
            log.error(e.getMessage());
            e.printStackTrace();

            if(txn.isActive())
                txn.rollback();
        } finally {
            if(txn.isActive())
                txn.rollback();
            session.close();
        }

        return principal;
    }

    @Override
    public Principal update(Principal principal) {
        return update(principal, true);
    }

    public Principal update(Principal principal, boolean authorise) {
        validate(principal);

        if (authorise) {
            authorise(principal);
        }

		final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction txn = entityManager.getTransaction();
        final Session session = (Session) entityManager.getDelegate();

        txn.begin();
        session.update(principal);
        txn.commit();
        session.close();

        return principal;
    }

    @Override
    public boolean delete(Principal principal) {
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction txn = entityManager.getTransaction();
        final Session session = (Session) entityManager.getDelegate();

        txn.begin();
        Principal p = entityManager.merge(principal);
        session.delete(p);
        txn.commit();
        session.close();

        return true;
    }

    @Override
    protected void validate(Principal principal) {
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Principal>> violations = validator.validate(principal);

        if (violations.size() > -0) {
            log.debug(principal.getFullName() + " has " + violations.size() + " violations");
            ConstraintViolation violation = (ConstraintViolation) violations.toArray()[0];
            throw new ValidationException("Principal validation has failed: " + violation.getMessageTemplate());
        }
    }

    public Principal addFriend(Principal principal, Principal friend) {
        if (principal.getFriendRequests().contains(friend)) {
            principal.addFriend(friend);
            principal.removeFriendRequest(friend);
            return update(principal);
        } else {
            throw new ValidationException(friend.getFullName() + " has not made a friend request to accept");
        }
    }

    public Principal removeFriend(Principal principal, Principal friend) {
        if (principal.getFriends().contains(friend)) {
            principal.removeFriend(friend);
            return update(principal);
        } else {
            throw new ValidationException(friend.getFullName() + " is not friend and cannot be removed");
        }
    }

    public Principal addFriendRequest(Principal principal, Principal friend) {
        if (principal.getFriendRequests().contains(friend)) {
            throw new ValidationException(friend.getFullName() + " is already a friend request");
        } else {
            principal.addFriendRequest(friend);
            return update(principal, false);
        }
    }

    public Principal removeFriendRequest(Principal principal, Principal friend) {
        if (principal.getFriendRequests().contains(friend)) {
            principal.removeFriendRequest(friend);
            return update(principal);
        } else {
            throw new ValidationException(friend.getFullName() + " has not made a friend request to decline");
        }
    }

    public Principal updateLocation(Principal principal, Location location) {
        principal.setLocation(location);
        return update(principal);
    }
}

