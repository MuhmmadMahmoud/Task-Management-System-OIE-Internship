# Mini Project Individual Task — Software Internship

## 🎯 Project Overview

**Project Title:** *Full-Stack Task Management Application*

> Each candidate must individually design, develop, and deliver a small but complete
> web application that demonstrates their understanding of the core concepts covered
> during the internship.

---

## 📋 Project Description

The candidate must build a **Task Management REST API with a basic Angular frontend**,
where users can register, log in, and manage their personal tasks.

---

## ✅ Functional Requirements

| # | Feature |
|---|---------|
| 1 | User registration and login |
| 2 | Create, read, update, and delete (CRUD) tasks |
| 3 | Assign a **status** to each task *(e.g., TODO, IN_PROGRESS, DONE)* |
| 4 | Assign a **priority** to each task *(e.g., LOW, MEDIUM, HIGH)* |
| 5 | Filter tasks by status or priority |
| 6 | Each user can only see and manage their own tasks |

---

## 🛠️ Technical Requirements

### 🔧 Backend
- **Java & OOP** — Apply proper OOP principles
*(encapsulation, inheritance, polymorphism, abstraction)*
- **Spring Boot** — Build a RESTful API
- **Spring Security** — Secure endpoints with JWT-based authentication
- **Hibernate & Spring Data JPA** — Persist data using JPA repositories
- **Relational Database** — Use **PostgreSQL**; provide an ERD diagram
- **Design Patterns** — Document their usage if any
- **Maven** — Use Maven for dependency management and build lifecycle
- **Code Quality** — Follow clean code principles; no unused imports,
meaningful naming, proper layering *(Controller → Service → Repository)*

### 🌐 Frontend
- **Angular** — Build a simple UI with at minimum:
  - Login / Register pages
  - Task list page with filters
  - Create / Edit task form

### 🐳 DevOps & Tooling
- **Git** — Use a Git repository with **meaningful commit messages**
and a proper branching strategy *(e.g., `main`, `develop`, `feature/...`)*
- **Docker** — Provide a `Dockerfile` for the backend and a `docker-compose.yml`
to run the full stack *(backend + database)*
- **CI/CD** — Set up a basic pipeline *(GitHub Actions or GitLab CI)* that:
  - Builds the project
  - Runs tests

### 🧪 Testing
- **BDD** — All backend scenarios must have BDD tests written for them first,
using Cucumber `.feature` files

---

## 📁 Deliverables

| Deliverable | Details |
|-------------|---------|
| 📂 Git Repository | Shared link with full commit history |
| 📄 README.md | Setup instructions, architecture overview, design pattern explanations |
| 🗃️ ERD Diagram | Database schema diagram |
| 🧪 BDD Scenarios | `.feature` files included in the repository |
| 🐳 Docker Setup | `Dockerfile` + `docker-compose.yml` |

---

## 📅 Deadline

> **All deliverables must be submitted by Sunday, 2nd of August.**

---

## 💡 Notes for Candidates

> - Focus on **quality over quantity** — a smaller, well-structured project
>   is better than a large, messy one.
> - **Plagiarism is not tolerated** — each submission must be the candidate's own work.
> - External libraries are allowed but must be **justified in the README**.
> - Time is tight — **plan before you code** and avoid over-engineering.
