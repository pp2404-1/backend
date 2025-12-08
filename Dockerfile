# Используем тот же образ что и тимлид, но с обновленными пакетами
FROM bellsoft/liberica-openjdk-alpine:17.0.12-cds

# Обновляем систему и устанавливаем безопасность
RUN apk update && apk upgrade && \
    rm -rf /var/cache/apk/*

WORKDIR /backend

# Копируем JAR (нужно собрать заранее через mvn package)
COPY target/*.jar /backend/backend.jar

# Создаем непривилегированного пользователя
RUN addgroup -S appgroup && adduser -S appuser -G appuser
USER appuser

CMD ["java", "-jar", "backend.jar"]