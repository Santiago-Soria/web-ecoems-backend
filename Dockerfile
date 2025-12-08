# 1. Usamos una imagen base ligera de Java 17
FROM eclipse-temurin:17-jdk-alpine

# 2. Creamos un punto de montaje temporal (opcional pero recomendado por Spring)
VOLUME /tmp

# 3. Copiamos el archivo .jar generado por Maven al contenedor y lo renombramos
COPY target/*.jar app.jar

# 4. Exponemos el puerto donde corre tu app (según tu application.properties)
EXPOSE 8090

# 5. Comando para ejecutar la aplicación cuando arranque el contenedor
ENTRYPOINT ["java","-jar","/app.jar"]