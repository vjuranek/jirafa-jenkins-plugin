package org.jenkinsci.plugins.jirafa.service.to;

/**
 * Encapsulation object for JIRA issues search criteria.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class SearchCriteria {

    private String testName;
    private String packageName;
    private String methodName;

    public SearchCriteria(String methodName, String testName, String packageName) {
        this.methodName = methodName;
        this.packageName = packageName;
        this.testName = testName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }
}
