package com.xiaoju.basetech.util;


/**
 * @description:
 * @author: charlyne
 * @time: 2019/6/13 10:44 AM
 */


import com.xiaoju.basetech.entity.CoverageReportEntity;
import jodd.io.FileUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDiffFiles {

    static final Logger logger = LoggerFactory.getLogger(JDiffFiles.class);

    public static HashMap<String, String> diffMethodsListNew(CoverageReportEntity coverageReport) {
        HashMap<String, String> map = new HashMap<>();

        if (coverageReport.getType() == Constants.ReportType.FULL.val()) {
            coverageReport.setRequestStatus(Constants.JobStatus.DIFF_METHOD_DONE.val());
            return map;
        }
        if (coverageReport.getBaseVersion().equals(coverageReport.getNowVersion())) {

            coverageReport.setErrMsg("两个commitid一致,没有增量代码");
            coverageReport.setRequestStatus(Constants.JobStatus.NODIFF.val());
            coverageReport.setReportUrl(Constants.NO_DIFFCODE_REPORT);
            coverageReport.setLineCoverage((double) 100);
            coverageReport.setBranchCoverage((double) 100);
            return map;
        }
        try {
            File newF = new File(coverageReport.getNowLocalPath());
            File oldF = new File(coverageReport.getBaseLocalPath());
            Git newGit;
            Git oldGit;
            Repository newRepository;
            Repository oldRepository;
            newGit = Git.open(newF);
            newRepository = newGit.getRepository();
            oldGit = Git.open(oldF);
            oldRepository = oldGit.getRepository();
            ObjectId baseObjId = oldGit.getRepository().resolve(coverageReport.getBaseVersion());
            ObjectId nowObjId = newGit.getRepository().resolve(coverageReport.getNowVersion());
            AbstractTreeIterator newTree = prepareTreeParser(newRepository, nowObjId);
            AbstractTreeIterator oldTree = prepareTreeParser(oldRepository, baseObjId);
            List<DiffEntry> diff = newGit.diff().setOldTree(oldTree).setNewTree(newTree).setShowNameAndStatusOnly(true).call();
            for (DiffEntry diffEntry : diff) {
                if (diffEntry.getChangeType() == DiffEntry.ChangeType.DELETE) {
                    continue;
                }
                if (diffEntry.getNewPath().indexOf(coverageReport.getSubModule()) < 0) {
                    continue;
                }
                if(diffEntry.getNewPath().indexOf("src/test/java")!=-1){
                    continue;
                }
                if (diffEntry.getNewPath().endsWith(".java")) {
                    String nowclassFile = diffEntry.getNewPath();
                    if (diffEntry.getChangeType() == DiffEntry.ChangeType.ADD) {
                        map.put(nowclassFile.replace(".java", ""), "true");
                    } else if (diffEntry.getChangeType() == DiffEntry.ChangeType.MODIFY) {
                        MethodParser methodParser = new MethodParser();
                        HashMap<String, String> baseMMap = methodParser.parseMethodsMd5(oldGit.getRepository().getDirectory().getParent() + "/" + nowclassFile);
                        HashMap<String, String> nowMMap = methodParser.parseMethodsMd5(newGit.getRepository().getDirectory().getParent() + "/" + nowclassFile);
                        HashMap<String, String> resMap = diffMethods(baseMMap, nowMMap);
                        if (resMap.isEmpty()) {
                            continue;
                        } else {
                            StringBuilder builder = new StringBuilder("");
                            for (String v : resMap.values()) {
                                builder.append(v + "#");
                            }
                            map.put(nowclassFile.replace(".java", ""), builder.toString());
                        }
                    }
                }
            }
            if (map.isEmpty()) {
                coverageReport.setLineCoverage((double) 100);
                coverageReport.setBranchCoverage((double) 100);
                coverageReport.setRequestStatus(Constants.JobStatus.SUCCESS.val());
                coverageReport.setReportUrl(Constants.NO_DIFFCODE_REPORT);
                // 删除下载的代码
                FileUtil.cleanDir(new File(coverageReport.getNowLocalPath()).getParent());
                coverageReport.setErrMsg("没有增量代码");
            } else {
                coverageReport.setRequestStatus(Constants.JobStatus.DIFF_METHOD_DONE.val());
            }
            return map;
        } catch (Exception e) {
            logger.error("计算增量方法出错uuid{}", coverageReport.getUuid(), e);
            coverageReport.setErrMsg("计算增量方法出错:" + e.getMessage());
            coverageReport.setRequestStatus(Constants.JobStatus.DIFF_METHOD_FAIL.val());
            return null;
        }
    }

    public static HashMap<String, String> diffMethodsListForEnv(String basePath,String nowPath,String baseVersion,String nowVersion) {
        HashMap<String, String> map = new HashMap<>();
        try {
            File newF = new File(nowPath);
            File oldF = new File(basePath);
            Git newGit;
            Git oldGit;
            Repository newRepository;
            Repository oldRepository;
            newGit = Git.open(newF);
            newRepository = newGit.getRepository();
            oldGit = Git.open(oldF);
            oldRepository = oldGit.getRepository();
            ObjectId baseObjId = oldGit.getRepository().resolve(baseVersion);
            ObjectId nowObjId = newGit.getRepository().resolve(nowVersion);
            AbstractTreeIterator newTree = prepareTreeParser(newRepository, nowObjId);
            AbstractTreeIterator oldTree = prepareTreeParser(oldRepository, baseObjId);
            List<DiffEntry> diff = newGit.diff().setOldTree(oldTree).setNewTree(newTree).setShowNameAndStatusOnly(true).call();
            for (DiffEntry diffEntry : diff) {
                if (diffEntry.getChangeType() == DiffEntry.ChangeType.DELETE) {
                    continue;
                }
                if(diffEntry.getNewPath().indexOf("src/test/java")!=-1){
                    continue;
                }
                if (diffEntry.getNewPath().endsWith(".java")) {
                    String nowclassFile = diffEntry.getNewPath();
                    if (diffEntry.getChangeType() == DiffEntry.ChangeType.ADD) {
                        map.put(nowclassFile.replace(".java", ""), "true");
                    } else if (diffEntry.getChangeType() == DiffEntry.ChangeType.MODIFY) {
                        MethodParser methodParser = new MethodParser();
                        HashMap<String, String> baseMMap = methodParser.parseMethodsMd5(oldGit.getRepository().getDirectory().getParent() + "/" + nowclassFile);
                        HashMap<String, String> nowMMap = methodParser.parseMethodsMd5(newGit.getRepository().getDirectory().getParent() + "/" + nowclassFile);
                        HashMap<String, String> resMap = diffMethods(baseMMap, nowMMap);
                        if (resMap.isEmpty()) {
                            continue;
                        } else {
                            StringBuilder builder = new StringBuilder("");
                            for (String v : resMap.values()) {
                                builder.append(v + "#");
                            }
                            map.put(nowclassFile.replace(".java", ""), builder.toString());
                        }
                    }
                }
            }
            return map;
        } catch (Exception e) {
            logger.error("计算增量方法出错", e.fillInStackTrace());
            return null;
        }
    }
    //这里找出来的diff是所有的java文件,不支持扫描指定目录
    public static HashMap<String, String> diffMethodsList(CoverageReportEntity coverageReport, Git baseGit, String baseCommitId, Git nowGit, String nowCommitId, String subModule) {
        HashMap<String, String> map = new HashMap<>();
        if (baseCommitId.equals(nowCommitId)) {
            return map;
        }
        try {
            ObjectId baseObjId = baseGit.getRepository().resolve(baseCommitId);
            ObjectId nowObjId = nowGit.getRepository().resolve(nowCommitId);
            AbstractTreeIterator baseTree = prepareTreeParser(baseGit.getRepository(), baseObjId);
            AbstractTreeIterator nowTree = prepareTreeParser(nowGit.getRepository(), nowObjId);
            List<DiffEntry> diff = nowGit.diff()
                    .setNewTree(nowTree)
                    .setOldTree(baseTree)
                    .setShowNameAndStatusOnly(true)
                    .call();
            for (DiffEntry diffEntry : diff) {
                if (diffEntry.getChangeType() == DiffEntry.ChangeType.DELETE) {
                    continue;
                }
                if (diffEntry.getNewPath().indexOf(subModule) < 0) {
                    continue;
                }
                if (diffEntry.getNewPath().endsWith(".java")) {
                    String nowclassFile = diffEntry.getNewPath();
                    if (diffEntry.getChangeType() == DiffEntry.ChangeType.ADD) {
                        map.put(nowclassFile.replace(".java", ""), "true");
                    } else if (diffEntry.getChangeType() == DiffEntry.ChangeType.MODIFY) {
                        MethodParser methodParser = new MethodParser();
                        HashMap<String, String> baseMMap = methodParser.parseMethodsMd5(baseGit.getRepository().getDirectory().getParent() + "/" + nowclassFile);
                        HashMap<String, String> nowMMap = methodParser.parseMethodsMd5(nowGit.getRepository().getDirectory().getParent() + "/" + nowclassFile);
                        HashMap<String, String> resMap = diffMethods(baseMMap, nowMMap);
                        if (resMap.isEmpty()) {
                            continue;
                        } else {
                            StringBuilder builder = new StringBuilder("");
                            for (String v : resMap.values()) {
                                builder.append(v + "#");
                            }
                            map.put(nowclassFile.replace(".java", ""), builder.toString());
                        }
                    }
                }
            }
            return map;
        } catch (Exception e) {
            coverageReport.setRequestStatus(Constants.JobStatus.GENERATEREPORT_FAIL.val());
        }
        return null;
    }

    public static HashMap<String, String> diffMethods(HashMap<String, String> baseMMap, HashMap<String, String> nowMMap) {
        HashMap<String, String> resMap = new HashMap<>();
        Iterator<String> iterator = nowMMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (!baseMMap.containsKey(key)) {
                resMap.put(key, nowMMap.get(key));
            }
        }
        return resMap;
    }


    public static AbstractTreeIterator prepareTreeParser(Repository repository, AnyObjectId objectId) throws IOException {
        try {
            RevWalk walk = new RevWalk(repository);
            RevTree tree;
            tree = walk.parseTree(objectId);
            CanonicalTreeParser TreeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                TreeParser.reset(reader, tree.getId());
            }
            walk.dispose();
            return TreeParser;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


