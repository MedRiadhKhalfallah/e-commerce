FROM maven:3.9-eclipse-temurin-25

WORKDIR /app

# Copier uniquement le pom pour cache dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier le reste
COPY src ./src

EXPOSE 8992

CMD ["mvn", "spring-boot:run"]