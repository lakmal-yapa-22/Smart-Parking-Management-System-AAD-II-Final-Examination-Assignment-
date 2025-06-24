# Smart Parking Management System (SPMS)

## Overview
Smart Parking Management System (SPMS) is a cloud-native, microservice-based platform designed to solve real-world parking issues in urban environments. It enables users to search, reserve, and pay for parking spaces in real-time while allowing space owners to manage availability efficiently.

### Features
- Real-time space availability and reservation
- User and vehicle registration
- Simulated vehicle entry and exit
- Internal payment system with digital receipts
- Centralized configuration and dynamic service discovery

---

## Microservices Architecture

| Microservice               | Port  | Responsibilities                                          |
|---------------------------|-------|-----------------------------------------------------------|
| API Gateway               | 8080  | Routes client requests to respective services             |
| Config Server             | 8888  | Centralized configuration management                      |
| Eureka Server             | 8761  | Service registry and discovery                            |
| Parking Space Service     | 8083  | Manage parking spots and availability                     |
| Payment Service           | 8084  | Simulate payments and generate receipts                   |
| User Service              | 8081  | User registration and profile management                  |
| Vehicle Service           | 8082  | Register and manage vehicles                              |

---

## Technologies Used

- **Spring Boot** - Core framework for microservices
- **Spring Cloud Config** - Centralized configuration
- **Spring Cloud Eureka** - Service registry
- **Spring Cloud Gateway** - API Gateway
- **Postman** - API testing and validation
- Optional: Python Flask / Node.js Express (Not used in this version)

---

## Resources

- 📬 **Postman Collection**: You can import the API requests into Postman using the following file:
  [Smart Parking Management System - SPMS.postman_collection.json](./jsonFile/smart%20parking%20Management%20system-spms.postman_collection.json)

- ![Eureka Dashboard](./docs/screenshots/img.png)

---

## How to Run

1. **Start Eureka Server (`8761`)**
2. **Start Config Server (`8888`)**
3. **Start all microservices (User, Vehicle, Parking, Payment)**
4. **Start API Gateway (`8080`)**
5. Use Postman collection to test the endpoints.

---

## Folder Structure

smart-parking-management-system/
├── README.md
├── postman_collection.json
├── docs/
│   └── screenshots/
│       └── eureka_dashboard.png
├── eureka-server/
│   ├── src/
│   ├── pom.xml
│   └── application.yml
├── config-server/
│   ├── src/
│   ├── pom.xml
│   └── application.yml
├── api-gateway/
│   ├── src/
│   ├── pom.xml
│   └── application.yml
├── user-service/
│   ├── src/
│   ├── pom.xml
│   └── application.yml
├── vehicle-service/
│   ├── src/
│   ├── pom.xml
│   └── application.yml
├── parking-space-service/
│   ├── src/
│   ├── pom.xml
│   └── application.yml
├── payment-service/
│   ├── src/
│   ├── pom.xml
│   └── application.yml
└── licene
