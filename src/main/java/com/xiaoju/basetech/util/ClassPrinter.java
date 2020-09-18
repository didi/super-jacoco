package com.xiaoju.basetech.util;

import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.*;

/**
 * @description:
 * @author: gaoweiwei_v
 * @time: 2019/8/30 2:18 AM
 */
public class ClassPrinter extends ClassVisitor {
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
        System.out.println(" " + name + desc);
        return null;
    }

    public void visitEnd() {
    }

}