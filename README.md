<p align="center">
  <img src="./EverGreen_Bank_Logo.png" alt="Evergreen Bank Logo" width="200"/>
</p>

# 🌲 Evergreen Bank API

[![CI](https://github.com/EkimBolat/evergreen-bank/actions/workflows/ci.yml/badge.svg)](https://github.com/EkimBolat/evergreen-bank/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-4169E1?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Containerized-2496ED?logo=docker&logoColor=white)
![JWT](https://img.shields.io/badge/Auth-JWT-black?logo=jsonwebtokens&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

A RESTful Banking API built with **Spring Boot**, **PostgreSQL**, and **Spring Data JPA** — with a twist: every transaction contributes to real-world environmental impact through our **Nature Points** system.

---

## 🌳 What Makes This Different

Every deposit, withdrawal, and transfer earns customers **Nature Points**. Once a customer accumulates enough points, a **real tree gets planted** on their behalf, and they receive a unique digital certificate as proof of their contribution.

---

## ✨ Key Features

- 🔐 **JWT Authentication** with role-based access control (Customer / Admin)
- 🛡️ **Fraud protection**: brute-force login lockout, daily/monthly withdrawal limits
- 🌱 **Nature Points**: gamified sustainability system with anti-abuse safeguards
- 🏦 **Full banking core**: accounts (Checking/Savings), transfers, transaction history, branches
- ⏰ **Scheduled recurring transfers** (daily/weekly/monthly), processed via nightly batch job
- 🐳 **Dockerized**, fully tested (unit + integration), documented via Swagger

---

## 🛠️ Tech Stack

- Java 21
- Spring Boot 4.1.0
- PostgreSQL
- Spring Data JPA
- Docker
- JWT Authentication
- Maven

---

## 🚀 Getting Started

```bash
git clone https://github.com/EkimBolat/evergreen-bank.git
cd evergreen-bank
docker compose up --build
```

The API will be available at `http://localhost:8080`, with interactive documentation at `http://localhost:8080/swagger-ui/index.html`.

---

## 🗺️ Roadmap

**Core:** ✅ Project setup · PostgreSQL · Account Management · Deposit & Withdrawal · Money Transfer · Transaction History · JWT · Docker · Tests

**Extended:**
- [x] Account Types (Checking / Savings with interest rate)
- [x] Branch Management + Account-Branch Assignment
- [x] Nature Points & Tree Certificates (with anti-abuse protection)
- [x] Login with National ID + Brute-Force Protection
- [x] Daily/Monthly Withdrawal Limits
- [x] Role-Based Access Control (Customer / Admin)
- [x] Pagination, Idempotency, Refresh Tokens, API Versioning
- [x] CI/CD Pipeline + Test Coverage Reporting
- [x] Health Check Endpoint
- [x] Scheduled/Recurring Transfers

---

📌 **Status:** Core banking features complete — actively expanding functionality

📄 **License:** [MIT](./LICENSE)