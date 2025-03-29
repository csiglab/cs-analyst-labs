package com.mycompany.app;

import java.math.BigInteger;

/**
 * Hello world!
 * 
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Welcome");
        try {
            BigInteger a = null;
            System.out.println(a.abs());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
