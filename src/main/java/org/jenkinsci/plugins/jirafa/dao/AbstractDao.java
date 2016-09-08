package org.jenkinsci.plugins.jirafa.dao;

import org.jenkinsci.plugins.jirafa.entity.Entity;
import org.jenkinsci.plugins.jirafa.entity.Link;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DAO for {@link AbstractDao}.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public abstract class AbstractDao<T extends Entity> {

    private Class<T> type;
    protected EntityManager em;

    @SuppressWarnings("unchecked")
    public AbstractDao(EntityManager em) {
        this.em = em;

        if (getClass().getGenericSuperclass() instanceof ParameterizedType) {
            type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        } else {
            type = (Class<T>) ((ParameterizedType) getClass().getSuperclass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
    }

    public T get(Long id) {
        return em.find(type, id);
    }

    public T update(final T entity) {
        T stored = em.merge(entity);
        em.flush();
        return stored;
    }

    public T create(final T entity) {
        em.persist(entity);
        em.flush();
        return entity;
    }

    public void delete(final T entity) {
        em.remove(entity);
        em.flush();
    }

    /**
     * Return result of named query
     *
     * @param queryName
     * @param queryParams
     * @return List of entities corresponding to query
     */
    public List<T> findByNamedQuery(String queryName, Map<String, Object> queryParams) {
        TypedQuery<T> tq = em.createNamedQuery(queryName, type);
        for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
            tq.setParameter(entry.getKey(), entry.getValue());
        }

        List<T> result1 = tq.getResultList();
        return result1;
    }

}
