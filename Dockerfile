# Шаг 1: Билд приложения
FROM gradle:7.6.0-jdk17 AS build

# Рабочая директория для сборки
WORKDIR /app

# Копируем конфигурационные файлы для сборки
COPY build.gradle.kts settings.gradle.kts gradlew /app/
COPY gradle /app/gradle

RUN chmod +x ./gradlew
# Качаем зависимости (кэшируем)
RUN chmod +x ./gradlew build --no-daemon || return 0

# Копируем весь проект в контейнер
COPY . .

RUN chmod +x ./gradlew
# Выполняем финальную сборку приложения
RUN ./gradlew build --no-daemon

# Шаг 2: Собираем финальный образ для запуска приложения
FROM openjdk:17-jdk-slim

# Указываем рабочую директорию
WORKDIR /app

# Копируем скомпилированный JAR файл
COPY --from=build /app/build/libs/*.jar app.jar

# Явно копируем ресурсы из src/main/resources в файловую систему Docker-контейнера
COPY --from=build /app/src/main/resources /app/resources

# Открываем порт 8080
EXPOSE 3000

# Переменные среды для JVM (если нужно)
ENV JAVA_OPTS=""

# Запускаем приложение
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
