# 📚 Library Management REST API

A simple, production-grade **Library System REST API** built with **Java 17** and **Spring Boot 3**.

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

Actuator - http://localhost:8080/actuator	

App Index → http://localhost:8080 (Html file added only for testing since this is api)	

Base url should be replaced after deployment	

###### Test Coverage using JaCoCo in local set up
	
		mvn clean test
		mvn jacoco:report


# 🐳 Run with Docker

GitHub url: https://github.com/kumarkathir421/library.git

### Steps to be followed using Play with Docker (PWD)
1. Goto link https://labs.play-with-docker.com
2. login -> Start -> Create new instance
3. To build jar and image
	#### Use below commands in PWD Terminal 
		git clone https://github.com/kumarkathir421/library.git
		cd library
		docker build -t library-api:latest .
		docker run -d -p 8080:8080 library-api:latest
4. To verify docker is running

		docker ps
6. Access Your API in Browser
	click the 8080 link in PWD UI / Open Port enter 8080
7. To stop running docker

		docker stop $(docker ps -q)
9. To check the logs
	
		docker logs -f jovial_maxwell
		docker logs jovial_maxwell

#🧩 GitHub Actions (CI/CD)
Each push or PR to main runs:

1. Build + Test (with JaCoCo report)
2. Docker Build Validation
3. Uploads coverage report as artifact

View report in Actions → Artifacts → jacoco-report

Open target/site/jacoco/index.html to view Test coverage.

#🧩 Screenshots
https://github.com/kumarkathir421/library/blob/main/LibraryManagement%20Screenshots.pdf

#🧩 Postman Collections
https://github.com/kumarkathir421/library/blob/main/Library-Management-export-postman.json

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

