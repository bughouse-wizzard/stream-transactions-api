# Используем официальный образ JDK 21 + Maven для сборки
FROM maven:3.9.5-eclipse-temurin-21 AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем исходный код и pom.xml
COPY pom.xml .
COPY src ./src
COPY incomes.csv .
COPY outcomes.csv .

# Собираем проект
RUN mvn clean package

# ---

# Финальный образ
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Копируем только нужные файлы из стадии сборки
COPY --from=builder /app/target/stream-transactions-api-1.0-SNAPSHOT-jar-with-dependencies.jar ./app.jar
COPY --from=builder /app/incomes.csv ./incomes.csv
COPY --from=builder /app/outcomes.csv ./outcomes.csv

# Запускаем сервер
ENTRYPOINT ["java", "-jar", "app.jar"]