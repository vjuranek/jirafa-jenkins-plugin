package org.jenkinsci.plugins.jirafa.dao;

import org.jenkinsci.plugins.jirafa.entity.FoundIssue;
import org.jenkinsci.plugins.jirafa.entity.Test;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO for {@link Test}.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class TestDao extends AbstractDao<Test> {

    public TestDao(EntityManager em) {
        super(em);
    }

    public Test getByName(String name) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        List<Test> result = findByNamedQuery(Test.GET_BY_NAME, params);

        return result.isEmpty() ? null : result.get(0);
    }

}
