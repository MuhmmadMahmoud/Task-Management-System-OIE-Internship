# ===== step 1: build the jar =====
# I use the Maven Wrapper (./mvnw) so the build always uses the same Maven version
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# I copy the wrapper and the pom.xml alone first so Docker can cache the dependencies
# and does not download them again every time I change my code
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# ===== step 2: run the jar on a small image =====
# the second image has only the java runtime and not the JDK, so it is much smaller
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/taskmanager-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
