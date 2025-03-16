package com.example;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        new App().sayHello();
    }

    public void sayHello() {
        System.out.println("Hello from sayHello method!");
    }
}
