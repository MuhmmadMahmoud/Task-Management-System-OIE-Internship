# Task Manager

My mini project for the OIE software internship.

It is a small task management app. A user makes an account, logs in, and then he can add,
see, change and delete **his own** tasks. Every task has a status (TODO, IN_PROGRESS, DONE)
and a priority (LOW, MEDIUM, HIGH), and he can filter his tasks by them.

**Backend:** Java 21, Spring Boot 3.4.1, Spring Security with JWT, Spring Data JPA, Maven
**Database:** PostgreSQL 16 · **Frontend:** Angular 19 · **Tests:** Cucumber (BDD)
**Also:** Docker, Docker Compose, GitHub Actions

## How to run it

### With Docker

You only need Docker:

```bash
docker compose up --build
```

Then open the app on http://localhost:4200 and the API is on http://localhost:8080.
To stop it use `docker compose down`, and add `-v` if you want to delete the data also.

The app uses the ports 4200 and 8080, so if another program is using them please close it first.

### Without Docker

You need Java 21, Node 22, and a PostgreSQL that is running.

```bash
psql -d postgres -c "CREATE DATABASE taskdb;"

cd backend
DB_USERNAME=postgres DB_PASSWORD=yourpassword ./mvnw spring-boot:run
```

And in another terminal:

```bash
cd frontend
npm install
npm start
```

Hibernate makes the tables the first time, so there is no SQL file to run.

### The tests

```bash
cd backend
./mvnw test
```

## The endpoints

`/api/auth/register` and `/api/auth/login` are open. Everything else needs the header
`Authorization: Bearer <token>`.

| Method | Endpoint | What it does |
|--------|----------|--------------|
| POST | `/api/auth/register` | makes a new user and gives back a token |
| POST | `/api/auth/login` | checks the password and gives back a token |
| GET | `/api/tasks` | my tasks, and it takes `?status=` and `?priority=` for the filters |
| GET | `/api/tasks/{id}` | one task of mine |
| POST | `/api/tasks` | make a new task |
| PUT | `/api/tasks/{id}` | change one of my tasks |
| DELETE | `/api/tasks/{id}` | delete one of my tasks |

All the errors come back in the same shape so the frontend always reads the same field:

```json
{ "message": "Task not found with id 5" }
```

400 means the data is wrong or the username is taken, 401 means the token is missing or
wrong, and 404 means the task is not there **or it belongs to another user**.

## The architecture

```
    Controller        takes the http request and gives back the answer
        |
        v
     Service          all the rules of the app are here
        |
        v
   Repository         this one only talks to the database
        |
        v
     Database
```

Beside these there is **model** for the entities, **dto** for what goes in and out of the
API, **security** for the JWT classes, and **exception** for my exceptions and the handler.

### How the security works

The user logs in and gets a JWT token. Angular keeps it in `localStorage` and the
`authInterceptor` puts it in every request. In the backend the `JwtFilter` runs before the
controller, opens the token and tells Spring Security who the user is.

The controller reads the username from `Authentication` and not from the url, so nobody can
write the name of another user and read his tasks. Every method in `TaskRepository` also
takes the user, like `findByIdAndUser(id, user)`, so a task of another user comes back
empty and the answer is 404.

### The OOP principles

- **Encapsulation** — all the fields are private and I reach them by the getters and setters
- **Inheritance** — `User` and `Task` both extend `BaseEntity`, which holds the id
- **Abstraction** — `BaseEntity` is abstract, and the services are interfaces
- **Polymorphism** — the controller holds the interface type, Spring puts the `Impl` inside it

## The design patterns

**Repository** — `UserRepository` and `TaskRepository` extend `JpaRepository`, so I do not
write SQL and the service asks for data without knowing the database.

**DTO** — I do not send the `Task` entity out, because it holds the `User` and its hashed
password. And `TaskRequest` has no id and no user, so nobody can put his task on another user.

**Dependency injection** — every class takes what it needs in its constructor and Spring
gives it, so I can put a fake object in a test.

**Chain of responsibility** — `JwtFilter` is inside the Spring Security filter chain, so the
token is checked in one place and not again in every controller method.

**Interceptor (Angular)** — `authInterceptor` adds the token to every request, so
`TaskService` stays clean.

## The database

Two tables, and one user has many tasks. The diagram is in [docs/ERD.md](docs/ERD.md).

## The tests

I wrote the `.feature` files first, then the step definitions, then the code.

| File | Scenarios |
|------|-----------|
| `authentication.feature` | 7 |
| `task_management.feature` | 11 |
| `task_security.feature` | 5 |

**23 scenarios and all of them pass.** They start the real app with `@SpringBootTest` and
call the real API, so one scenario tests the controller, the service, the repository and the
security together. They use H2 in the memory so they run anywhere.

The pipeline in `.github/workflows/ci.yml` runs on every push, builds the project, runs these
tests, and builds the Angular app.

## The libraries I added

| Library | Why |
|---------|-----|
| **jjwt** | Spring Security checks a token but does not make JWT tokens alone, jjwt makes it and reads it |
| **Cucumber** | the task asked for BDD with `.feature` files, and Cucumber is what runs Gherkin in Java |
| **H2** (tests only) | so the tests do not need a real PostgreSQL, this is what makes them run on GitHub Actions |
| **PostgreSQL driver** | without it Java can not talk to PostgreSQL |

## The branches

`main` has the finished code, I merge into `develop` first, and every feature has its own
`feature/...` branch.
