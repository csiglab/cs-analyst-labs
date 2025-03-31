package com.mycompany.app;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import java.util.HashMap;
import java.time.Instant;
import java.util.UUID;
import java.util.Map;

public class Logger {

    static {
        System.out.println("Logger class loaded by: " + Logger.class.getClassLoader());
    }

    public static void init() { }

    public static void logException(Throwable e) {
        try {
            // Create JSON object with exception details
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("id", UUID.randomUUID().toString());
            jsonMap.put("message", e.getMessage() != null ? e.getMessage() : null);
            jsonMap.put("exception", e.getClass().getName());
            jsonMap.put("timestamp", Instant.now().toEpochMilli());

            // Convert stack trace to a string
            StringBuilder stackTrace = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                stackTrace.append(element.toString()).append("\n");
            }
            jsonMap.put("stackTrace", stackTrace.toString());
            

            // Convert map to JSON string using Gson
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            String jsonString = gson.toJson(jsonMap);

            System.out.println("===========");
            System.out.println("❌ Failure Here");
            System.out.println(jsonString);
            System.out.println("===========");

            OkHttpJsonSender.send(jsonString, "http://localhost:8080/api/logs");;

        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }
}