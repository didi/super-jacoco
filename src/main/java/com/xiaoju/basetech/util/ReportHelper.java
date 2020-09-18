package com.xiaoju.basetech.util;

//import org.apache.commons.io.IOUtils;


import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: gaoweiwei_v
 * @time: 2019/7/31 10:54 AM
 */

public class ReportHelper {
    /**
     * @description:为多模块的工程添加一个子模块jacocomodule，dependency其他工程的原有子模块
     * @param projectPath
     * @return
     */
    public static Boolean addcoverageModule(String projectPath){
        File projectDir=new File(projectPath);
        FileWriter writer=null;
        FileWriter cwriter=null;
        if(!projectDir.exists()){
            return false;
        }
        File parentFile=new File(projectDir.getAbsolutePath()+"/pom.xml");
        if(!parentFile.exists()){
            return false;
        }
        File[] files = projectDir.listFiles();
        File childFile=null;

        //查找是否存在pom文件
        int num=0;
        for(File file:files){
            if(childFile!=null){
                break;
            }
            if(file.isDirectory()){
                File[] subfiles=file.listFiles();
                for(File subfile:subfiles){
                    if(subfile.getName().equals("pom.xml")){
                        childFile=subfile.getAbsoluteFile();
                        num=1;
                        break;
                    }
                }

            }
        }
        if(num==0){
            return false;
        }
        try {

            BufferedReader parentbReader=new BufferedReader(new FileReader(parentFile));
            StringBuilder sb=new StringBuilder();
            StringBuilder sb1=new StringBuilder();
            String s="";
            while ((s=parentbReader.readLine())!=null){
                sb.append(s.trim());
                sb1.append(s.trim()+"\n");
            }
            String pomstr=sb.toString();
            // TODO: 2019/7/25 留作使用pomstrfinal
            String pomstrfinal=sb1.toString();
            //获取groupid和version和artifactId，这里需要pom中的<groupid><version><artifactId>放在前面，否则会出错
            BufferedReader childReader=new BufferedReader(new FileReader(childFile));
            StringBuilder childsb=new StringBuilder();
            String childs="";
            while ((childs=childReader.readLine())!=null){
                childsb.append(childs.trim());
            }
            String childpomstr=childsb.toString();

            //重childpom中获取parent
            String childRegex="<parent>.*?</parent>";
            Pattern childPattern=Pattern.compile(childRegex);
            Matcher childM=childPattern.matcher(childpomstr);
            String childParentStr="";
            if(!childM.find()){
                return false;
            }else{
                childParentStr=childM.group();
            }


            //获取groupid
            String groupidregex="<groupId>.*?</groupId>";
            Pattern groupidpattern=Pattern.compile(groupidregex);
            Matcher groupidM=groupidpattern.matcher(childParentStr);
            String groupid="";
            if(groupidM.find()){
                groupid=groupidM.group().replaceAll("</?groupId>","");
            }else {
                return false;
            }
            //获取version
            String versionregex="<version>.*?</version>";
            Pattern versionpattern=Pattern.compile(versionregex);
            Matcher versionM=versionpattern.matcher(childParentStr);
            String version="";
            if(versionM.find()){
                //version=versionM.group().replaceAll("</?version>","");
                version=versionM.group();
            }else{
                return false;
            }

            //创建新模块和新模块的pom文件
            String projectlabel="<project\n" +
                    "        xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n" +
                    "        xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n";
            String modelbabel="<modelVersion>4.0.0</modelVersion>\n<artifactId>jacocomodule</artifactId>\n<name>jacocomodule</name>\n"+version+"\n<dependencies>\n";

            StringBuilder coveragestr=new StringBuilder("");
            coveragestr.append(projectlabel);
            coveragestr.append(childParentStr);
            coveragestr.append(modelbabel);


            // 获取工程的所有module
            String moduleregex="<modules>.*?</modules>";
            Pattern modulepattern=Pattern.compile(moduleregex);
            Matcher moduleM=modulepattern.matcher(pomstr);
            String modules="";
            if(moduleM.find()){
                modules=moduleM.group();
                modules=modules.replaceAll("</?modules?>",",");
                String[] module=modules.split(",");
                for(int i=0;i<module.length;i++){
                    if(!module[i].equals("")){
                        coveragestr.append("<dependency>\n<groupId>"+
                                groupid +"</groupId>\n<artifactId>"+module[i]+"</artifactId>\n</dependency>\n");
                    }
                }
            }else
            {
                return false;
            }

            coveragestr.append("</dependencies>\n");
            coveragestr.append("<build>\n" +
                    "        <plugins>\n" +
                    "            <plugin>\n" +
                    "                <groupId>org.jacoco</groupId>\n" +
                    "                <artifactId>jacoco-maven-plugin</artifactId>\n" +
                    "                <version>1.0.1-SNAPSHOT</version>\n" +
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
                    "    </build>\n");

            coveragestr.append("</project>");

            //加入<module>jacocomodule</module>到parentpom文件
            pomstrfinal=pomstrfinal.replace("<modules>","<modules>\n<module>jacocomodule</module>");
            writer=new FileWriter(parentFile);
            writer.write(pomstrfinal);
            writer.flush();
            //end

            File coveragemodule=new File(projectPath+"/jacocomodule");
            if(!coveragemodule.exists()){
                coveragemodule.mkdir();
                File coveragepomFile=new File(projectPath+"/jacocomodule/pom.xml");
                coveragepomFile.setWritable(true, false);
                if(!coveragepomFile.exists()){
                    coveragepomFile.createNewFile();
                    cwriter=new FileWriter(coveragepomFile);
                    cwriter.write(coveragestr.toString());
                    cwriter.flush();
                    return true;
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(writer!=null){
                    writer.close();
                }
                if(cwriter!=null) {
                    cwriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return file.delete();
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFile(f);
            }
            return file.delete();
        }
    }

}