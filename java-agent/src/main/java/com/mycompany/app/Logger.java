package com.mycompany.app;

public class Logger {

    static {
        System.out.println("Logger class loaded by: " + Logger.class.getClassLoader());
    }

    public static void init() {

    }
    public static void logException(Throwable e) {
        try {
            System.out.println("===========");
            System.out.println("❌ Failure Here");
            e.printStackTrace();
            System.out.println("===========");
        } catch (Exception ex) {
            // ex.printStackTrace();
        }
    }
}
