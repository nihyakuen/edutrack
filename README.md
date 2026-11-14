# EduTrack

EduTrack — учебный REST API для управления занятиями, оценками и посещаемостью студентов.

### Стек

Java, Spring Boot, Spring Data JPA, Spring Security, JWT, PostgreSQL, Flyway, Gradle, Docker.

### Возможности

- **ADMIN** — управление пользователями, группами, дисциплинами и назначениями преподавателей.
- **TEACHER** — управление занятиями, проверочными работами, оценками и посещаемостью.
- **STUDENT** — просмотр своих дисциплин, занятий, оценок и посещаемости.

Реализованы ролевая авторизация, валидация и пагинация.

### Запуск

Требуется Docker с поддержкой Compose.

Создать `.env` с JWT-секретом:

```bash
echo "JWT_SECRET=$(openssl rand -base64 32)" > .env
```

Запустить приложение:

```bash
docker compose up --build
```

Swagger UI: http://localhost:8080/swagger-ui/index.html

Flyway автоматически создаёт структуру базы данных. Демонстрационные данные доступны отдельно в `scripts/demo_seed.sql` и не загружаются автоматически.

### Статус

MVP в разработке