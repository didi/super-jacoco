package com.xiaoju.basetech.util;

/**
 * @description:
 * @author: charlyne
 * @time: 2019/6/27 4:38 PM
 */

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MethodParser {
    protected HashMap<String, String> methodMd5Map;
    public HashMap<String, String> parseMethodsMd5(String classFile) throws Exception {
        methodMd5Map = new HashMap<>();
        FileInputStream in = new FileInputStream(classFile);
        CompilationUnit cu = JavaParser.parse(in);
        //这里去掉注释好像没啥用20200213
        List<Comment> comments = cu.getAllContainedComments();
        List<Comment> unwantedComments = comments
                .stream()
                .filter(p -> !p.getCommentedNode().isPresent() || p instanceof LineComment)
                .collect(Collectors.toList());
        unwantedComments.forEach(Node::remove);
        cu.accept(new MethodMd5Visitor(), null);
        //下面这一句用来返回类的方法集合带md5
        return getMethodMd5Map();

    }



    public static void main(String[] args){
        String file="/Users/didi/IdeaProjects/super-jacoco/clonecode/1581780948510/8b2c3257b672041b5dc272f19fdab45410301c5e/rollsroyce-biz/src/main/java/com/xiaoju/automarket/energy/rollsroyce/biz/flow/OrderFlow.java";
        MethodParser parser=new MethodParser();
        try {
         //   parser.parseMethodsMd5(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String ll="/Users/didi/IdeaProjects/super-jacoco/clonecode/1582023571353/8b2c3257b672041b5dc272f19fdab45410301c5e/rollsroyce-biz/src/main/java/";
        System.out.println(ll.replace("/src/main/java/","/target/"));

    }

    private class MethodMd5Visitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration n, Void arg) {
            NodeList<Parameter> parameters = n.getParameters();
            StringBuilder builder=new StringBuilder(n.getNameAsString());
            for (Parameter parameter : parameters) {
                //比如private boolean isNotOwnerOrder(long passportUid, OrderInfoDTO orderInfoDTO) {
                //long的size是0的,而且asm中也没有这个long,所以要丢掉
                // TODO: 2020/2/14
                if(parameter.getType().getChildNodes().size()>0){
                    builder.append(",");
                    builder.append(String.valueOf(parameter.getType().getChildNodes().get(0)));
                }
            }
            String md5 = "";
            // n.toString是方法体
            md5 = getMD5Value(n.toString());
            // 以md5作为存储key,方法名字作为value,
            // TODO 万一包含重写函数（overwrite）?待优化
            methodMd5Map.put(md5, builder.toString());
            super.visit(n, arg);
        }

    }

    // MD5 加密工具类
    public static String getMD5Value(String dataStr) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(dataStr.getBytes("UTF8"));
            byte s[] = m.digest();
            String result = "";
            for (int i = 0; i < s.length; i++) {
                result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }





    public HashMap<String, String> getMethodMd5Map() {
        return methodMd5Map;
    }

    public void setMethodMd5Map(HashMap<String, String> methodMd5Map) {
        this.methodMd5Map = methodMd5Map;
    }
}
