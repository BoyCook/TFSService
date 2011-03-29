package org.cccs.tfs.service;

import org.cccs.tfs.domain.File;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 * User: boycook
 * Date: 29/03/2011
 * Time: 21:22
 */
public class FileService extends BaseService<File> {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public File create(File file) {
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
}
