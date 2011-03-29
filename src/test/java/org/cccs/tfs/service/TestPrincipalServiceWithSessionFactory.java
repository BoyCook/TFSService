package org.cccs.tfs.service;

import org.cccs.tfs.domain.Principal;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * User: boycook
 * Date: 11/02/2011
 * Time: 12:20
 */
@Ignore //need file hibernate.cfg.xml
public class TestPrincipalServiceWithSessionFactory {

    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        return sessionFactory;
    }

    private List findWithSF() {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        List result = session.createQuery( "from Principal" ).list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    private void createWithSF(Principal principal) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        session.save(principal );
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void createNewWithSFShouldWork() {
        Principal p = new Principal("BoyCook", "Craig", "Cook", "password", "craig.cook@bt.com", "07918880501");

        List listBefore = findWithSF();
        assertThat(listBefore.size(), is(equalTo(0)));

        createWithSF(p);

        List listAfter = findWithSF();
        assertThat(listAfter.size(), is(equalTo(1)));
    }


//		entityManager.getTransaction().begin();
//		entityManager.persist(principal);
//		entityManager.getTransaction().commit();
//		entityManager.close();

//    public EntityManagerFactory getEntityManagerFactory() {
//        if (entityManagerFactory == null) {
//            entityManagerFactory = Persistence.createEntityManagerFactory("org.cccs.tfs.jpa");
//        }
//        return entityManagerFactory;
//    }
}
