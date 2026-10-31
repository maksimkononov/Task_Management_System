# Task Management System

Небольшой учебный проект на Java и Spring Boot — REST API для управления задачами. Делал его, чтобы на практике разобраться с Spring MVC, работой с PostgreSQL через JPA/Hibernate и разделением приложения на слои.

## Что умеет приложение

- Создавать, получать и редактировать задачи.
- Отменять задачи, запускать их в работу и отмечать выполненными.
- Искать задачи по автору, исполнителю, статусу и приоритету; поддерживается пагинация.
- Проверять входные данные и обрабатывать ошибки.

У задачи есть четыре статуса: `CREATED`, `IN_PROGRESS`, `DONE`, `CANCEL`. При создании устанавливается `CREATED`. Редактировать можно только созданную задачу, а завершить — только задачу в работе. Для запуска нужен назначенный исполнитель; одновременно у одного исполнителя может быть не больше пяти задач со статусом `IN_PROGRESS`.

## Стек

Java 17, Spring Boot, Spring MVC, Spring Data JPA, Hibernate, PostgreSQL, Maven. Для локального запуска базы данных использую Docker, для проверки запросов — Postman.

## Запуск на macOS

Понадобятся **JDK 17** и **Docker Desktop**.

1. Запустить Docker Desktop.
2. Поднять PostgreSQL. Например, если контейнер ещё не создан:

   ```bash
   docker run --name task-postgres \
     -e POSTGRES_USER=postgres \
     -e POSTGRES_PASSWORD=root \
     -e POSTGRES_DB=postgres \
     -p 5432:5432 \
     -d postgres:16
   ```

   При повторном запуске достаточно `docker start task-postgres`.

3. Проверить подключение в `src/main/resources/application.properties`. В текущем проекте указано:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
   spring.datasource.username=postgres
   spring.datasource.password=root
   ```

   Если PostgreSQL уже запущен с другими параметрами, нужно изменить настройки приложения. Пароль выше предназначен только для локального учебного запуска.

4. Из корня проекта запустить приложение:

   ```bash
   chmod +x mvnw
   ./mvnw spring-boot:run
   ```

После запуска запросы можно отправлять на `http://localhost:8080` через Postman.

## API

| Метод | Адрес | Описание |
| --- | --- | --- |
| `POST` | `/tasks` | Создать задачу |
| `GET` | `/tasks/{id}` | Получить задачу по ID |
| `GET` | `/tasks` | Получить список задач с фильтрами |
| `PUT` | `/tasks/{id}` | Обновить задачу |
| `DELETE` | `/tasks/{id}/cancel` | Отменить задачу |
| `POST` | `/tasks/{id}/start` | Начать выполнение |
| `POST` | `/tasks/{id}/complete` | Завершить выполнение |

Отмена не удаляет запись из базы, а меняет её статус на `CANCEL`.

### Пример запроса

Создание задачи — `POST http://localhost:8080/tasks` с заголовком `Content-Type: application/json`:

```json
{
  "creatorId": 1,
  "assignedUserId": 2,
  "createDateTime": "2026-10-31T12:00:00",
  "deadlineDate": "2027-01-15T18:00:00",
  "priority": "HIGH"
}
```

`deadlineDate` должен быть не раньше момента отправки запроса. Поля `id`, `status` и `doneDateTime` при создании заполнять не нужно.

Пример поиска задач исполнителя:

```http
GET http://localhost:8080/tasks?assignedUserId=2&status=CREATED&pageSize=10&pageNumber=0
```

Также доступны фильтры `creatorId` и `priority` (`LOW`, `MEDIUM`, `HIGH`). Нумерация страниц начинается с нуля. Если параметры пагинации не переданы, используются `pageSize=10` и `pageNumber=0`.

## О реализации

Контроллеры принимают HTTP-запросы, сервисный слой содержит бизнес-логику, а репозитории работают с PostgreSQL через Spring Data JPA. Для преобразования сущностей используется отдельный mapper. Валидация реализована через Bean Validation, обработка исключений — через `@ControllerAdvice`.

Проект учебный. Основные сценарии я проверял локально через Postman с PostgreSQL в Docker.
