FROM maven:3.9-eclipse-temurin-25

WORKDIR /app

# Copier uniquement le pom pour cache dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier le reste
COPY src ./src

EXPOSE 8992
EXPOSE 5005

# Run with devtools and optional debug
CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"]
