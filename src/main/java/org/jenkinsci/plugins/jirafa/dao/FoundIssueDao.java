package org.jenkinsci.plugins.jirafa.dao;

import org.jenkinsci.plugins.jirafa.entity.FoundIssue;
import org.jenkinsci.plugins.jirafa.entity.Link;
import org.jenkinsci.plugins.jirafa.entity.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO for {@link FoundIssueDao}.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class FoundIssueDao extends AbstractDao<FoundIssue> {

    public FoundIssueDao(EntityManager em) {
        super(em);
    }

    public FoundIssue getByKey(String key) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("key", key);
        List<FoundIssue> result = findByNamedQuery(FoundIssue.GET_BY_KEY, params);

        return result.isEmpty() ? null : result.get(0);
    }

    public List<FoundIssue> getAll() {
        TypedQuery<FoundIssue> tq = em.createNamedQuery(FoundIssue.GET_ALL, FoundIssue.class);
        List<FoundIssue> result = tq.getResultList();
        return result;
    }


}
