package com.xiaoju.basetech.util;

/**
 * @description:
 * @author: gaoweiwei_v
 * @time: 2019/6/20 4:28 PM
 */

import org.eclipse.jgit.api.Git;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.FS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

@Component
public class GitHandler {
    static final Logger logger = LoggerFactory.getLogger(GitHandler.class);

    @Value(value = "${gitlab.username}")
    private  String username;

    @Value(value = "${gitlab.password}")
    private  String password;

    public Git cloneRepository(String gitUrl, String codePath, String commitId) throws GitAPIException {
        Git git = Git.cloneRepository()
                .setURI(gitUrl)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                .setDirectory(new File(codePath))
                .setBranch(commitId)
                .call();
        // 切换到指定commitId
        checkoutBranch(git, commitId);
        return git;
    }

    private static Ref checkoutBranch(Git git, String branch) {
        try {
            return git.checkout()
                    .setName(branch)
                    .call();
        } catch (GitAPIException e) {
            throw new IllegalStateException(e);
        }
    }

    public static boolean isValidGitRepository(String codePath) {
        Path folder = Paths.get(codePath);
        if (Files.exists(folder) && Files.isDirectory(folder)) {
            // If it has been at least initialized
            if (RepositoryCache.FileKey.isGitRepository(folder.toFile(), FS.DETECTED)) {
                // we are assuming that the clone worked at that time, caller should call hasAtLeastOneReference
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


}