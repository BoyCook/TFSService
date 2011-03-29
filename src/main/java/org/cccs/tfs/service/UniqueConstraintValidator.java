package org.cccs.tfs.service;

import org.apache.commons.beanutils.BeanUtils;
import org.cccs.tfs.domain.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

import static org.cccs.tfs.utils.RuntimeSupport.isNotEmpty;

/**
 * User: boycook
 * Date: 13/02/2011
 * Time: 15:59
 */
public class UniqueConstraintValidator implements ConstraintValidator<Unique, Object> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    private Class<?> entity;
    private String idField;
    private String uniqueField;

    @Override
    public void initialize(Unique annotation) {
        this.entity = annotation.entity();
        this.idField = annotation.idField();
        this.uniqueField = annotation.uniqueField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String idValue = "";
        String uniqueValue = "";

        try {
            idValue = BeanUtils.getProperty(value, idField);
            uniqueValue = BeanUtils.getProperty(value, uniqueField);
        } catch (Exception e) {
            log.error("Error looking up field");
            log.error(e.getMessage());
        }

        return !idValue.equals("0") || isNotEmpty(uniqueValue) && query(uniqueValue) == 0;
    }

    private int query(String value) {
        //This is crap, but validate is being called on txn.commit() with no autowire
        if (entityManagerFactory != null) {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            StringBuilder query = new StringBuilder();
            query.append("from ");
            query.append(entity.getSimpleName());
            query.append(" where upper(");
            query.append(uniqueField);
            query.append(") like upper(?1)");

            List list = entityManager
                    .createQuery(query.toString(), Principal.class)
                    .setParameter(1, "%" + value + "%")
                    .getResultList();

            entityManager.getTransaction().commit();
            entityManager.close();

            return list.size();
        }
        return 0;
    }
}
