package org.jenkinsci.plugins.jirafa.dao;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jenkinsci.plugins.jirafa.entity.Link;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DAO for {@link Link}.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class LinkDao extends AbstractDao<Link> {

    public LinkDao(EntityManager em) {
        super(em);
    }

    public List<Link> findByTestAndStacktrace(String test, String stacktrace) {
        TypedQuery<Link> tq = em.createNamedQuery(Link.FIND_BY_TEST_AND_STACKTRACE, Link.class);
        tq.setParameter("test", test);
        tq.setParameter("stacktrace", stacktrace);

        List<Link> result = tq.getResultList();
        return result;
    }

    public List<Link> getAll() {
        TypedQuery<Link> tq = em.createNamedQuery(Link.GET_ALL, Link.class);
        List<Link> result = tq.getResultList();
        return result;
    }

}
