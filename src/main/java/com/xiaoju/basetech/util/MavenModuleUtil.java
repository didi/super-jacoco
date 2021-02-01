package com.xiaoju.basetech.util;

/**
 * @description:
 * @author: gaoweiwei_v
 * @time: 2019/8/27 4:17 PM
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xiaoju.basetech.entity.CoverageReportEntity;
import com.xiaoju.basetech.entity.ModuleInfo;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class MavenModuleUtil {

    public void addMavenModule(CoverageReportEntity coverageReport) {
        try {
            String pomPath = coverageReport.getNowLocalPath() + "/pom.xml";
            File pomFile = new File(pomPath);
            if (!pomFile.exists()) {
                coverageReport.setRequestStatus(Constants.JobStatus.FAILADDMODULE.val());
                return;
            }
            // 添加lombok配置
            File lombokConfig = new File(coverageReport.getNowLocalPath() + "/lombok.config");
            FileWriter lombokWriter = new FileWriter(lombokConfig);
            lombokWriter.write("lombok.addLombokGeneratedAnnotation = true");
            lombokWriter.flush();
            lombokWriter.close();
            ArrayList<String> list = getChildPomsPath(pomPath);
            if (list.size() <= 1) {
                coverageReport.setReportFile(coverageReport.getNowLocalPath() + "/target/site/jacoco/index.html");
                coverageReport.setRequestStatus(Constants.JobStatus.ADDMODULE_DONE.val());
                return;
            }
            StringBuilder denpBuilder = new StringBuilder();
            ModuleInfo moduleInfo = getModuleInfo(pomPath);
            String str = dependencyStr(pomPath, moduleInfo, denpBuilder).toString();
            if (StringUtils.isEmpty(str)) {
                coverageReport.setRequestStatus(Constants.JobStatus.ADDMODULE_DONE.val());
                coverageReport.setReportFile(coverageReport.getNowLocalPath() + "/target/site/jacoco/index.html");
                return;
            }
            //在父pom中写入jacocomodule
            BufferedReader parentbReader = new BufferedReader(new FileReader(pomFile));
            StringBuilder sb = new StringBuilder();
            String s = "";
            while ((s = parentbReader.readLine()) != null) {
                sb.append(s.trim() + "\n");

            }
            String pomStr = sb.toString();
            pomStr = pomStr.replace("<modules>", "<modules>\n<module>jacocomodule</module>");
            FileWriter writer = new FileWriter(pomFile);
            writer.write(pomStr);
            writer.flush();
            writer.close();
            StringBuilder builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                    "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                    "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n");
            builder.append("<modelVersion>4.0.0</modelVersion>\n");
            builder.append("<parent>\n");
            builder.append("<artifactId>" + moduleInfo.getArtifactId() + "</artifactId>\n");
            builder.append("<groupId>" + moduleInfo.getGroupId() + "</groupId>\n");
            builder.append("<version>" + moduleInfo.getVersion() + "</version>\n");
            builder.append("</parent>\n");
            builder.append("<groupId>" + moduleInfo.getGroupId() + "</groupId>\n");
            builder.append("<artifactId>jacocomodule</artifactId>\n");
            builder.append("<version>" + moduleInfo.getVersion() + "</version>\n");
            builder.append("<dependencies>\n");
            builder.append(str);
            builder.append("\n</dependencies>\n" +
                    "<build>\n" +
                    "        <plugins>\n" +
                    "            <plugin>\n" +
                    "                <groupId>org.jacoco</groupId>\n" +
                    "                <artifactId>jacoco-maven-plugin</artifactId>\n" +
                    "                <version>1.0.2-SNAPSHOT</version>\n" +
                    "                <executions>\n" +
                    "                    <execution>\n" +
                    "                        <id>report-aggregate</id>\n" +
                    "                        <phase>compile</phase>\n" +
                    "                        <goals>\n" +
                    "                            <goal>report-aggregate</goal>\n" +
                    "                        </goals>\n" +
                    "                    </execution>\n" +
                    "                </executions>\n" +
                    "            </plugin>\n" +
                    "        </plugins>\n" +
                    "    </build>\n" +
                    "</project>");
            File coverageModule = new File(coverageReport.getNowLocalPath() + "/jacocomodule");
            if (!coverageModule.exists()) {
                coverageModule.mkdir();
                File coveragePomFile = new File(coverageReport.getNowLocalPath() + "/jacocomodule/pom.xml");
                if (!coveragePomFile.exists()) {
                    coveragePomFile.createNewFile();
                    FileWriter cwriter = new FileWriter(coveragePomFile);
                    cwriter.write(builder.toString());
                    cwriter.flush();
                    cwriter.close();
                    coverageReport.setReportFile(coverageReport.getNowLocalPath() + "/jacocomodule/target/site/jacoco-aggregate/index.html");
                    coverageReport.setRequestStatus(Constants.JobStatus.ADDMODULE_DONE.val());
                    return;
                }
            }
        } catch (Exception e) {
            log.error("添加集成模块执行异常:{}", coverageReport.getUuid(), e);
            coverageReport.setErrMsg("添加集成模块执行异常:" + e.getMessage());
            coverageReport.setRequestStatus(Constants.JobStatus.FAILADDMODULE.val());
        }
    }

    public static void replaceArgLine(String pomPath) {
        File pomFile = new File(pomPath);
        BufferedReader parentbReader = null;
        try {
            parentbReader = new BufferedReader(new FileReader(pomFile));
            StringBuilder sb = new StringBuilder();
            String s = "";
            while ((s = parentbReader.readLine()) != null) {
                sb.append(s.trim() + "\n");

            }
            String pomStr = sb.toString();
            if (pomStr.contains("<argLine>")) {
                FileWriter writer = new FileWriter(pomFile);
                writer.write(pomStr.replaceAll("<argLine>", "<argLine>@{argLine} "));
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            log.error("replaceArgLineError", e);
        }

    }

    //获取一个moduleGAV等基本信息
    public static ModuleInfo getModuleInfo(String pomFile) {
        ModuleInfo moduleInfo = new ModuleInfo();
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(new File(pomFile));
            Element root = document.getRootElement();
            Element ee = root.element("parent");
            if (ee != null) {
                if (ee.element("version") != null) {
                    moduleInfo.setParentVersion(ee.element("version").getText());
                    moduleInfo.setVersion(moduleInfo.getParentVersion());
                }
                if (ee.element("groupId") != null) {
                    moduleInfo.setParentGroupId(ee.element("groupId").getText());
                    moduleInfo.setGroupId(moduleInfo.getParentGroupId());

                }
                if (ee.element("artifactId") != null) {
                    moduleInfo.setParentArtifactId(ee.element("artifactId").getText());
                    moduleInfo.setArtifactId(moduleInfo.getParentArtifactId());
                }
            }
            if (root.element("properties") != null) {
                moduleInfo.setProperties(root.element("properties"));
            }
            if (root.element("packaging") != null) {
                moduleInfo.setPackaging(root.element("packaging").getText());
            }
            int i = 0;
            for (Iterator<Element> it = root.elementIterator(); it.hasNext(); ) {
                Element e = it.next();
                if (i < 3) {
                    if (e.getName().equals("version")) {
                        moduleInfo.setVersion(e.getText());
                        i++;
                    } else if (e.getName().equals("groupId")) {
                        moduleInfo.setGroupId(e.getText());
                        i++;
                    } else if (e.getName().equals("artifactId")) {
                        moduleInfo.setArtifactId(e.getText());
                        i++;
                    }
                }else {
                    break;
                }
            }
            if(!StringUtils.isEmpty(moduleInfo.getVersion())&&!StringUtils.isEmpty(moduleInfo.getGroupId())
                    &&!StringUtils.isEmpty(moduleInfo.getArtifactId())){
                moduleInfo.setFlag(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moduleInfo;

    }

    //获取所有的子模块pom文件路径
    public static ArrayList<String> getChildPomsPath(String pomPath) {
        File dir = new File(pomPath).getParentFile();
        ArrayList<String> list = new ArrayList<>();
        if (!dir.exists()) {
            return list;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                File[] childFiles = file.listFiles();
                for (File childFile : childFiles) {
                    if (childFile.getName().equals("pom.xml")) {
                        list.add(childFile.getAbsolutePath());
                    }
                }
            }
        }

        return list;

    }

    //获取pom中有效的modules列表
    public static ArrayList<String> getValidModules(String pomPath) {
        ArrayList<String> validModuleList = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(pomPath));
            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = reader.readLine()) != null) {
                sb.append(s.trim());
            }
            String pomStr = sb.toString();
            String moduleregex = "<modules>.*?</modules>";
            Pattern modulepattern = Pattern.compile(moduleregex);
            Matcher moduleM = modulepattern.matcher(pomStr);
            String modules;
            if (moduleM.find()) {
                modules = moduleM.group();
                modules = modules.replaceAll("<!--<module>.*?</module>-->", ",");
                modules = modules.replaceAll("</?modules?>", ",");
                String[] module = modules.split(",");
                for (String m : module) {
                    if (!m.equals("")) {
                        validModuleList.add(m);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return validModuleList;
    }


    public static StringBuilder dependencyStr(String pomPath, ModuleInfo moduleInfo, StringBuilder dependencyBuilder) {
        //moduleInfo是pomFile的GAV等信息
        if (moduleInfo.getPackaging() == null || moduleInfo.getPackaging().equals("jar")) {
            if (moduleInfo.getArtifactId() != null) {
                String groupId = getModuleGroupId(moduleInfo);
                String version = getModuleVersion(moduleInfo);
                StringBuilder sb = new StringBuilder("<dependency>\n");
                sb.append("<artifactId>" + moduleInfo.getArtifactId() + "</artifactId>\n");
                if (groupId != null) {
                    sb.append("<groupId>" + groupId + "</groupId>\n");
                }
                if (version != null) {
                    sb.append("<version>" + version + "</version>\n");
                }
                sb.append("</dependency>\n");
                dependencyBuilder.append(sb.toString());
            }
        } else {
            ArrayList<String> validModuleList = getValidModules(pomPath);
            for (int i = 0; i < validModuleList.size(); i++) {
                String childPom = new File(pomPath).getParent() + "/" + validModuleList.get(i) + "/pom.xml";
                ModuleInfo moduleInfoChild = getModuleInfo(childPom);
                if (moduleInfo.isFlag()) {
                    replaceArgLine(childPom);
                    moduleInfoChild.setParent(moduleInfo);
                    moduleInfoChild.setFlag(true);
                    dependencyBuilder = dependencyStr(childPom, moduleInfoChild, dependencyBuilder);
                }
            }
        }
        return dependencyBuilder;
    }


    public static String getModuleGroupId(ModuleInfo moduleInfo) {
        String groupId = moduleInfo.getGroupId();
        if (groupId == null) {
            if (moduleInfo.getParent() != null) {
                groupId = getModuleGroupId(moduleInfo.getParent());
            }
        }
        return groupId;

    }

    public static String getModuleVersion(ModuleInfo moduleInfo) {
        String version = moduleInfo.getVersion();
        if (version != null && version.contains("$")) {
            ModuleInfo moduleInfo1 = moduleInfo;
            String versionName = version.replace("$", "").replace("{", "").replace("}", "");
            while (moduleInfo1.getParent() != null) {
                Element properties = moduleInfo1.getParent().getProperties();
                if (properties != null && properties.element(versionName) != null) {
                    version = properties.element(versionName).getText();
                    return version;
                } else {
                    moduleInfo1 = moduleInfo1.getParent();
                }
            }

        } else if (version == null) {
            version = moduleInfo.getParentVersion();
        }

        return version;

    }
}