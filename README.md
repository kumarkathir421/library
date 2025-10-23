# 📚 Library Management REST API

A simple, production-grade **Library System REST API** built with **Java 17** and **Spring Boot 3**, following clean-code and 12-Factor principles.

---

## 🧩 Features

✅ Register borrowers and books  
✅ Borrow and return books (one borrower per book ID)  
✅ Multiple copies allowed per ISBN  
✅ Validation + global error handling  
✅ In-memory H2 / PostgreSQL (via profiles)  
✅ Full unit + integration tests with JaCoCo coverage  
✅ Containerized with Docker  
✅ Declarative CI/CD (GitHub Actions)

---

## 🧩 Tech Stack

| Layer | Technology |
|--------|-------------|
| Language | Java 17 |
| Framework | Spring Boot 3.x |
| Database | H2 (dev/test) / PostgreSQL (prod) |
| Build Tool | Maven |
| Testing | JUnit 5 + Mockito + SpringBootTest |
| Coverage | JaCoCo |
| Container | Docker + Docker Compose |
| CI/CD | GitHub Actions |

---

## 🧩 Getting Started

### 🧩 Local Setup (Dev Profile)

App starts at → http://localhost:8080
Swagger UI → http://localhost:8080/swagger-ui.html
App Index → http://localhost:8080/index.html (Html file added only for testing since this is api)

# 🐳 Run with Docker
### Build jar and image
mvn clean package -DskipTests
docker build -t library-api .

### Run container
docker run -p 8080:8080 library-api

#☸️ Run with Docker Compose
docker-compose up --build

#🧩 Run Tests + Coverage
mvn clean verify

#🧩 GitHub Actions (CI/CD)
Each push or PR to main runs:

1. Build + Test (with JaCoCo report)
2. Docker Build Validation
3. Uploads coverage report as artifact

View report in Actions → Artifacts → jacoco-report
#🧩 API Endpoints

| HTTP     | Endpoint                                     				| Description             				|
| -------- | ---------------------------------------------------------- | ------------------------------------	|
| **POST** | `/api/borrowers`                             				| Register a new borrower 				|
| **GET**  | `/api/borrowers`											| List all registered borrower 			|
| **POST** | `/api/books`                                 				| Register a new book     				|
| **GET**  | `/api/books`                                 				| List all books          				|
| **POST** | `/api/books/borrow/{bookCode}					` 			| Borrow a book           				|
| **POST** | `/api/books/return/{bookCode}`                 			| Return a book           				|
| **GET**  | `/api/history`												| List all borrowed books				|
| **GET**  | `/api/history/borrower/{code}`								| List all books for sepecific borrower |
| **GET**  | `/api/history/book/{code}`									| List all borrowes for specific book 	|

#🧩 Project Structure

src/main/java/com/example/library
├── controller        # REST Controllers
├── dto               # Request/Response DTOs
├── exception         # Global Error Handling
├── model             # JPA Entities
├── repository        # Spring Data Repositories
├── service           # Business Logic
└── LibraryApiApplication.java

# 🏁 License

This project was developed as part of a technical assessment.

