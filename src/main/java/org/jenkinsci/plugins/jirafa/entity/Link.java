package org.jenkinsci.plugins.jirafa.entity;

import org.jenkinsci.plugins.database.jpa.PerItemTable;

import javax.persistence.*;

/**
 * Database entity to store the hard link between test failure and JIRA issue.
 *
 * I know that this entity doesn't conform to normalize rules, but since it's extremely simple application,
 * I wanted to keep it as simple as possible even at the bottom layer, also to make it fast (we don't have to do joins).
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
@PerItemTable
@NamedQueries({
        @NamedQuery(name = Link.FIND_BY_TEST_AND_STACKTRACE, query = "SELECT link FROM Link link WHERE link.test = :test AND link.stacktrace = :stacktrace"),
        @NamedQuery(name = Link.GET_ALL, query = "SELECT link FROM Link link")
})
@javax.persistence.Entity
public class Link implements Entity {

    public static final String FIND_BY_TEST_AND_STACKTRACE = "Link.findByTestAndStacktrace";
    public static final String GET_ALL = "Link.getAll";

    @Id
    @SequenceGenerator(name = "LINK_ID_GENERATOR", sequenceName = "LINK_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LINK_ID_GENERATOR")
    private Long id;

    @Column
    private String issue;

    @Column
    private String test;

    @Column
    private String summary;

    @Column
    private String stacktrace;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "Link{" +
                ", id=" + id +
                ", issue='" + issue + '\'' +
                ", test='" + test + '\'' +
                ", summary='" + summary + '\'' +
                ", stacktrace='" + stacktrace + '\'' +
                '}';
    }
}
