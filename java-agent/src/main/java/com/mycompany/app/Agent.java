package com.mycompany.app;

import java.io.File;
import java.util.jar.JarFile;
import java.lang.instrument.Instrumentation;

public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {

        System.out.println("Premain started!");

        File jarFile = new File("/home/dbremont/Code/java-agent/target/java-agent-1.0-SNAPSHOT.jar");
         try {
             inst.appendToBootstrapClassLoaderSearch(new JarFile(jarFile));
             System.out.println("✅ Successfully added to bootstrap classpath: " + jarFile.getAbsolutePath());
         } catch (Exception e) {
            System.out.println("❌ Failed to add to bootstrap: " + e.getMessage());
         }

        inst.addTransformer(new NullPointerTransformer(), true);

        try {
            // Force transformation for the Java base system classes loaded from 'jrt:/' path
            for (Class<?> loadedClass : inst.getAllLoadedClasses()) {
                // Why I Cant' Modify Lambdas?
                if (loadedClass.getName().startsWith("java.") && inst.isModifiableClass(loadedClass)) {
                    System.out.println("Retransforming system class: " + loadedClass.getName());
                    inst.retransformClasses(loadedClass);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}