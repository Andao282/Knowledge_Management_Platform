# Library Management System API (QLSach)

A RESTful backend for managing a library: books, authors, publishers, categories, readers, staff, borrowing/returning, fines, and online fine payment through VNPay.

Built with **Java 17** and **Spring Boot**, following a layered **Controller → Service → Repository** architecture. Authentication is stateless and based on **JWT**, with role-based authorization for administrative operations.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Data Model](#data-model)
- [API Overview](#api-overview)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Testing](#testing)
- [Project Structure](#project-structure)
- [Notes and Future Work](#notes-and-future-work)

## Features

- **Authentication & authorization** — login with BCrypt-hashed passwords, stateless JWT access tokens, role-based access control (`ROLE_ADMIN`, `ROLE_STAFF`) via `@PreAuthorize`.
- **Catalog management** — CRUD for books, authors, publishers, and categories, including search, paging, and batch creation.
- **Reader and staff management** — CRUD, batch creation, and search by name.
- **Borrowing and returning** — create borrow slips with multiple books, validate stock, decrement/increment inventory, and track per-book return condition.
- **Fine calculation** — overdue fines (5,000 VND/day) and damage fees (50,000 VND), created automatically during return.
- **Overdue scheduler** — a scheduled job scans loans that are past their expected return date and creates/updates fine slips.
- **Online payment** — VNPay sandbox integration with HMAC-SHA512 request signing and callback signature verification.
- **API documentation** — interactive Swagger UI generated with springdoc-openapi.
- **Consistent error handling** — validation errors, business errors, and access-denied errors are returned through a global exception handler.

## Tech Stack

| Category | Technology |
| --- | --- |
| Language | Java 17 |
| Framework | Spring Boot 4.1 |
| Web | Spring MVC, REST |
| Persistence | Spring Data JPA, Hibernate, MySQL 8 |
| Security | Spring Security, JWT (jjwt 0.11.5), BCrypt |
| Validation | Jakarta Bean Validation |
| Documentation | springdoc-openapi (Swagger UI) |
| Payment | VNPay Sandbox (HMAC-SHA512) |
| Build | Maven (Maven Wrapper included) |
| Testing | JUnit 5, Mockito, AssertJ, H2 (in-memory) |
| Utilities | Lombok |

## Architecture

```
HTTP request
    │
    ▼
JwtAuthFilter ──► SecurityFilterChain (route protection, RBAC)
    │
    ▼
Controller  (@RestController)  ── request/response mapping, validation
    │
    ▼
Service     (@Service)         ── business rules, transactions
    │
    ▼
Repository  (JpaRepository)    ── persistence
    │
    ▼
MySQL
```

Cross-cutting concerns:

- `GlobalExceptionHandler` maps domain and validation exceptions to JSON error responses.
- DTOs keep API contracts separate from JPA entities.
- `LibraryScheduler` runs overdue detection outside the request path.

## Data Model

Core entities (see the ERD for relationships):

| Entity | Description |
| --- | --- |
| `Account` | Login credentials, role, status, linked staff member |
| `Staff` | Library staff |
| `Reader` | Library card holder |
| `Author` | Book author |
| `Publisher` | Book publisher |
| `Category` | Book category |
| `Book` | Book metadata and available quantity |
| `BorrowSlips` | Borrowing header (reader, staff, dates, returned flag) |
| `BorrowDetails` | Borrowed books with composite key (`BorrowID`, `BookID`) |
| `FineSlips` | Fine for an overdue or damaged book |

![ERD](docs/QLSach.png)

## API Overview

Base path: `/Knowledge_Management_Platform`

Authentication:

- `POST /Knowledge_Management_Platform/accounts/create` — create an account (public)
- `POST /Knowledge_Management_Platform/accounts/login` — obtain a JWT (public)

Books:

| Method | Path | Description |
| --- | --- | --- |
| GET | `/books` | List all books |
| GET | `/books/{id}` | Get a book by ID |
| GET | `/books/page?page=0&size=3&sortBy=bookID` | Paged and sorted listing |
| GET | `/books/search-by-name?name=` | Search by name |
| GET | `/books/search-by-category?categoryName=` | Search by category |
| POST | `/books` | Create a book (admin) |
| POST | `/books/multiple-books` | Batch create (admin) |
| PUT | `/books/{id}` | Update (admin) |
| DELETE | `/books/{id}` | Delete (admin) |

Authors, categories, publishers, readers, and staff expose the same CRUD shape:

| Resource | Base path | Extra endpoints |
| --- | --- | --- |
| Authors | `/authors` | `GET /authors/search-by-name` |
| Categories | `/categories` | `GET /categories/{search}` |
| Publishers | `/publishers` | `GET /publishers/search-by-name` |
| Readers | `/readers` | `GET /readers/{search-by-name}` |
| Staff | `/staffs` | `GET /staffs/{search-by-name}` |

Each resource also supports `POST /{resource}`, `POST /{resource}/multiple-*`, `PUT /{resource}/{id}`, and `DELETE /{resource}/{id}`.

Borrowing:

| Method | Path | Description |
| --- | --- | --- |
| POST | `/borrows` | Create a borrow slip with one or more books |
| POST | `/borrows/return` | Return one or more books and calculate fines |
| GET | `/borrows?page=0&size=10&sortBy=borrowDate` | Paged listing |
| GET | `/borrows/{borrowID}` | Borrow slip details |
| GET | `/borrows/reader/{readerID}` | Borrowing history of a reader |

Fines and payment:

| Method | Path | Description |
| --- | --- | --- |
| PUT | `/Knowledge_Management_Platform/fines/{fineID}/pay` | Mark a fine as paid |
| GET | `/api/payment/create-payment?fineId=&amount=` | Build a VNPay payment URL |
| GET | `/api/payment/vnpay-callback` | VNPay return/callback endpoint |

Swagger UI: `http://localhost:8080/swagger-ui.html`  
OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Getting Started

### Prerequisites

- JDK 17 or newer
- MySQL 8
- No local Maven installation required — the Maven Wrapper is included

### 1. Create the database

```sql
CREATE DATABASE Book_Manage_System
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

Hibernate is configured with `ddl-auto=update`, so tables are created on first start.

### 2. Set environment variables

The application reads secrets from environment variables. On Windows PowerShell:

```powershell
$env:DB_PASSWORD="your_mysql_password"
$env:GMAIL_APP_PASSWORD="your_gmail_app_password"
$env:VNPAY_HASH_SECRET="your_vnpay_sandbox_hash_secret"
```

On macOS/Linux:

```bash
export DB_PASSWORD="your_mysql_password"
export GMAIL_APP_PASSWORD="your_gmail_app_password"
export VNPAY_HASH_SECRET="your_vnpay_sandbox_hash_secret"
```

### 3. Run the application

```powershell
# Windows
.\mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`.

### 4. Authenticate

```http
POST /Knowledge_Management_Platform/accounts/login
Content-Type: application/json

{
  "username": "admin",
  "password": "your_password"
}
```

The response body contains the JWT. Send it on protected endpoints:

```http
Authorization: Bearer <token>
```

In Swagger UI, click **Authorize** and paste the token.

## Configuration

All settings live in [`src/main/resources/application.properties`](src/main/resources/application.properties).

| Property | Environment variable | Default |
| --- | --- | --- |
| `spring.datasource.url` | — | `jdbc:mysql://localhost:3306/Book_Manage_System` |
| `spring.datasource.username` | — | `root` |
| `spring.datasource.password` | `DB_PASSWORD` | `root` |
| `spring.mail.username` | — | `kmplatform1@gmail.com` |
| `spring.mail.password` | `GMAIL_APP_PASSWORD` | — |
| `vnpay.tmnCode` | — | `PA1XDOO5` |
| `vnpay.hashSecret` | `VNPAY_HASH_SECRET` | — |
| `vnpay.url` | — | VNPay sandbox payment URL |
| `vnpay.returnUrl` | — | VNPay sandbox return URL |

Never commit real secrets. Use environment variables or a local profile that is excluded from Git.

## Testing

Run the test suite with:

```powershell
# Windows
.\mvnw.cmd test

# macOS / Linux
./mvnw test
```

Tests use an in-memory **H2** database configured in
[`src/test/resources/application.properties`](src/test/resources/application.properties),
so **no MySQL instance is required** to run them.

Current coverage focuses on the service layer:

| Test class | What it verifies |
| --- | --- |
| `BorrowServiceTest` | Stock decrement, out-of-stock handling, missing reader, overdue fine, damage fee, on-time return, double return |
| `AccountServiceTest` | Password hashing, staff linkage, login success, disabled account, wrong password, unknown user |
| `BookServiceTest` | Book creation and DTO mapping, duplicate ID, missing author, missing book, delete validation |
| `JwtServiceTest` | Token structure, username and role claims |
| `VNPayConfigTest` | HMAC-SHA512 determinism, hex encoding, key/data sensitivity, null handling |
| `QlsachApiApplicationTests` | Spring context loads successfully |

## Project Structure

```
src
├── main
│   ├── java/com/oadna/qlsach_api
│   │   ├── config          # Security, JWT filter, OpenAPI, VNPay helper
│   │   ├── controller      # REST endpoints
│   │   ├── dto             # Request/response models
│   │   ├── entity          # JPA entities
│   │   ├── exception       # Global exception handler
│   │   ├── repository      # Spring Data repositories
│   │   ├── scheduler       # Overdue fine scheduler
│   │   └── service         # Business logic and transactions
│   └── resources
│       └── application.properties
└── test
    ├── java/com/oadna/qlsach_api
    │   ├── config
    │   └── service
    └── resources
        └── application.properties   # H2 test configuration
```

## Notes and Future Work

- Externalize the JWT signing key through an environment variable.
- Replace `System.out.println` logging with SLF4J.
- Introduce Flyway or Liquibase instead of `ddl-auto=update`.
- Add controller/integration tests (MockMvc) and a CI workflow.
- Add Docker Compose for the application and MySQL.
- Restrict the VNPay payment amount to the value stored in the database instead of the request parameter.

## Author

**Dao Hoang An** — [github.com/Andao282](https://github.com/Andao282)
