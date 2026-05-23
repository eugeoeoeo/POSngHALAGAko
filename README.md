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
