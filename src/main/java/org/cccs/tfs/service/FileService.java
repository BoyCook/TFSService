package org.cccs.tfs.service;

import org.cccs.tfs.domain.File;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * User: boycook
 * Date: 29/03/2011
 * Time: 21:22
 */
public class FileService extends BaseService<File> {

    @Override
    public File create(File file) {
        validate(file);

        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction txn = entityManager.getTransaction();
        final Session session = (Session) entityManager.getDelegate();

        try {
            txn.begin();
            session.saveOrUpdate(file);
            txn.commit();

        } catch (Exception e) {
            log.error("Error creating " + file.getName());
            log.error(e.getMessage());
            e.printStackTrace();

            if(txn.isActive())
                txn.rollback();

        } finally {
            if(txn.isActive())
                txn.rollback();
            session.close();
        }

        return file;
    }

    @Override
    public File update(File file) {
        validate(file);
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction txn = entityManager.getTransaction();
        final Session session = (Session) entityManager.getDelegate();

        txn.begin();
        session.update(file);
        txn.commit();
        session.close();

        return file;
    }

    @Override
    public boolean delete(File file) {
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction txn = entityManager.getTransaction();
        final Session session = (Session) entityManager.getDelegate();

        txn.begin();
        File p = entityManager.merge(file);
        session.delete(p);
        txn.commit();
        session.close();

        return true;
    }

    @Override
    protected void validate(File file) {
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<File>> violations = validator.validate(file);

        if (violations.size() > -0) {
            log.debug(file.toString() + " has " + violations.size() + " violations");
            ConstraintViolation violation = (ConstraintViolation) violations.toArray()[0];
            throw new ValidationException("File validation has failed: " + violation.getMessageTemplate());
        }
    }
}
