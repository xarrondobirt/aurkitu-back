# Usamos una imagen base ligera de Java 21 (igual que en el CI)
FROM eclipse-temurin:21-jre-alpine

# Creamos un usuario no-root por seguridad
# Si una vulnerabilidad del back permite ejecutar código remoto y no ponemos un user específico, tendría permisos de root
# Docker no mapea nombres de usuario, mapea números (UIDs). Este usuario tendrá que escribir en uploads (imágenes, docs, etc)
# Lo dejo preparado con el mismo UID que tiene la carpeta en el servidor. Tutto esto por no usar volúmenes y sí bind mounts ...
# drwxr-sr-x  4 birt          birt            4,0K nov 18 22:49 uploads --> birt es el UID=1000 en el server (normalmente es el UID que se le da al primer usuario que se crea en la máquina)
RUN addgroup -g 1000 spring && adduser -u 1000 -D -G spring spring

# Directorio de trabajo dentro del contenedor (si no, intentaría escribir en / y no )
WORKDIR /app

# Como la carpeta /app la crea Docker (root), debemos dársela al usuario spring.
# Esto permite que la app cree carpetas como ./datos/log sin errores.
RUN chown -R spring:spring /app

USER spring:spring

# Exponemos el puerto 8080 (el que usa Spring Boot por defecto)
EXPOSE 8080

# Copiamos el JAR que generó Maven en el paso anterior del CI en la máquina ubuntu-latest que se levanta
# El jar lo crea el ci.yml con run: mvn -B package
# El jar está en la carpeta target ...
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# El comando que arranca la aplicación, y el usuario que la arranca es spring (por el USER spring:spring)
ENTRYPOINT ["java","-jar","app.jar"]