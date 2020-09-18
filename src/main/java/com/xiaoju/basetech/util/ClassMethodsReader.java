package com.xiaoju.basetech.util;

import jdk.internal.org.objectweb.asm.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @description:
 * @author: gaoweiwei_v
 * @time: 2019/9/3 9:10 PM
 */
public class ClassMethodsReader {

    public ArrayList<String> getMethodList(String file) throws IOException {
        File classFile = new File(file);
        ClassPrinter cp = new ClassPrinter();
        if (classFile.exists()) {
            FileInputStream inputStream = new FileInputStream(classFile);
            ClassReader cr = new ClassReader(inputStream);
            cr.accept(cp, 0);
        } else {
            cp.methodInfoList.add("fileNotExists");
        }
        return cp.methodInfoList;

    }

    class ClassPrinter extends ClassVisitor {
        public ArrayList<String> methodInfoList = new ArrayList<>();

        public ClassPrinter() {
            super(Opcodes.ASM4);
        }

        public void visit(int version, int access, String name,
                          String signature, String superName, String[] interfaces) {
        }

        public void visitSource(String source, String debug) {
        }

        public void visitOuterClass(String owner, String name, String desc) {
        }

        public AnnotationVisitor visitAnnotation(String desc,
                                                 boolean visible) {
            return null;
        }

        public void visitAttribute(Attribute attr) {
        }

        public void visitInnerClass(String name, String outerName,
                                    String innerName, int access) {
        }

        public FieldVisitor visitField(int access, String name, String desc,
                                       String signature, Object value) {
            return null;
        }

        public MethodVisitor visitMethod(int access, String name,
                                         String desc, String signature, String[] exceptions) {
            methodInfoList.add(name + desc);
            return null;
        }

        public void visitEnd() {

        }

    }

}