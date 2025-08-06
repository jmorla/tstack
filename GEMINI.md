# Tstack Project Analysis

This document provides an overview of the Tstack project, a Spring Boot application.

## Project Overview

Tstack is a Java-based application built with the Spring Boot framework. It appears to be a command-line application that interacts with a database to manage financial market data, specifically symbols and trend bars.

### Key Technologies

*   **Java 21:** The project is built using Java version 21.
*   **Spring Boot 3.5.4:** The core framework for the application.
*   **Spring Shell:** Provides the command-line interface functionality.
*   **Spring Data JDBC:** Used for database interaction.
*   **Flyway:** For database schema migrations.
*   **H2 Database:** An in-memory database used for development and testing.
*   **Lombok:** A library to reduce boilerplate code.
*   **Maven:** The build and dependency management tool.

### Core Concepts

The application revolves around two main entities:

*   **Symbol:** Represents a financial instrument (e.g., a stock or currency pair). It stores information like the symbol name, description, volume constraints, and other trading-related properties.
*   **TrendBar:** Represents a single bar in a price chart, capturing the open, high, low, and close prices for a specific time frame (e.g., 1 minute, 1 hour, 1 day).

### Database Schema

The database schema is managed by Flyway migrations. Two tables are created:

1.  `symbols`: Stores information about the financial symbols.
2.  `trendbar_history`: Stores the historical trend bar data for each symbol.

### Application Structure

*   **`TstackApplication.java`:** The main entry point for the Spring Boot application.
*   **`entities` package:** Contains the `Symbol` and `TrendBar` entity classes, which map to the database tables.
*   **`repositories` package:** Contains the Spring Data JDBC repositories (`SymbolRepository` and `TrendBarRepository`) for accessing and manipulating the database.
*   **`resources` directory:**
    *   `application.yaml`: The main configuration file for the application.
    *   `db/migration`: Contains the Flyway SQL scripts for database schema creation.

## How to Run

The application can be run as a standard Spring Boot application using Maven:

```bash
./mvnw spring-boot:run
```

This will start the application and the Spring Shell, allowing you to interact with the application through the command line.
