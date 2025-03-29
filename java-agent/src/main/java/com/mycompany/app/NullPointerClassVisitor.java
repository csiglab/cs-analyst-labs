package com.mycompany.app;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class NullPointerClassVisitor extends ClassVisitor {
    public NullPointerClassVisitor(ClassWriter writer) {
        super(Opcodes.ASM9, writer);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (name.equals("<init>")) {
            return new NullPointerMethodVisitor(mv);
        }
        return mv;
    }
}
