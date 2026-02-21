# BlackRock Hacking India 2026
## Retirement Auto-Savings & Investment Returns API

---

## 📌 Overview

This project implements a production-grade REST API for automated retirement savings using expense rounding and investment strategies.

The system:

- Rounds expenses to the nearest multiple of 100
- Validates transactions
- Applies temporal constraints (q, p, k periods)
- Calculates investment returns for:
    - Index funds
    - NPS (including tax benefits)
- Adjusts returns for inflation
- Provides performance metrics
- Is fully containerized using Docker

The implementation strictly follows the processing order defined in the challenge specification.

---

## 🛠 Tech Stack

- Java 17
- Spring Boot
- Maven
- Docker
- JUnit 5

---

## 📂 Project Structure


src/main/java → Application source code
src/test/java → Unit tests
Dockerfile → Docker container configuration
pom.xml → Maven configuration
README.md → Project documentation


---

## 🚀 Running the Application Locally

### 1️⃣ Build the Project

```bash
mvn clean package
2️⃣ Run the Application
java -jar target/*.jar

Application runs on:

http://localhost:5477
🐳 Running with Docker (Required for Evaluation)
1️⃣ Build Docker Image
docker build -t blk-hacking-ind-manikanta-k .
2️⃣ Run Docker Container
docker run -d -p 5477:5477 blk-hacking-ind-manikanta-k

Application will be available at:

http://localhost:5477

Docker image uses a lightweight Linux-based image:

eclipse-temurin:17-jdk-alpine
📡 API Endpoints
🔹 Parse Transactions

POST

/blackrock/challenge/v1/transactions:parse

Rounds expense amounts to the nearest multiple of 100 and calculates remanent.

🔹 Validate Transactions

POST

/blackrock/challenge/v1/transactions:validator

Validates:

Date format

Negative amounts

Duplicate timestamps

Upper limit constraints

🔹 Apply Temporal Rules

POST

/blackrock/challenge/v1/transactions:filter

Applies:

q period fixed override

p period extra addition

k period grouping logic

🔹 Calculate Returns (Index)

POST

/blackrock/challenge/v1/returns:index

Calculates:

Compound returns

Inflation-adjusted real returns

🔹 Calculate Returns (NPS)

POST

/blackrock/challenge/v1/returns:nps

Calculates:

Compound returns

Inflation-adjusted real returns

Tax benefits using slab-based calculation

🔹 Performance Metrics

POST

/blackrock/challenge/v1/performance

Returns:

Execution time (ms)

Memory usage (MB)

Active thread count

📊 Business Logic Summary
Expense Rounding
ceiling = ceil(amount / 100) * 100
remanent = ceiling - amount
Compound Growth Formula
A = P * (1 + r)^n
Inflation Adjustment
real = A / (1 + inflation)^n
NPS Tax Benefit

10% of annual income

Maximum cap ₹200,000

Slab-based tax calculation

🧪 Testing

Unit tests are included in:

src/test/java

Run tests using:

mvn test

Tests cover:

Expense parsing

Validation rules

Temporal rule application

Returns calculation