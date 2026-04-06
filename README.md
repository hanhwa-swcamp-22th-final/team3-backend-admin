# Team3-Backend-Admin

This project is a backend administration system developed using Java and Spring Boot. It employs a CQRS (Command Query Responsibility Segregation) architecture to manage complex business logic and efficient data retrieval.

## Project Structure

The project is structured into two main layers based on the CQRS pattern:

### 1. Command Side (`src/main/java/.../admin/command`)
Responsible for handling state-changing operations (Create, Update, Delete).
- **Application Layer**: REST controllers and services that coordinate domain logic.
- **Domain Layer**: Contains domain entities (Aggregates), repository interfaces, and domain services.
- **Infrastructure Layer**: Implementation of repositories using Spring Data JPA.

### 2. Query Side (`src/main/java/.../admin/query`)
Responsible for efficient data retrieval (Read).
- **Controller Layer**: REST controllers specifically for data fetching.
- **Service Layer**: Services that handle query logic and DTO mapping.
- **Mapper Layer**: Uses MyBatis for complex SQL queries and mapping results to DTOs.

## Key Modules and APIs

The system is organized around several core business domains:

### Authentication & User Management (`/api/v1/auth`)
- User login, logout, and JWT token refresh.
- Profile management (updating personal information, password changes).

### Organization Management (`/api/v1/organization`)
- **Departments**: Management of organizational units and hierarchy.
- **Employees**: Employee registration, skill assessment, role management, and soft deletion.

### Equipment & Production Line Management
- **Equipment (`/api/v1/equipment-management/equipments`)**: CRUD operations for industrial equipment.
- **Factory Lines & Processes (`/api/v1/equipment-management/factory-lines`)**: Managing production line structures and processes.
- **Maintenance (`/api/v1/equipment-management/maintenance-logs`)**: Tracking maintenance logs and result records.
- **Aging & Baseline**: Management of equipment aging parameters and performance baselines.

### Configuration & Standards
- **Environment (`/api/v1/equipment-management/environment-standards`)**: Managing environmental standards and event tracking.
- **Domain Keywords (`/api/v1/domain-keyword`)**: Management of domain-specific keywords.
- **Industry Presets (`/api/v1/industry/preset`)**: Pre-configured settings for different industry types.

## Detailed API Endpoints

### 1. Authentication (`/api/v1/auth`)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/login` | User login and receive JWT tokens |
| POST | `/refresh` | Refresh access token using refresh token |
| POST | `/logout` | Invalidate current session and tokens |
| PUT | `/profile` | Update current user's profile information |
| PUT | `/password` | Change user password |

### 2. Organization Management (`/api/v1/organization`)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/department` | Create a new organizational department |
| PUT | `/department` | Update department details |
| DELETE | `/department/{id}` | Soft delete a department |
| GET | `/department/{id}` | Get specific department details |
| GET | `/departments` | List all departments |
| POST | `/employee` | Register a new employee |
| GET | `/employee/{code}` | Get specific employee details |
| GET | `/employees` | List all employees |
| PUT | `/employee/skill` | Update employee skill scores |
| PUT | `/employee/role` | Change employee role with history |
| PUT | `/employee/department` | Reassign employee to a department (HRM only) |
| DELETE | `/employee/{code}` | Soft delete an employee (change status) |

### 3. Equipment & Maintenance (`/api/v1/equipment-management`)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/equipments` | Create new equipment |
| GET | `/equipments` | List equipment with filters |
| POST | `/factory-lines` | Create a new factory line |
| POST | `/equipment-processes` | Create a new equipment process |
| POST | `/maintenance-item-standards`| Create maintenance standards |
| POST | `/maintenance-logs` | Record a new maintenance activity |
| GET | `/equipment-aging-params` | Query equipment aging history |
| GET | `/equipment-baselines` | Query equipment performance baselines |

### 4. Configuration & Standards
| Method | Path | Description |
|--------|------|-------------|
| POST | `/domain-keyword` | Create a new domain keyword |
| GET | `/domain-keyword` | List domain keywords by category |
| POST | `/industry/preset` | Create industry-specific presets |
| POST | `/environment-standards` | Create environmental safety standards |
| POST | `/environment-events` | Record environmental deviation events |

## Tech Stack
- **Core**: Java 17+, Spring Boot
- **Security**: Spring Security, JWT (JSON Web Token)
- **Database Access**: Spring Data JPA (Command), MyBatis (Query)
- **Testing**: JUnit 5, Mockito
- **API Documentation/Testing**: IntelliJ HTTP Client (`.http` files)
- **Encryption**: AES encryption for sensitive employee data
