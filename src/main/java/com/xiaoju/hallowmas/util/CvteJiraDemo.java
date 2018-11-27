package com.xiaoju.hallowmas.util;/*
package com.xiaoju.testReport.util;

import com.atlassian.jira.rest.client.IssueRestClient;
import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.SearchRestClient;
import com.atlassian.jira.rest.client.domain.*;
import com.atlassian.jira.rest.client.domain.input.ComplexIssueInputFieldValue;
import com.atlassian.jira.rest.client.domain.input.FieldInput;
import com.atlassian.jira.rest.client.domain.input.IssueInput;
import com.atlassian.jira.rest.client.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutionException;

*/
/**
 * Created by yehonggang on 17/5/18.
 *//*

public class CvteJiraDemo {
    public static String BaseURL = "http://10.0.50.185:9091";
    public static String User = "";
    public static String Password = "";
    private static URI jiraServerUri = URI
            .create("http://10.0.50.185:9091/");
    private static boolean quiet = false;
    private static final long BUG_TYPE_ID = 1L; // JIRA magic value
    private static final long TASK_TYPE_ID = 3L; // JIRA magic value
    private static final DateTime DUE_DATE = new DateTime();
    private static final String PRIORITY = "Trivial";
    private static final String DESCRIPTION = "description";


    public static void main(String[] args) throws InterruptedException,
            ExecutionException {

        final AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
        URI jiraServerUri;
        try {
            jiraServerUri = new URI(BaseURL);
            final JiraRestClient restClient = (JiraRestClient) factory.createWithBasicHttpAuthentication(jiraServerUri, User,
                            Password);
            getAllProjects(restClient);
            getProject(restClient, "SERVICES");
            getIssue(restClient, "SERVICES-299");
            getIssueFields(restClient, "FEEDBACK-27");
            addIssue(restClient, "FEEDBACK", "AAAAB");
            addIssueComplex(restClient, "FEEDBACK", DUE_DATE.toString());

        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

        }
    }

    private static void println(Object o) {
        if (!quiet) {
            System.out.println(o);
        }
    }

    private static void parseArgs(String[] argsArray) throws URISyntaxException {
        final List<String> args = Lists.newArrayList(argsArray);
        if (args.contains("-q")) {
            quiet = true;
            args.remove(args.indexOf("-q"));
        }

        if (!args.isEmpty()) {
            jiraServerUri = new URI(args.get(0));
        }
    }

    private static Transition getTransitionByName(
            Iterable<Transition> transitions, String transitionName) {
        for (Transition transition : transitions) {
            if (transition.getName().equals(transitionName)) {
                return transition;
            }
        }
        return null;
    }

    // 得到所有项目信息
    private static void getAllProjects(final JiraRestClient restClient)
            throws InterruptedException, ExecutionException {
        try {

            Promise<Iterable<BasicProject>> list = restClient
                    .getProjectClient().getAllProjects();
            Iterable<BasicProject> a = list.get();
            Iterator<BasicProject> it = a.iterator();
            while (it.hasNext()) {
                System.out.println(it.next());
            }

        } finally {
        }
    }

    // 得到单一项目信息
    private static void getProject(final JiraRestClient restClient,
                                   String porjectKEY) throws InterruptedException, ExecutionException {
        try {

            Project project = restClient.getProjectClient()
                    .getProject(porjectKEY).get();
            System.out.println(project);

        } finally {
        }
    }

    // 得到单一问题信息
    private static void getIssue(final JiraRestClient restClient,
                                 String issueKEY) throws InterruptedException, ExecutionException {
        try {

            Promise<Issue> list = restClient.getIssueClient()
                    .getIssue(issueKEY);
            Issue issue = list.get();
            System.out.println(issue);

        } finally {
        }
    }

    // 创建问题
    public static BasicIssue createIssue(final JiraRestClient jiraRestClient,
                                         IssueInput newIssue) {
        BasicIssue basicIssue = jiraRestClient.getIssueClient()
                .createIssue(newIssue).claim();
        return basicIssue;
    }

    //添加备注到问题
    public static void addCommentToIssue(final JiraRestClient jiraRestClient, Issue issue, String comment) {
        IssueRestClient issueClient = jiraRestClient.getIssueClient();
        issueClient.addComment(issue.getCommentsUri(), Comment.valueOf(comment)).claim();
    }


    //删除问题，目前找不到对应API
    public static void deleteIssue(final JiraRestClient jiraRestClient, Issue issue) {
        IssueRestClient issueClient = jiraRestClient.getIssueClient();
        //issueClient.deleteIssue(issue.getKey(), false).claim();
    }

    //通过标题获取问题
    public static Iterable findIssuesByLabel(final JiraRestClient jiraRestClient, String label) {
        SearchRestClient searchClient = jiraRestClient.getSearchClient();
        String jql = "labels%3D" + label;
        com.atlassian.jira.rest.client.domain.SearchResult results = ((SearchRestClient) jiraRestClient).searchJql(jql).claim();
        return results.getIssues();
    }

    //通过KEY获取问题
    public static Issue findIssueByIssueKey(final JiraRestClient jiraRestClient, String issueKey) {
        SearchRestClient searchClient = jiraRestClient.getSearchClient();
        String jql = "issuekey = \"" + issueKey + "\"";
        SearchResult results = searchClient.searchJql(jql).claim();
        return (Issue) results.getIssues().iterator().next();
    }

    // 创建问题 ：仅有简单问题名称
    private static void addIssue(final JiraRestClient restClient,
                                 String porjectKEY, String issueName) throws InterruptedException,
            ExecutionException {
        try {
            IssueInputBuilder builder = new IssueInputBuilder(porjectKEY,
                    TASK_TYPE_ID, issueName);
            builder.setDescription("issue description");
            final IssueInput input = builder.build();

            try {
                // create issue
                final IssueRestClient client = restClient.getIssueClient();
                final BasicIssue issue = client.createIssue(input).claim();
                final Issue actual = client.getIssue(issue.getKey()).claim();
                System.out.println(actual);
            } finally {
                if (restClient != null) {
                    // restClient.close();
                }
            }

        } finally {
        }
    }

    // 创建问题 ：包含自定义字段
    private static void addIssueComplex(final JiraRestClient restClient,
                                        String porjectKEY, String issueName) throws InterruptedException,
            ExecutionException {
        try {
            IssueInputBuilder builder = new IssueInputBuilder(porjectKEY,
                    TASK_TYPE_ID, issueName);
            builder.setDescription("issue description");
            // builder.setFieldValue("priority", ComplexIssueInputFieldValue.with("name", PRIORITY));
            //单行文本
            builder.setFieldValue("customfield_10042", "单行文本测试");

            //单选字段
            builder.setFieldValue("customfield_10043", ComplexIssueInputFieldValue.with("value", "一般"));

            //数值自定义字段
            builder.setFieldValue("customfield_10044", 100.08);

            //用户选择自定义字段
            builder.setFieldValue("customfield_10045", ComplexIssueInputFieldValue.with("name", "admin"));
            //用户选择自定义字段(多选)
            Map<String, Object> user1 = new HashMap<String, Object>();
            user1.put("name", "admin");
            Map<String, Object> user2 = new HashMap<String, Object>();
            user2.put("name", "wangxn");
            ArrayList peoples = new ArrayList();
            peoples.add(user1);
            peoples.add(user2);
            builder.setFieldValue("customfield_10047", peoples);

            //设定父问题
            Map<String, Object> parent = new HashMap<String, Object>();
            parent.put("key", "FEEDBACK-25");
            FieldInput parentField = new FieldInput("parent", new ComplexIssueInputFieldValue(parent));
            builder.setFieldInput(parentField);

            final IssueInput input = builder.build();
            try {
                final IssueRestClient client = restClient.getIssueClient();
                final BasicIssue issue = client.createIssue(input).claim();
                final Issue actual = client.getIssue(issue.getKey()).claim();
                System.out.println(actual);
            } finally {
                if (restClient != null) {
                    // restClient.close();
                }
            }

        } finally {
        }
    }


    //获取问题的所有字段
    private static void getIssueFields(final JiraRestClient restClient,
                                       String issueKEY) throws InterruptedException, ExecutionException {
        try {

            Promise<Issue> list = restClient.getIssueClient()
                    .getIssue(issueKEY);
            Issue issue = list.get();
            Iterable<Field> fields = issue.getFields();
            Iterator<Field> it = fields.iterator();
            while (it.hasNext()) {
                System.out.println(it.next());
            }

        } finally {
        }
    }
}
*/
