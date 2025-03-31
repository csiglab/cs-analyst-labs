package com.mycompany.app;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ClassVisitor;

public class NullPointerTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, 
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        
        System.out.println(className);

        if (!className.equals("java/lang/NullPointerException")) {
            return null; // Ignore all classes except NullPointerException
        }
        try {
            ClassReader reader = new ClassReader(classfileBuffer);
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            ClassVisitor visitor = new NullPointerClassVisitor(writer);
            reader.accept(visitor, 0);
            return writer.toByteArray(); // Return modified bytecode

        } catch (Exception e) {
            System.out.println(e);
            // TODO: handle exception

        }
        return null;
    }
}
