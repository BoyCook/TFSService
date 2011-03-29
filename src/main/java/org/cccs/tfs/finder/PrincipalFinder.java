package org.cccs.tfs.finder;

import org.cccs.tfs.domain.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * User: boycook
 * Date: 11/02/2011
 * Time: 11:35
 */
public class PrincipalFinder implements Finder<Principal> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public List<Principal> all() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
        List<Principal> result = entityManager.createQuery("from Principal", Principal.class).getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();

        log.debug("Found all Principals: " + result.size());
        return result;
    }

    @Override
    public Principal find(Object name) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
        List<Principal> result = entityManager
                        .createQuery( "from Principal where upper(shortName) = upper(?1) ", Principal.class)
                        .setParameter(1, name).getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();

        if (result == null || result.size() ==0) {
            throw new EntityNotFoundException("Can't find: " + name);
        }

        Principal p = result.get(0);

        log.debug("Found Principal: " + p.getFullName());
        return p;
    }

    @Override
    public List<Principal> filter(Map<String, String[]> parameters) {
        return search(parameters, true);
    }

    @Override
    public List<Principal> search(String name) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
        List<Principal> result = entityManager
                        .createQuery( "from Principal where upper(shortName) like upper(?1) ", Principal.class)
                        .setParameter(1, "%" + name + "%").getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();

        log.debug(format("Found '%s' Principals: %d", name, result.size()));
        return result;
    }

    @Override
    public List<Principal> search(Map<String, String[]> parameters) {
        return search(parameters, false);
    }

    private List<Principal> search(Map<String, String[]> parameters, boolean or) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

        String sql = "from Principal ";

        StringBuilder where = new StringBuilder();

        int i=1;
        for (String key: parameters.keySet()) {
            String[] values = parameters.get(key);

            for (int x=0; x<values.length; x++) {
                String join = (i==1) ? " where " : (or ? " or " : " and ");

                where.append(join);
                where.append("upper(");
                where.append(key);
                where.append(") like upper(?");
                where.append(i);
                where.append(") ");
                i++;
            }
        }

        sql = sql + where.toString();

        TypedQuery<Principal> query = entityManager
                .createQuery(sql, Principal.class);

        i=1;
        for (String key: parameters.keySet()) {
            String[] values = parameters.get(key);

            for (String value: values) {
                query.setParameter(i, "%" + value + "%");
                i++;
            }
        }

        List<Principal> result = query.getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();

        return result;
    }
}
