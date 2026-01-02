# 🎓 Proctovision – Intelligent Online Exam Proctoring System

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Backend-brightgreen)
![MySQL](https://img.shields.io/badge/Database-MySQL-orange)
![Status](https://img.shields.io/badge/Project-Final%20Year-success)

Proctovision is a **full-stack web-based online examination and proctoring system** designed to conduct **secure, monitored, and controlled online exams**.  
The system focuses on **exam integrity**, **real-time activity monitoring**, and **role-based management**, closely simulating real-world examination environments.

This project was developed as a **Final Year MCA Project**, emphasizing **clean architecture, backend security, and scalability**.

---

## 🎯 Project Objective

The primary objective of Proctovision is to:
- Conduct secure online examinations
- Monitor candidate activities during exams
- Reduce malpractice in remote examinations
- Provide centralized control to administrators and examiners

---

## 🏗️ System Architecture

The application follows a **layered architecture**, ensuring maintainability and scalability.

Client (Browser)
↓
Controller Layer (Spring MVC)
↓
Service Layer (Business Logic)
↓
Repository Layer (JPA / Hibernate)
↓
MySQL Database


✔ Separation of concerns  
✔ Easy debugging & testing  
✔ Industry-standard backend design  

---

## 👥 User Roles

### 🔹 Admin
- Manage users and roles
- Create and control exams
- Monitor overall system activity

### 🔹 Examiner
- Monitor live examinations
- Review candidate activities
- Identify suspicious behavior

### 🔹 Student
- Secure authentication
- Attend online exams
- Follow controlled exam rules

---

## 🧠 Key Features

- Secure login & authentication
- Role-Based Access Control (RBAC)
- Online exam creation & participation
- Real-time exam monitoring
- Activity logging & behavior tracking
- Examiner dashboard
- Scalable backend architecture

---

## 🧪 Real-World Scenarios Covered

- Prevents multiple concurrent login sessions
- Detects abnormal exam behavior
- Tracks session and activity events
- Logs suspicious actions for review
- Ensures secure and controlled exam flow

---

## 🛠️ Tech Stack

### Backend
- Java 17
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate
- Maven

### Frontend
- HTML5
- CSS3
- JavaScript

### Database
- MySQL

### Tools & Platforms
- Git & GitHub
- Git LFS (for large media files)
- Maven Wrapper

---

## 📸 Application Screenshots
> Screenshots are stored inside the `screenshots/` directory

### 🏠 Home Page
![Home Page]( screenshots/Screenshot%202025-10-14%20131531.png)

### 🔐 Login Page
![Login Page](screenshots/Screenshot%202025-10-14%20135813.png) 

### 🔐 Login Page 2
![Login Page](screenshots/Screenshot%202025-10-14%20134617.png) 

### 📊 Admin Dashboard
![Admin Dashboard](screenshots/Screenshot%202025-10-03%20115032.png)

### 👁️ Exam Monitoring Page
![Exam Monitoring](screenshots/Screenshot%202025-10-14%20135954.png)

### 🧪 Test Cases & Validation
![Test Cases](screenshots/Screenshot%202025-10-14%20141106.png)

### 🔹 Extra Screenshots
![Extra Screenshot 1](screenshots/Screenshot%202025-10-15%20092310.png)
![Extra Screenshot 2](screenshots/Screenshot%202025-10-15%20092703.png)
![Extra Screenshot 3](screenshots/Screenshot%202025-10-15%20100852.png)
![Extra Screenshot 4](screenshots/Screenshot%202025-10-15%20104244.png)
![Extra Screenshot 5](screenshots/Screenshot%202025-10-15%20111122.png)
![Extra Screenshot 6](screenshots/Screenshot%202025-10-29%20122043.png)


## 🧪 Testing & Validation

The system was tested to ensure correctness, security, and reliability.

### ✔ Testing Types
- Unit Testing
- Integration Testing
- Functional Testing
- Manual UI Testing

### ✔ Tools Used
- JUnit
- Spring Boot Test Framework

All critical workflows such as authentication, exam flow, and activity logging were validated successfully.

---

## ⚙️ How to Run Locally

### Prerequisites
- Java 17+
- Maven
- MySQL
- Git

### Steps

```bash
git clone https://github.com/vinayakpatil99/Proctovision.git
cd Proctovision
mvn spring-boot:run
