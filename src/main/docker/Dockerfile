FROM gradle:9-jdk25 AS deps
WORKDIR /home/gradle/project

COPY build.gradle.kts settings.gradle.kts ./
RUN gradle dependencies --no-daemon --configuration runtimeClasspath

FROM gradle:9-jdk25 AS build
WORKDIR /home/gradle/project

COPY --from=deps /home/gradle/.gradle /home/gradle/.gradle
COPY . .
RUN gradle bootJar --no-daemon -x test

RUN java -Djarmode=layertools -jar build/libs/*.jar extract --destination build/extracted

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring

COPY --from=build /home/gradle/project/build/extracted/dependencies/ ./
COPY --from=build /home/gradle/project/build/extracted/spring-boot-loader/ ./
COPY --from=build /home/gradle/project/build/extracted/snapshot-dependencies/ ./
COPY --from=build /home/gradle/project/build/extracted/application/ ./

EXPOSE 8080

ENTRYPOINT [
  "java", \
  "-XX:MaxRAMPercentage=95.0", \
  "-XX:+UseContainerSupport", \
  "-XX:+UseZGC", \
  "-XX:+UnlockExperimentalVMOptions", \
  "-XX:+UseCompactObjectHeaders", \
  "org.springframework.boot.loader.launch.JarLauncher"
 ]

