# University Assistant App

A Spring Boot application with a relational database design for managing academic entities, departments, and course enrollments, featuring **OpenAPI** and an **LLM-driven** interaction model where API endpoints serve as executable tool for AI agent. 

## 🏗 Architecture Overview

### Core Components
*   **Controller Layer**: Exposed via a RESTful interface and fully documented with SpringDoc OpenAPI (Swagger). This layer serves as the primary entry point for both human users and LLM agents, providing a structured schema for interaction.
*   **Service Layer (Business Logic)**: Orchestrates the application's core logic, managing the data flow between the AI module and the persistence layer. It ensures data consistency and applies domain-specific rules before returning results.
*   **Repository Layer**: Leveraging Spring Data JPA with a focus on High-Performance Native Queries.
    * Utilizes Window Functions and CTEs (Common Table Expressions) to perform complex analytical ranking and Top-N logic directly at the database level.
    * Employs Interface-based Projections to minimize memory footprint and optimize data transfer.

*   **AI Orchestration**: Integration with LLM provider (via Spring AI) utilizing **Function Calling**.

### Database Design & Architecture

The relational schema is designed in **3rd Normal Form (3NF)** to ensure data integrity, eliminate redundancy, and maintain clear logical boundaries between entities.

#### 🧩 Entity Relationships & Business Rules

*   **Lecturers & Degrees (1:N):** Every lecturer is assigned exactly one academic degree (e.g., Assistant, Associate Professor, Professor) via the `degree_id` foreign key.
*   **Lecturers & Departments (M:N):** To support the requirement that a lecturer can work in multiple departments, a join table `lecturer_departments` is used.
*   **Students & Departments (1:N):** Each student belongs to exactly one department, enforced by the `department_id` in the `students` table.
*   **Courses & Lecturers (M:N):** Courses can be co-taught by multiple lecturers. This is handled by the `course_lecturers` link table.
*   **Enrollments (M:N):** Students register for courses through the `enrollments` table, which also captures the student's `grade` for the specific course.
*   **Department Leadership:** The `departments` table includes a `head_id` pointing to the `lecturers` table to identify the head of the department.

#### 🛡️ Constraints and Optimization

*   **Data Integrity:** All foreign keys are explicitly defined with `NOT NULL` constraints where mandatory.
*   **Uniqueness:** Unique constraints are applied to `degrees.title` and `departments.name` to prevent duplicate catalog entries.
*   **Performance:** Primary keys use `BIGINT` for scalability, and foreign key columns are indexed to optimize complex `JOIN` queries.
*   **Migrations:** Database schema evolution is managed via **Liquibase (YAML)**, ensuring version control and reproducible environments across different stages.

## 🚀 Database Migrations

The project uses **Liquibase** for version control of the database schema. The migration files are located in:
`src/main/resources/db/changelog/`

To run the migrations locally, simply start the Spring Boot application, or use the following command:
```bash
mvn liquibase:update
```

## 🤖 LLM Approach

The application utilizes **Function Calling (Tool Use)** to bridge the gap between natural language user intents and DB operations. Instead of static logic, the LLM acts as an orchestrator that dynamically selects the appropriate repository-backed tools.

### 1. Prompt Engineering Strategy

*   **System Persona**: The LLM is configured as an "University Assistant." The system prompt provides the model with architectural context, ensuring it understands the relationship between Students, Lecturers, Departments etc.
*   **Data Grounding**: The model is instructed to only provide information retrieved through tool execution, reducing "hallucinations" probability regarding student records, grades etc.

### 2. Tools & Function Calling (Repository-Backed)

Each "Tool" available to the LLM is directly powered by specialized **Native SQL** logic in the Repository layer, allowing the model to perform complex data analysis:


| Tool Capability | Implementation Logic | AI Use Case |
| :--- | :--- | :--- |
| **Global Discovery** | `searchPeople` | Handles fuzzy name searches for student and lecturer in a single intent. |
| **Performance Ranking** | `getTopStudents` (Window Functions) | Solves complex analytical prompts like "Who are the top performers in X?" |
| **Academic Records** | `getStudentGrades` (Projections) | Fetches grade history when a specific student ID is identified. |
| **Statistical Analysis** | `getDepartmentStats` | Provides quick organizational stats for department.  |
| **Course records** | `getCoursesByProfessor` | Provides info for all courses by professor name.  |
| **Global Analysis** | `getAverageSalariesByDepartments` | Provides stats for all departments and avg salaries.  |

### 3. Execution Lifecycle

1.  **Intent Extraction**: User asks: *"Get top 3 students in Logic & Computation"*
2.  **Parameter Mapping**: LLM identifies the need for `getTopStudents` and extracts `courseTitle="Logic & Computation"` and `limit=3`.
3.  **Native Execution**: The **Repository Layer** executes a CTE with a Window Function to calculate the rank at the database level.
4.  **Service Logic**: The Service calls the Repository, receives a `StudentProjection`, and converts it into a `StudentDTO`.
5.  **LLM Synthesis**: The model receives the clean DTO and generates a conversational response based on the structured data.

## 🚀 Getting Started

You can run the entire ecosystem using Docker (recommended) or set up the components manually for development.

### Option 1: Running with Docker (Quick Start)
This is the easiest way to spin up the App, PostgreSQL, and Ollama with a single command.

1.  **Clone the repository**:
    ```bash
    git clone <your-repo-url>
    cd <project-folder>
    ```
2.  **Launch the stack**:
    ```bash
    docker-compose up --build
    ```
3.  **Wait for initialization**: 
    *   The PostgreSQL container will run healthchecks.
    *   The Ollama container will automatically download the `llama3.2:3b` model (this may take a few minutes depending on your internet speed).
4.  **Access the App**:
    *   **API**: `http://localhost:8080/api/ask`
    *   **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`

---

### Option 2: Local Development Setup
If you want to run the Spring Boot application locally (e.g., in your IDE) while keeping services in Docker.

1.  **Start Infrastructure only**:
    ```bash
    docker-compose up postgres ollama
    ```
2.  **Configure Environment**:
    Ensure your `src/main/resources/application.properties` (or environment variables) match the local ports:
    *   `spring.datasource.url=jdbc:postgresql://localhost:5432/university_db`
    *   `spring.ai.ollama.base-url=http://localhost:11434`
3.  **Run the Application**:
    ```bash
    ./mvnw spring-boot:run
    ```

---

### 🛠 Prerequisites
*   **Docker & Docker Compose** installed.
*   **Java 21** (if running locally).
*   Minimum **8GB RAM** allocated to Docker (check Docker Desktop settings / colima / Orbstack).

### ⚠️ Performance Considerations

Running the entire stack (Database, Java App, and LLM) via Docker Compose introduces specific overhead that affects system response times:

*   **Virtualization Overhead**: Running on non-native environments (especially Windows or macOS via Docker Desktop) adds a translation layer for I/O and CPU instructions. This can increase latency during the execution of **Native SQL** queries and heavy JVM operations.
*   **LLM Inference Latency**: By default, **Ollama** runs within a container without direct GPU passthrough. Consequently, all model reasoning is performed on the **CPU**, which significantly slows down token generation compared to native or GPU-accelerated environments.
*   **Network Bridging**: Communication between the Spring Boot application and Ollama occurs over the Docker internal virtual bridge. While efficient, it adds overhead to the request/response cycle during **Function Calling** and tool orchestration.
*   **Resource Contention**: The JVM, PostgreSQL, and the Ollama engine compete for the same host RAM and CPU cycles. 

    > **Note**: For stable performance, it is recommended to allocate at least **8GB of RAM** to the Docker engine.