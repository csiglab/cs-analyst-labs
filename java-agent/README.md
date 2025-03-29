# Java Agent

> Note check the Byte Code Compatiblity of the ASM Lib Version. I Have tested for Java 8 and 11; failed for 25.

## Run

```bash
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=42693 \
     -javaagent:target/java-agent-1.0-SNAPSHOT.jar \
     -jar target/java-agent-1.0-SNAPSHOT.jar
```

```bash
java -javaagent:target/java-agent-1.0-SNAPSHOT.jar
     -jar target/java-agent-1.0-SNAPSHOT.jar
```

## **Step 4: Package and Run the Agent**

> ...

### Compile the Java agent

```bash
javac -cp asm-9.5.jar:. MyAgent.java MyLogger.java
```

### Create `MANIFEST.MF`

```
Premain-Class: MyAgent
```

### Create the JAR

```bash
jar cmf MANIFEST.MF myagent.jar MyAgent.class MyLogger.class
```

### Run with the Agent

```bash
java -javaagent:myagent.jar -jar YourApplication.jar
```

TODO:
- Separar el agente del código.
- Enviar la info a un servidor python en formato json - guardar en sqlite.
