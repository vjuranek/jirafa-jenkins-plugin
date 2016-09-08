package org.jenkinsci.plugins.jirafa.controller;

import hudson.matrix.MatrixConfiguration;
import hudson.model.Job;
import hudson.tasks.junit.CaseResult;
import hudson.tasks.test.MetaTabulatedResult;
import hudson.tasks.test.TestResult;
import org.jenkinsci.plugins.jirafa.entity.FoundIssue;
import org.jenkinsci.plugins.jirafa.entity.Link;
import org.jenkinsci.plugins.jirafa.service.LinkService;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class ExtendedMetaTabulatedResult {

    private MetaTabulatedResult metaTabulatedResult;

    private Map<String, List<Link>> testsWithLinks;
    private Map<String, List<FoundIssue>> testsWithFoundIssues;
    private Map<String, CaseResult> caseResults;

    private LinkService linkService;

    public void initMetaTabulatedResult(MetaTabulatedResult metaTabulatedResult) {
        this.metaTabulatedResult = metaTabulatedResult;
        testsWithLinks = new HashMap<String, List<Link>>();
        testsWithFoundIssues = new HashMap<String, List<FoundIssue>>();
        caseResults = new HashMap<String, CaseResult>();

        String jobName;
        Job job = metaTabulatedResult.getRun().getParent();
        if (job instanceof MatrixConfiguration) {
            jobName = job.getParent().getDisplayName();
        } else {
            jobName = job.getName();
        }

        linkService = new LinkService(jobName);

        for (TestResult item: metaTabulatedResult.getFailedTests()) {
            if (!(item instanceof CaseResult)) {
                throw new IllegalStateException("Unexpected objects in collection. Failed tests are not instances of CaseResult!");
            }
            CaseResult caseResult = (CaseResult) item;
            String testName = caseResult.getFullDisplayName();
            caseResults.put(testName, caseResult);
            ExtendedCaseResult extendedCaseResult = new ExtendedCaseResult();
            extendedCaseResult.initCaseResult(caseResult);

            List<Link> linkedIssues = extendedCaseResult.getLinkedIssues();
            if (linkedIssues != null && !linkedIssues.isEmpty()) {
                testsWithLinks.put(testName, linkedIssues);
            }

            List<FoundIssue> foundIssues = extendedCaseResult.getFoundIssues();
            if (foundIssues != null && !foundIssues.isEmpty()) {
                testsWithFoundIssues.put(testName, foundIssues);
            }
        }
    }

    @JavaScriptMethod
    public void linkIssuesToTest(String test, List<String> issueKeys, String stacktrace) {
        for (FoundIssue issue: testsWithFoundIssues.get(test)) {
            if (issueKeys.contains(issue.getKey())) {
                linkService.linkIssueToTest(
                        issue.getKey(),
                        issue.getSummary(),
                        test,
                        stacktrace
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

    @JavaScriptMethod
    public void linkIssuesToAll(List<String> tests, List<List<String>> issueKeys) {
        for (int i = 0; i < tests.size(); i++) {
            List<String> issueKeysForTest = issueKeys.get(i);
            linkIssuesToTest(tests.get(i), issueKeysForTest, caseResults.get(tests.get(i)).getErrorStackTrace());
        }
    }

    public static String getJiraURL() {
        return ExtendedCaseResult.getJiraURL();
    }

    public static ExtendedMetaTabulatedResult newInstance() {
        return new ExtendedMetaTabulatedResult();
    }

    public Map<String, List<FoundIssue>> getTestsWithFoundIssues() {
        return testsWithFoundIssues;
    }

    public Map<String, List<Link>> getTestsWithLinks() {
        return testsWithLinks;
    }
}
