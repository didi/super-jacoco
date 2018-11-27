package com.xiaoju.hallowmas.util;

import com.google.common.base.Preconditions;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Random;

/**
 * Created by wangping on 17/2/15.
 */
public class GitOperator {
    private String userName;
    private String password;

    private static final Logger LOG = LoggerFactory.getLogger(GitOperator.class);

    static Random ran = new Random();

    /**
     * 获取指定分支
     *
     * @param url
     * @param branchName
     */
    public void pullSpecifiedBranch(String url, String branchName, String localRepositoryPath) {
        LOG.info("want to create new branch with source:" + branchName + ", in url:" + url);
        try {
            verifyUrl(url);
            boolean sourceExist = isExistedBranch(url, branchName, localRepositoryPath);
            LOG.info("sourceExist:" + sourceExist);
            if (sourceExist) {
                File tempDir = new File(localRepositoryPath);
                getGit(url, tempDir, branchName);
                LOG.info("pull branch '" + branchName + "'  ");
            } else {
                LOG.error("branch state is illegal");
                throw new IllegalArgumentException("branch state");
            }
        } catch (Exception e) {
            String errMsg = "Failed to create new branch. e:" + e.getMessage();
            LOG.error(errMsg);
        }
    }

    private boolean isExistedBranch(String url, String branchName, String localRepositoryPath) {
        File tempDir = requestTempDir(localRepositoryPath);
        try {

            Git git = getGit(url, tempDir, branchName);
            if (git == null) {
                return false;
            }
            List<Ref> list = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call();

            for (Ref ref : list) {
                if (ref.getName().equals("refs/remotes/origin/" + branchName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            cleanupFile(tempDir);
        }

        return false;
    }

    /**
     * 在操作本地Git仓库时先申请一个本地的临时存储目录
     *
     * @return
     */
    private static File requestTempDir(String localRepositoryPath) {
        File tempDir = new File(localRepositoryPath + "/tempDir" + ran.nextInt(200));
        return tempDir;
    }

    /**
     * 用完本地临时目录后,立即释放
     *
     * @param tempFile
     */
    private void cleanupFile(File tempFile) {
        if (tempFile.isDirectory()) {
            File[] subFiles = tempFile.listFiles();
            for (File file : subFiles) {
                cleanupFile(file);
            }
        }

        tempFile.delete();
    }

    private Git getGit(String url, File localPath, String branchName) throws Exception {
        if (org.apache.commons.lang.StringUtils.isBlank(url) || org.apache.commons.lang.StringUtils.isBlank(branchName)) {
            return null;
        }
        return Git.cloneRepository().setURI(url)
                .setDirectory(localPath).setBranch(branchName).setCredentialsProvider(new UsernamePasswordCredentialsProvider(getUserName(), getPassword())).call();

    }

    private void verifyUrl(String url) throws IllegalArgumentException {
        Preconditions.checkArgument(url != null && url.length() > 10, "illegal url");
        Preconditions.checkArgument(url.startsWith("git") || url.startsWith("https"), "illegal url header");
        Preconditions.checkArgument(url.endsWith("git"), "illegal url tailer");
    }

    public GitOperator(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
