# JPMorgan Chase - Midas Core Transaction Processing System

A complete microservices-based transaction processing system built with Spring Boot, demonstrating real-world software engineering practices including Kafka integration, REST APIs, database persistence, and microservices architecture.

## 🎯 Project Overview

Midas Core is a financial transaction processing system that:
- Processes transactions via Kafka message streaming
- Validates transactions and manages user balances
- Integrates with external incentive APIs
- Provides REST endpoints for balance queries
- Persists data using JPA/Hibernate with H2 database

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    MIDAS CORE (Port 33400)              │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌─────────────────┐         ┌──────────────────┐     │
│  │ Kafka Consumer  │────────▶│ Transaction      │     │
│  │ (trader-updates)│         │ Service          │     │
│  └─────────────────┘         └────────┬─────────┘     │
│                                        │               │
│                              ┌─────────▼─────────┐     │
│                              │ Incentive Service │     │
│                              │ (REST Client)     │     │
│                              └─────────┬─────────┘     │
│                                        │               │
│  ┌─────────────────┐         ┌────────▼─────────┐     │
│  │ Balance         │────────▶│ User Repository  │     │
│  │ Controller      │         │ (H2 Database)    │     │
│  │ (REST API)      │         └──────────────────┘     │
│  └─────────────────┘                                   │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

## 🚀 Features

### ✅ Transaction Processing
- **Kafka Integration**: Consumes transaction messages from `trader-updates` topic
- **Validation**: Validates sender/recipient existence and sufficient balance
- **Atomic Operations**: Uses `@Transactional` for data consistency
- **Balance Management**: Automatically updates sender and recipient balances

### ✅ Incentive System
- **REST Client**: Integrates with external incentive API
- **Bonus Calculation**: Retrieves incentive amounts for each transaction
- **Smart Distribution**: Adds incentives to recipient (not deducted from sender)

### ✅ Balance Query API
- **REST Endpoint**: `GET /balance?userId={id}`
- **JSON Response**: Returns user balance as JSON
- **Error Handling**: Returns 0 for non-existent users

### ✅ Data Persistence
- **JPA/Hibernate**: ORM for database operations
- **H2 Database**: In-memory database for development
- **Entity Relationships**: Many-to-one relationships between transactions and users
- **Transaction Records**: Stores complete transaction history with incentives

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.2.5
- **Language**: Java 17
- **Build Tool**: Maven
- **Messaging**: Apache Kafka 3.1.4
- **Database**: H2 2.2.224
- **ORM**: Spring Data JPA
- **Testing**: JUnit 5, Spring Boot Test, Embedded Kafka, Testcontainers

## 📋 Prerequisites

- Java 17
- Maven 3.6+
- Docker (optional, for Kafka)

## 🔧 Setup & Installation

### 1. Clone the Repository
```bash
git clone https://github.com/yashsanap14/forage-midas.git
cd forage-midas
```

### 2. Install Java 17
```bash
# Using Homebrew (macOS)
brew install openjdk@17

# Or download from Oracle
# https://www.oracle.com/java/technologies/downloads/#java17
```

### 3. Build the Project
```bash
./mvnw clean install
```

### 4. Run the Application
```bash
./mvnw spring-boot:run
```

The application will start on **port 33400**.

## 🧪 Running Tests

### Run All Tests
```bash
./mvnw test
```

### Run Specific Task Tests
```bash
# Task 1: Setup Verification
./mvnw -Dtest=TaskOneTests test

# Task 3: Database Integration
./mvnw -Dtest=TaskThreeTests test

# Task 4: Incentive API Integration
./mvnw -Dtest=TaskFourTests test

# Task 5: Balance Query API
./mvnw -Dtest=TaskFiveTests test
```

## 📊 Project Structure

```
forage-midas/
├── src/
│   ├── main/
│   │   ├── java/com/jpmc/midascore/
│   │   │   ├── config/           # Configuration classes
│   │   │   │   ├── KafkaConsumerConfig.java
│   │   │   │   ├── KafkaProducerConfig.java
│   │   │   │   └── RestTemplateConfig.java
│   │   │   ├── consumer/         # Kafka consumers
│   │   │   │   └── TransactionConsumer.java
│   │   │   ├── controller/       # REST controllers
│   │   │   │   └── BalanceController.java
│   │   │   ├── entity/           # JPA entities
│   │   │   │   ├── UserRecord.java
│   │   │   │   └── TransactionRecord.java
│   │   │   ├── foundation/       # Domain models
│   │   │   │   ├── Transaction.java
│   │   │   │   ├── Balance.java
│   │   │   │   └── Incentive.java
│   │   │   ├── repository/       # Data repositories
│   │   │   │   ├── UserRepository.java
│   │   │   │   └── TransactionRepository.java
│   │   │   ├── service/          # Business logic
│   │   │   │   ├── TransactionService.java
│   │   │   │   └── IncentiveService.java
│   │   │   └── MidasCoreApplication.java
│   │   └── resources/
│   │       └── application.yml   # Application configuration
│   └── test/                     # Test files
├── services/                     # External services
│   └── transaction-incentive-api.jar
├── pom.xml                       # Maven configuration
└── README.md                     # This file
```

## 🔑 Key Components

### Transaction Processing Flow

1. **Message Reception**: Kafka consumer receives transaction from `trader-updates` topic
2. **Validation**: TransactionService validates sender, recipient, and balance
3. **Incentive Calculation**: IncentiveService calls external API for bonus amount
4. **Balance Update**: Sender balance decreased, recipient balance increased (+ incentive)
5. **Persistence**: Transaction record saved to database with all details

### API Endpoints

#### Get User Balance
```http
GET /balance?userId={id}
```

**Response:**
```json
{
  "amount": 1200.23
}
```

## 📈 Task Completion

- ✅ **Task 1**: Project Setup & Configuration
- ✅ **Task 2**: Maven Dependencies Integration
- ✅ **Task 3**: H2 Database Integration (Answer: 627)
- ✅ **Task 4**: Incentive API Integration (Answer: 3089)
- ✅ **Task 5**: Balance Query REST API

## 📝 Configuration

### Application Properties (`application.yml`)

```yaml
general:
  kafka-topic: trader-updates

server:
  port: 33400
```

### Dependencies

- Spring Boot Starter Data JPA (3.2.5)
- Spring Boot Starter Web (3.2.5)
- Spring Kafka (3.1.4)
- H2 Database (2.2.224)
- Spring Boot Starter Test (3.2.5)
- Spring Kafka Test (3.1.4)
- Testcontainers Kafka (1.19.1)

## 🎓 Learning Outcomes

This project demonstrates:

1. **Microservices Architecture**: Separation of concerns with modular design
2. **Event-Driven Design**: Kafka-based message processing
3. **REST API Development**: Building and consuming RESTful services
4. **Database Design**: JPA entities with proper relationships
5. **Transaction Management**: Ensuring data consistency with `@Transactional`
6. **Testing**: Unit and integration testing with embedded Kafka
7. **Configuration Management**: Spring Boot configuration and profiles
8. **Build Automation**: Maven for dependency management and builds

## 🤝 Contributing

This is a learning project completed as part of the JPMorgan Chase Software Engineering Virtual Experience on Forage.

## 📄 License

This project is part of the JPMorgan Chase Forage program.

## 🙏 Acknowledgments

- JPMorgan Chase for the virtual experience program
- Forage for providing the platform
- The Spring Boot and Apache Kafka communities

## 📧 Contact

**Yash Sanap**
- GitHub: [@yashsanap14](https://github.com/yashsanap14)

---

**Built with ❤️ as part of JPMorgan Chase Software Engineering Virtual Experience**
