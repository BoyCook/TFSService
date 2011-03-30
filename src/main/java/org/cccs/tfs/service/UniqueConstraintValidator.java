package org.cccs.tfs.service;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

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
    private String[] uniqueFields;

    @Override
    public void initialize(Unique annotation) {
        this.entity = annotation.entity();
        this.idField = annotation.idField();
        this.uniqueFields = annotation.uniqueFields();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String idValue = "";
        String[] uniqueValues = new String[uniqueFields.length];

        try {
            idValue = BeanUtils.getProperty(value, idField);

            for (int i=0; i<uniqueFields.length; i++) {
                uniqueValues[i] = BeanUtils.getProperty(value, uniqueFields[i]);
            }
        } catch (Exception e) {
            log.error("Error looking up field");
            log.error(e.getMessage());
        }

        return !idValue.equals("0") || uniqueValues != null && query(uniqueValues) == 0;
    }

    private int query(String[] values) {
        //This is crap, but validate is being called on txn.commit() with no autowire
        if (entityManagerFactory != null) {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            StringBuilder query = new StringBuilder();
            query.append("from ");
            query.append(entity.getSimpleName());

            for (int i=0; i<uniqueFields.length; i++) {
                String join = (i==0) ? " where " : " and ";

                query.append(join);
                query.append("upper(");
                query.append(uniqueFields[i]);
                query.append(") like upper(?");
                query.append(i + 1);
                query.append(") ");
            }

            Query typeQuery = entityManager.createQuery(query.toString());

            for (int i=0; i<uniqueFields.length; i++) {
                typeQuery.setParameter(i+1, "%" + values[i] + "%");
            }

            List list = typeQuery.getResultList();

            entityManager.getTransaction().commit();
            entityManager.close();

            return list.size();
        }
        return 0;
    }
}
