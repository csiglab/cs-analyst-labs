# Analysis of Java Computational Systems

> ...

## Dependencies

- `wget https://github.com/glowroot/glowroot/releases/download/v0.14.0/glowroot-0.14.0-dist.zip`
- `unzip glowroot-0.14.0-dist.zip`

## Build

- `mvn archetype:generate -DgroupId=com.example -DartifactId=timeapp -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false`

- `mvn clean package spring-boot:repackage`

## Run

- `file-roller target/timeapp-1.0-SNAPSHOT.jar`
- `java -javaagent:../glowroot/glowroot.jar -jar target/timeapp-1.0-SNAPSHOT.jar --server.port=8082`
- ``

## Analysis

```bash
for i in {1..10}
do
  curl -X GET http://localhost:8082/api/time
done
```

## Future Work

- Intercept Java Connection to Execute Queries 🔄💻🔍
- Enhance SQL Logs with Context -> Include Stack Trace 📚🔍📝

## References

- [pinpoint](https://github.com/pinpoint-apm/pinpoint)
- [skywalking](https://github.com/apache/skywalking)
- [opentelemetry-java](https://github.com/open-telemetry/opentelemetry-java)
- [glowroot](https://github.com/glowroot/glowroot)
- [stagemonitor](https://github.com/stagemonitor/stagemonitor)
