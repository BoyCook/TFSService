package org.cccs.tfs.finder;

import org.cccs.tfs.domain.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * User: boycook
 * Date: 29/03/2011
 * Time: 22:53
 */
public class FileFinder implements Finder<File> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public List<File> all() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
        List<File> result = entityManager.createQuery("from File", File.class).getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();

        log.debug("Found all Files: " + result.size());
        return result;
    }

    @Override
    public File find(Object businessKey) {
        throw new UnsupportedOperationException("There is no single unique business key for a File");
    }

    @Override
    public List<File> filter(Map<String, String[]> parameters) {
        return search(parameters, true);
    }

    @Override
    public List<File> search(Map<String, String[]> parameters) {
        return search(parameters, false);
    }

    @Override
    public List<File> search(String name) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
        List<File> result = entityManager
                        .createQuery( "from File where upper(artefactId) like upper(?1) ", File.class)
                        .setParameter(1, "%" + name + "%").getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();

        log.debug(format("Found '%s' File: %d", name, result.size()));
        return result;
    }

    private List<File> search(Map<String, String[]> parameters, boolean or) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

        String sql = "from File ";

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

        TypedQuery<File> query = entityManager
                .createQuery(sql, File.class);

        i=1;
        for (String key: parameters.keySet()) {
            String[] values = parameters.get(key);

            for (String value: values) {
                query.setParameter(i, "%" + value + "%");
                i++;
            }
        }

        List<File> result = query.getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();

        return result;
    }
}
