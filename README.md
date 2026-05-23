# 💖 POSngHALAGAko

A simple JavaFX Point-of-Sale (POS) application for selling emotions and feelings — built as a practice project for Object-Oriented Programming.

> *"POS ng Halaga Ko"* — Your one-stop shop for emotions.

## About

POSngHALAGAko is a demo POS system where products are emotions like Love, Happiness, Fulfillment, and more. It features user authentication, a point-of-sale interface, and product management — all connected to a Supabase PostgreSQL database.

## Features

- **Login / Sign Up** — User authentication with SHA-256 password hashing
- **Dashboard** — Navigation hub to POS or Product Management
- **Point of Sale** — Browse products, add to cart, checkout with payment and change calculation
- **Product Management** — View, add, edit, and delete products (emotions catalog)

## Tech Stack

- **Java 21** + **JavaFX 21**
- **Maven** — Build tool
- **PostgreSQL** — Database (via Supabase)
- **JDBC** — Database connectivity with `PreparedStatement` (SQL injection prevention)
- **dotenv-java** — Environment variable management

## Project Structure

```
src/main/java/com/pos/posnghalagako/
├── Launcher.java                  # Entry point
├── app/
│   └── POSApplication.java       # JavaFX Application
├── controller/
│   ├── LoginController.java
│   ├── SignUpController.java
│   ├── DashboardController.java
│   ├── POSController.java
│   └── ProductController.java
├── factory/
│   └── SceneFactory.java         # Scene creation from FXML
├── model/
│   ├── UserAccount.java
│   ├── Product.java
│   ├── Transaction.java
│   ├── TransactionItem.java
│   └── CartItem.java
├── repository/
│   ├── AuthRepository.java
│   ├── ProductRepository.java
│   └── TransactionRepository.java
└── util/
    ├── Database.java             # JDBC connection via .env
    ├── PasswordUtil.java         # SHA-256 hashing
    └── SessionManager.java       # Session tracking
```

## Setup

### 1. Database

Run `posnghalagako.sql` in your Supabase SQL Editor to create the tables and seed data.

### 2. Environment Variables

Create a `.env` file in the project root:

```
DB_URL=jdbc:postgresql://your-host:6543/postgres
DB_USER=your-user
DB_PASSWORD=your-password
```

### 3. Run

```bash
# Using Maven wrapper
./mvnw.cmd javafx:run

# Or run Launcher.java directly in IntelliJ
```

## Default Accounts

| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | Admin |
| cashier | password | Cashier |

## Security

- Passwords are hashed with **SHA-256** before storage
- All database queries use **PreparedStatement** to prevent SQL injection
- Credentials stored in `.env` file (excluded from version control via `.gitignore`)

---

*Built for OOP class — practice project.*
