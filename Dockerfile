# Etapa 1: Compilación con Maven y JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Setear el directorio de trabajo
WORKDIR /app

# Copiar los archivos de Maven primero para aprovechar la cache
COPY pom.xml .
COPY src ./src

# Compilar la app, saltando los tests para que el build sea más rápido
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final para ejecutar la app
FROM eclipse-temurin:21-jdk

# Crear directorio de trabajo
WORKDIR /app

# Copiar el .jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto donde corre tu aplicación
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]