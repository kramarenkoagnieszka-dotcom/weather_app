# Weather Temperature Service (AWS Lambda)

A professional, serverless AWS Lambda function built with **Java 21** that fetches real-time weather data for any city and classifies temperatures into human-readable categories.

## 🚀 Public Access
The function is exposed via a Public Lambda Function URL.
- **GET Parameter:** `city`
- **Public URL:** [https://fa33f43dwsqrubjmifgm4k22u40gzowd.lambda-url.eu-north-1.on.aws/](https://fa33f43dwsqrubjmifgm4k22u40gzowd.lambda-url.eu-north-1.on.aws/?city=Wroclaw)

---

## 🛠 Key Design Decisions

### 🏗 Architecture & Clean Code
* **Decoupled Orchestration:** Uses a strict Separation of Concerns where the Lambda handler orchestrates, services manage business flow, and clients handle infrastructure.
* **Centralized Configuration:** All technical constants are moved to a dedicated `AppConfig` class. This ensures that the parameters can be adjusted in one place without touching the business code.
* **Manual DI & Performance:** A custom `ApplicationContext` manages singletons (`HttpClient`, `ObjectMapper`), optimizing Lambda cold-starts and component lifecycles without framework overhead.
* **Strong Type Safety:** Eliminated "magic strings" and numbers using Java **Enums** and named constants, ensuring runtime stability and self-documenting code.

### 🔌 Integration & Resilience
* **API Agnosticism:** Core logic is decoupled from providers via `GeocodingClient` and `TemperatureClient` interfaces, allowing provider swaps (e.g., to AccuWeather) with zero impact on business logic.
* **DTO Encapsulation:** External API schemas are strictly isolated; raw JSON responses are mapped immediately to internal Domain Models to protect the core from external changes.
* **Resilient Communication:** Implementation of `HttpClientWrapper` with custom **Retry mechanisms**, read timeouts, and `Locale.US` formatting for cross-region coordinate consistency.
* **Boilerplate Efficiency:** Leverages **Lombok** to minimize verbosity, keeping the focus on architectural patterns and logic rather than POJO ceremonies.

---

## 🏗 Project Structure
- `app`: Lambda Handler and central ApplicationContext and configuration.
- `client`: Infrastructure layer handling HTTP communication and URL encoding.
- `service`: Business logic (orchestration and temperature classification).
- `model`: Domain objects, DTOs, and Enums.
- `exception`: Custom exception hierarchy for precise error mapping.

---

## 🧰 Tools & Libraries

- **Lombok:** Used to reduce boilerplate code (Getters, Builders, Constructors), keeping the data models clean and readable.
- **Jackson:** Utilized for JSON serialization and deserialization, integrated with Java Enums.
- **Checkstyle** ensures consistent code formatting and style.
- **SpotBugs** scans the bytecode to catch potential bugs and vulnerabilities before deployment.

---

## 🧪 Testing & Validation

### Unit Testing Strategy
The architecture allows for full test coverage without real API calls:
1. **Mocking Infrastructure:** Using Mockito to mock `GeocodingClient` and `TemperatureClient`.
2. **Logic Isolation:** `TemperatureClassifier` is tested as a pure function to verify boundary cases (e.g., exactly 0°C, 10°C, 30°C).
3. **Resilience Testing:** Simulating timeouts and 500 errors to verify retry logic.

### Example Requests and Responses

Below are real examples of calls to the Public Lambda URL and the structured JSON responses returned by the service:

| Location     | Example URL | Sample JSON Response | Category |
|:-------------| :--- | :--- | :--- |
| **Wroclaw**  | [Link](https://fa33f43dwsqrubjmifgm4k22u40gzowd.lambda-url.eu-north-1.on.aws/?city=Wroclaw) | `{"unit":"CELSIUS","city":"Wroclaw","temperature":23.7,"category":"WARM"}` | **Warm** |
| **Tokyo**    | [Link](https://fa33f43dwsqrubjmifgm4k22u40gzowd.lambda-url.eu-north-1.on.aws/?city=Tokyo) | `{"unit":"CELSIUS","city":"Tokyo","temperature":19.7,"category":"MILD"}` | **Mild** |
| **Nuuk**     | [Link](https://fa33f43dwsqrubjmifgm4k22u40gzowd.lambda-url.eu-north-1.on.aws/?city=Nuuk) | `{"unit":"CELSIUS","city":"Nuuk","temperature":0.8,"category":"COLD"}` | **Cold** |
| **Kugaaruk** | [Link](https://fa33f43dwsqrubjmifgm4k22u40gzowd.lambda-url.eu-north-1.on.aws/?city=kugaaruk) | `{"unit":"CELSIUS","city":"Kugaaruk","temperature":-10.7,"category":"FREEZING"}` | **Freezing** |
| **Dubai**    | [Link](https://fa33f43dwsqrubjmifgm4k22u40gzowd.lambda-url.eu-north-1.on.aws/?city=Dubai) | `{"unit":"CELSIUS","city":"Dubai","temperature":41.3,"category":"HOT"}` | **Hot** |
---

## 🧠 Design Reflection (Task 4)

### Future-Proofing
The current design heavily supports the addition of new weather providers through the **Strategy/Adapter pattern**. Adding a new provider only requires a new implementation of the `TemperatureClient` interface. Switching between them is a single-line change in the `ApplicationContext`.

### Potential Improvements

If I had more time to further develop this project, I would focus on the following enhancements:

* **Provider Factory Implementation:** While the system uses interfaces, the specific client is currently hardcoded in the configuration. Implementing a **Factory Pattern** would allow dynamic selection of weather providers based on input parameters (e.g., `?city=London&provider=accuweather`).
* **Enhanced Geocoding Precision:** Currently, the app picks the first result returned by the API (e.g., the most popular "Berlin"). A production-ready version should support additional parameters like country codes or states to handle cases where multiple cities share the same name across different regions.
* **User-Centric Error Handling:** At the moment, detailed errors are logged in AWS CloudWatch for developers, but the end-user receives a generic error message. I would implement a global error handler to map exceptions into clean, user-friendly JSON responses.
* **Full Test Coverage:** The architecture is fully prepared for testing (Separation of Concerns), but I would aim for 100% coverage, specifically including integration tests for complex network scenarios and edge cases.
* **Integration & Security:** Currently, the Lambda is exposed via a public URL for simplicity. A production-grade evolution would involve integrating it with an existing ecosystem or a dedicated frontend, while securing the endpoint (e.g., via API Gateway with proper IAM/Cognito authorization) to observe its performance and behavior in a real-world environment.

---

## 📦 Building the project (maven wrapper included)
```bash
./mvnw clean package
