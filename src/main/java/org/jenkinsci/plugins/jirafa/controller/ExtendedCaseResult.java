package org.jenkinsci.plugins.jirafa.controller;

import hudson.matrix.MatrixConfiguration;
import hudson.matrix.MatrixProject;
import hudson.model.*;
import hudson.tasks.Publisher;
import hudson.tasks.junit.CaseResult;
import hudson.tasks.junit.JUnitResultArchiver;
import hudson.tasks.junit.TestDataPublisher;
import hudson.util.DescribableList;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.jirafa.JirafaPrecomputer;
import org.jenkinsci.plugins.jirafa.entity.FoundIssue;
import org.jenkinsci.plugins.jirafa.entity.Link;
import org.jenkinsci.plugins.jirafa.service.FoundIssueService;
import org.jenkinsci.plugins.jirafa.service.JiraFinderService;
import org.jenkinsci.plugins.jirafa.service.LinkService;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class ExtendedCaseResult {

    private CaseResult caseResult;
    private List<Link> linkedIssues;
    private List<FoundIssue> foundIssues;
    private LinkService linkService;
    private FoundIssueService foundIssueService;

    private static String jiraUrl;
    private static String filter;
    private static String username;
    private static String password;

    /**
     * Init method that sets up everything.
     *
     * @param caseResult
     */
    public void initCaseResult(CaseResult caseResult) {
        this.caseResult = caseResult;

        String jobName;
        Job job = caseResult.getRun().getParent();
        if (job instanceof MatrixConfiguration) {
            jobName = job.getParent().getDisplayName();
        } else {
            jobName = job.getName();
        }
        initJiraProperties(job);

        linkService = new LinkService(jobName);
        foundIssueService = new FoundIssueService();

        linkedIssues = linkService.findIssuesByTestAndStacktrace(caseResult.getFullName(), caseResult.getErrorStackTrace());
        if (linkedIssues == null || linkedIssues.isEmpty()) {
            foundIssues = foundIssueService.getFoundIssuesForTest(caseResult.getFullName());
        }
    }

    @JavaScriptMethod
    public void searchIssues() {
        /*jiraFinderService.connect();

        foundIssues = jiraFinderService.search(new SearchCriteria(caseResult.getName(), caseResult.getSimpleName(), caseResult.getPackageName()));

        jiraFinderService.close();*/
    }

    @JavaScriptMethod
    public List<IssueTO> getFoundIssueTOs() {
        List<IssueTO> issues = new ArrayList<IssueTO>();
        if (foundIssues != null) {
            for (FoundIssue issue : foundIssues) {
                IssueTO newIssue = new IssueTO(issue.getKey(), issue.getSummary(), issue.getDescription());
                issues.add(newIssue);
            }
        }
        return issues;
    }

    @JavaScriptMethod
    public void linkIssues(List<String> issueKeys) {
        for (FoundIssue issue: foundIssues) {
            if (issueKeys.contains(issue.getKey())) {
                linkService.linkIssueToTest(
                        issue.getKey(),
                        issue.getSummary(),
                        caseResult.getFullDisplayName(),
                        caseResult.getErrorStackTrace()
                );
            }
        }
    }

    @JavaScriptMethod
    public void deleteLinks(Long[] ids) {
        for (Long id: ids) {
            linkService.delete(id);
        }
    }

    public static String getJiraURL() {
        return jiraUrl + "browse/";
    }

    public static ExtendedCaseResult newInstance() {
        return new ExtendedCaseResult();
    }

    public CaseResult getCaseResult() {
        return caseResult;
    }

    public void setCaseResult(CaseResult caseResult) {
        this.caseResult = caseResult;
    }

    public void setFoundIssues(List<FoundIssue> foundIssues) {
        this.foundIssues = foundIssues;
    }

    public List<Link> getLinkedIssues() {
        return linkedIssues;
    }

    public void setLinkedIssues(List<Link> linkedIssues) {
        this.linkedIssues = linkedIssues;
    }

    public List<FoundIssue> getFoundIssues() {
        return foundIssues;
    }

    private void initJiraProperties(Job job) {
        Project project;
        if (job instanceof MatrixConfiguration) {
            project = (MatrixConfiguration) job;
        } else {
            project = (FreeStyleProject) job;
        }
        DescribableList<Publisher, Descriptor<Publisher>> publishers =  project.getPublishersList();

        JUnitResultArchiver archiver = publishers.get(JUnitResultArchiver.class);
        List<TestDataPublisher> testDataPublishers = archiver.getTestDataPublishers();
        for (TestDataPublisher testDataPublisher: testDataPublishers) {
            if (testDataPublisher instanceof JirafaPrecomputer) {
                JirafaPrecomputer jirafaPrecomputer = (JirafaPrecomputer) testDataPublisher;
                jiraUrl = jirafaPrecomputer.getJiraUrl();
                filter = jirafaPrecomputer.getFilter();
                username = jirafaPrecomputer.getUsername();
                password = jirafaPrecomputer.getPassword();
                break;
            }
        }
    }
}
