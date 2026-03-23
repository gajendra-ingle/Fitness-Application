<div align="center">

# 💪 FitnessAI

### AI-Powered Fitness & Nutrition Tracker

A full-stack application that delivers personalised workout plans, tracks nutrition, monitors body progress, and uses **Spring AI + GPT-4o** to generate intelligent fitness coaching — all backed by a production-grade Spring Boot REST API and a modern React frontend.

<br/>

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring AI](https://img.shields.io/badge/Spring_AI-1.0.0--M6-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![React](https://img.shields.io/badge/React-18.2-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-purple?style=for-the-badge)


</div>

---

## ✨ Features

| Module | Description |
|---|---|
| 🔐 **Authentication** | JWT-based register / login with BCrypt password hashing |
| 👤 **User Profile** | Age, height, weight, gender, fitness goal & activity level |
| 🏋️ **Workout Tracker** | Log sessions with type, duration, calories, and exercise details |
| 🥗 **Nutrition Logger** | Track meals with full macro breakdown (protein / carbs / fats) |
| 📈 **Progress Tracker** | Log weight entries, auto-calculate BMI, view historical trends |
| 🤖 **AI Coach** | GPT-4o generates personalised 7-day workout plans and daily meal plans via Spring AI |
| 📋 **Exercise Library** | Searchable and filterable exercise database seeded at startup |
| 📖 **Swagger UI** | Full interactive API documentation at `/swagger-ui.html` |

---

## 🛠 Tech Stack

### Backend

| Layer | Technology |
|---|---|
| Framework | Spring Boot 3.2 · Java 17 |
| AI Integration | Spring AI 1.0.0-M6 · OpenAI GPT-4o |
| Security | Spring Security · JJWT 0.12.3 (JWT) · BCrypt |
| Persistence | Spring Data JPA · Hibernate · MySQL 8 |
| Validation | Jakarta Bean Validation |
| Documentation | SpringDoc OpenAPI 2.3 (Swagger UI) |
| Utilities | Lombok · MapStruct |
| Build | Maven 3.9 |

### Frontend

| Layer | Technology |
|---|---|
| Framework | React 18.2 (Create React App) |
| Styling | CSS-in-JS with centralised design token system |
| Charts | Pure SVG (no external chart library) |
| Fonts | Syne (display) · DM Sans (body) via Google Fonts |
| State | React `useState` / `useReducer` hooks |

---

## 🚀 Getting Started

### Prerequisites

| Tool | Version |
|---|---|
| Java | 17+ |
| Maven | 3.9+ |
| Node.js | 18+ |
| npm | 9+ |
| MySQL | 8.0+ |
| OpenAI API Key | [Get one here](https://platform.openai.com/api-keys) |

---

### 1 · Clone the repository

```bash
git clone https://github.com/gajendra-ingle/Fitness-Application.git
cd fitnessai
```

---

### 2 · Database setup

```sql
CREATE DATABASE fitness_ai_db;
```

Hibernate's `ddl-auto: update` will create all tables automatically on first run. To seed the exercise library, run:

```bash
mysql -u root -p fitness_ai_db < backend/database/schema.sql
```

---

### 3 · Backend setup

```bash
cd backend

# 1. Copy and fill in the environment file
cp .env.example .env
#    → Set DB_PASSWORD, SPRING_AI_OPENAI_API_KEY, JWT_SECRET

# 2. Build and run
mvn clean install -DskipTests
mvn spring-boot:run
```

> API starts at **http://localhost:8080**
> Swagger UI: **http://localhost:8080/swagger-ui.html**

---

### 4 · Frontend setup

```bash
cd frontend

# 1. Copy and fill in the environment file
cp .env.example .env
#    → REACT_APP_API_BASE_URL is already set to http://localhost:8080/api

# 2. Install and start
npm install
npm start
```

> Opens at **http://localhost:3000**

---

## 📁 Project Structure

```
fitnessai/
├── backend/                                        Spring Boot API
│   ├── database/
│   │   └── schema.sql                              DDL + seed data
│   ├── .env.example                                Environment variable template
│   └── src/main/
│       ├── resources/
│       │   └── application.yml                     Config (reads from .env)
│       └── java/com/fitnessai/
│           ├── FitnessAiApplication.java
│           ├── ai/
│           │   ├── AiFitnessRecommendationService.java   GPT-4o via Spring AI
│           │   ├── WorkoutPlanResponse.java
│           │   └── DietPlanResponse.java
│           ├── config/
│           │   ├── ChatClientConfig.java            Spring AI ChatClient bean
│           │   └── OpenApiConfig.java               Swagger JWT config
│           ├── controller/
│           │   ├── AuthController.java
│           │   ├── ProfileController.java
│           │   ├── WorkoutController.java
│           │   ├── NutritionController.java
│           │   ├── ProgressController.java
│           │   └── AiController.java
│           ├── dto/
│           │   ├── request/                         RegisterRequest, LoginRequest,
│           │   │                                    ProfileRequest, WorkoutRequest,
│           │   │                                    NutritionLogRequest, ProgressRequest
│           │   └── response/                        AuthResponse, ProfileResponse,
│           │                                        WorkoutResponse, NutritionResponse,
│           │                                        ProgressResponse
│           ├── entity/
│           │   ├── User.java
│           │   ├── UserProfile.java                 FitnessGoal, ActivityLevel, Gender enums
│           │   ├── Workout.java                     WorkoutType enum
│           │   ├── WorkoutExercise.java
│           │   ├── Exercise.java                    Difficulty enum
│           │   ├── NutritionLog.java                MealType enum
│           │   └── Progress.java
│           ├── exception/
│           │   ├── ResourceNotFoundException.java
│           │   ├── BadRequestException.java
│           │   └── GlobalExceptionHandler.java
│           ├── repository/
│           │   ├── UserRepository.java
│           │   ├── UserProfileRepository.java
│           │   ├── WorkoutRepository.java
│           │   ├── ExerciseRepository.java
│           │   ├── NutritionLogRepository.java
│           │   └── ProgressRepository.java
│           ├── security/
│           │   ├── JwtUtils.java
│           │   ├── JwtAuthFilter.java
│           │   ├── UserDetailsServiceImpl.java
│           │   └── SecurityConfig.java
│           └── service/
│               ├── AuthService.java (+ impl)
│               ├── ProfileService.java (+ impl)
│               ├── WorkoutService.java (+ impl)
│               ├── NutritionService.java (+ impl)
│               └── ProgressService.java (+ impl)
│
└── frontend/                                       React SPA
    ├── .env.example                                Environment variable template
    ├── public/
    │   └── index.html
    └── src/
        ├── App.js                                  Root — router + layout
        ├── index.js
        ├── styles/
        │   └── globals.css                         Reset, animations, scrollbar
        ├── constants/
        │   ├── theme.js                            Design tokens (colors, fonts, radius…)
        │   └── navigation.js                       Nav config & page metadata
        ├── utils/
        │   └── formatters.js                       Pure helper functions (BMI, duration…)
        ├── hooks/
        │   ├── useLocalStorage.js
        │   └── useToggle.js
        ├── components/
        │   ├── Sidebar/                            Collapsible nav + streak badge
        │   ├── Topbar/                             Fixed header, search, notifications
        │   ├── charts/
        │   │   ├── SparkLine/                      SVG sparkline
        │   │   ├── DonutChart/                     SVG donut
        │   │   ├── BarChart/                       SVG grouped bar chart
        │   │   └── MacroBar/                       Horizontal macro progress bar
        │   └── ui/
        │       ├── StatCard/                       KPI metric card
        │       ├── Badge/                          Pill label
        │       ├── Button/                         primary / secondary / ghost / danger
        │       ├── FormField/                      input / select / textarea wrapper
        │       ├── Modal/                          Accessible overlay (ESC + backdrop)
        │       └── PageCard/                       Section card wrapper
        └── pages/
            ├── Dashboard/     (components/ + data/)
            ├── Workouts/      (components/ + data/)
            ├── Nutrition/     (components/ + data/)
            ├── Progress/      (components/ + data/)
            ├── AICoach/       (components/ + data/)
            └── Exercises/     (components/ + data/)
```

---

## 🌐 API Reference

All endpoints except `/api/auth/**` require the header:
```
Authorization: Bearer <your_jwt_token>
```

### Auth

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/register` | Register a new user |
| `POST` | `/api/auth/login` | Login → returns JWT token |

### Profile

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/profile` | Get current user's profile |
| `PUT` | `/api/profile/update` | Update profile (goal, weight, height…) |

### Workouts

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/workouts/user/{userId}` | Get all workouts for a user |
| `GET` | `/api/workouts/{id}` | Get a single workout |
| `POST` | `/api/workouts` | Log a new workout |
| `PUT` | `/api/workouts/{id}` | Update a workout |
| `DELETE` | `/api/workouts/{id}` | Delete a workout |

### Nutrition

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/nutrition/log` | Log a food entry |
| `GET` | `/api/nutrition/history` | All nutrition entries |
| `GET` | `/api/nutrition/today` | Today's entries |
| `DELETE` | `/api/nutrition/{id}` | Delete a log entry |

### Progress

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/progress/log` | Log weight / body metrics (BMI auto-calculated) |
| `GET` | `/api/progress/history` | All progress entries |

### AI Coach

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/ai/my-workout-plan` | GPT-4o personalised 7-day workout plan |
| `GET` | `/api/ai/my-diet-plan` | GPT-4o personalised daily meal plan |

> 📖 Full interactive docs with request/response schemas at **`http://localhost:8080/swagger-ui.html`**



