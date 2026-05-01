# Weather Classification Lambda

A professional, serverless AWS Lambda function built with Java 21 that fetches real-time weather data and classifies temperatures into human-readable categories.

## 🚀 Solution Description
The application retrieves current weather data from the **Open-Meteo API** and applies a business logic layer to categorize the temperature. The project is designed with high scalability and maintainability in mind, following SOLID principles.

## 🛠 Key Design Decisions
- **Separation of Concerns:** Each component has a single, well-defined responsibility. The Lambda handler is limited to orchestration, the service manages the business flow, and the client handles infrastructure-specific communication.
- **Hybrid Packaging (Layer & Feature-based):** The project structure uses a deliberate mix of layer-based packaging (e.g., `client`, `model`) and feature-grouping (`openmeteo`) This ensures high navigability, logical grouping of related components, and a clean project overview.
- **Encapsulation of Provider DTOs:** External API structures are strictly isolated within the client layer. The raw Open-Meteo response models are mapped immediately to internal domain models, protecting the business logic from potential changes in the external API's JSON schema.
- **Provider Abstraction & API Agnosticism:** The core application was decoupled from the specific weather provider using the `WeatherClient` interface. This allows for switching to a different provider without touching business logic and enables easy **Mocking** for unit tests.
- **Manual Dependency Injection (ApplicationContext):** To optimize performance and manage component lifecycles, `ApplicationContext` was implemented. This ensures that heavy-duty objects like `HttpClient` and `ObjectMapper` are singletons and increases code maintainability.
- **Strong Type Safety:** Instead of using Strings for data, Java **Enums** for Cities and Temperature Categories were used. This eliminates runtime errors caused by typos, ensures consistency across the entire system and increases code readability.
- **Zero Magic Numbers & Strings:** All business-critical thresholds (temperature ranges) and technical configurations (timeouts, default units) are extracted into named constants (`private static final`), making the code self-documenting and maintainable.
- **Boilerplate Reduction with Lombok:** Lombok annotations (`@Data`, `@Getter`, `@RequiredArgsConstructor`) were used to eliminate verbose boilerplate code. This keeps the codebase clean and allows the reviewer to focus on the actual architecture and logic.

## 🧰 Tools & Libraries
- **Lombok:** Used to reduce boilerplate code (Getters, Builders, Constructors), keeping the data models clean and readable.
- **Jackson:** Utilized for high-performance JSON serialization and deserialization, integrated with Java Enums.
- **Static Analysis (SpotBugs & Checkstyle):** The project is configured to follow strict coding standards.
    - **Checkstyle** ensures consistent code formatting and style.
    - **SpotBugs** scans the bytecode to catch potential bugs and vulnerabilities before deployment.

## 🧪 Unit Testing Strategy
To test the solution without calling the real API, we use a **Mocking** strategy:
1.  **Mocking the Client:** By mocking the `WeatherClient` interface, we can simulate various weather conditions.
2.  **Logic Verification:** We inject the mock into `WeatherService` to verify if it correctly orchestrates the classification and returns the expected `WeatherResponse`.
3.  **Isolated Classifier Tests:** The `TemperatureClassifier` is tested as a pure function, ensuring 100% coverage of all temperature ranges defined in the requirements.

## 📦 Building the project
To compile, check code quality, and generate the deployable JAR:
```bash
mvn clean package
