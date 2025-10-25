# Citation Service API

An API-based citation creation and management service that allows clients to build, maintain, and enrich libraries of sources while automatically generating formatted citations and bibliographies.

Authors:
- Alexa Kafka | ak5124
- Abdulmohsen Alghannam | afa2165
- Matthew Labasan | mjl2278
- Sungjun Lee | sl5778
- Usman Moazzam | uam2105

To view the original project proposal, click [here](https://docs.google.com/document/d/1V7UUUKATDx-as5N2krsXF6NSkbQoo0iBBf0t164SlHI/edit?usp=sharing).

## Current Features

- **Multi-Style Citation Generation**: Supports MLA, APA, and Chicago citation formats
- **Single Source Citations**: Generate citations for individual sources with style and backfill options
- **Group Citation Generation**: Generate citations for all sources in a submission group
- **Book Management**: Full CRUD operations for book records
- **Video Management**: Full CRUD operations for video records  
- **Article Management**: Full CRUD operations for article records
- **REST API**: Clean RESTful endpoints for all operations
- **Postgres Database**: Postgres database for development and testing using Google Cloud SQL

## Technology Stack

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Data JPA**
- **Postgres** Hosted on Google Cloud SQL
- **Maven** for dependency management

## Project Structure

```
src/
├── main/
│   ├── java/com/columbia/coms4156/citationservice/
│   │   ├── CitationServiceApplication.java         # Main application
│   │   ├── controller/                             # API Endpoints
│   │   │   ├── CitationController.java
│   │   │   └── SourceController.java
│   │   ├── model/                               
│   │   │   ├── Article.java
│   │   │   ├── Book.java
│   │   │   ├── Citation.java
│   │   │   ├── Source.java
│   │   │   ├── Submission.java
│   │   │   ├── User.java
│   │   │   └── Video.java
│   │   ├── repository/                             # Database Method Management
│   │   │   ├── ArticleRepository.java
│   │   │   ├── BookRepository.java
│   │   │   ├── CitationRepository.java
│   │   │   ├── SubmissionRepository.java
│   │   │   ├── UserRepository.java
│   │   │   └── VideoRepository.java
│   │   ├── service/                                # Backend Logic
│   │   │   ├── CitationService.java
│   │   │   └── SourceService.java
│   │   └── utils/
│   │       └──DatabaseStartupCheck.java           # Checks database connection
│   └── resources/
│       ├── application.properties
│       └── (local) application-dev.properties  # NOT committed — see DB section
└── test/
    └── java/com/columbia/coms4156/citationservice/
        └── CitationServiceApplicationTests.java
```

## Class and Database Design
See [here](https://www.canva.com/design/DAG2NLXV3-U/WCSwNCgI2ZkAA9SOC6vNbQ/edit) for design. Read up on the reasoning for why certain classes are there before creating the rest of the endpoints.

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd coms-4156-group-project
   ```

2. **Build the project**
   ```bash
   mvn clean compile
   ```

3. **Connect to the Database**
  - Add this file to your local repo from other devs. Do NOT commit it to version control:
      - `src/main/resources/application-dev.properties`
      - This file contains environment-specific credentials and connection settings used when running the app against an external (non-H2) database.
  - Navigate to this [Google Console CloudSQL](https://console.cloud.google.com/sql/instances/ase-project/overview?authuser=1&project=not-founders) page and turn on the server instance.
  - Before you can connect from your machine, add your public IP address to the Cloud SQL instance's authorized networks. If you do not add your IP, the instance will refuse connections. Use the Cloud Console networking page for the instance:
      - https://console.cloud.google.com/sql/instances/ase-project/connections/networking?authuser=1&project=not-founders

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - API Base URL: `http://localhost:8080`
   - Database: https://console.cloud.google.com/sql/instances/ase-project/studio?authuser=1&project=not-founders 
      - Access given upon request.

5. **Tear Down**
  - Once done with using the application, ensure you turn off the CloudSQL server on the [Google Console CloudSQL](https://console.cloud.google.com/sql/instances/ase-project/overview?authuser=1&project=not-founders) page.

## API Endpoints

### SourceController

| Method | Endpoint                   | Description          | Input                                                                               | Output                           |
|--------|----------------------------|----------------------|-------------------------------------------------------------------------------------|----------------------------------|
| POST   | `/api/source/book`         | Create a new Book    | Book JSON (title*, author*, publisher, publicationYear, city, edition)              | Book object with generated ID    |
| GET    | `/api/source/book`         | Get all Books        | None                                                                                | List of Book objects             |
| GET    | `/api/source/book/{id}`    | Get a Book by ID     | Path param: id (Long)                                                               | Book object or 404               |
| PUT    | `/api/source/book/{id}`    | Update a Book        | Path param: id (Long), Book JSON                                                    | Updated Book object or 404       |
| DELETE | `/api/source/book/{id}`    | Delete a Book        | Path param: id (Long)                                                               | 204 No Content or 404            |
| POST   | `/api/source/video`        | Create a new Video   | Video JSON (title*, author*, director, durationSeconds, platform, url, releaseYear) | Video object with generated ID   |
| GET    | `/api/source/video`        | Get all Videos       | None                                                                                | List of Video objects            |
| GET    | `/api/source/video/{id}`   | Get a Video by ID    | Path param: id (Long)                                                               | Video object or 404              |
| PUT    | `/api/source/video/{id}`   | Update a Video       | Path param: id (Long), Video JSON                                                   | Updated Video object or 404      |
| DELETE | `/api/source/video/{id}`   | Delete a Video       | Path param: id (Long)                                                               | 204 No Content or 404            |
| POST   | `/api/source/article`      | Create a new Article | Article JSON (title*, author*, journal, volume, issue, pages, doi, publicationYear) | Article object with generated ID |
| GET    | `/api/source/article`      | Get all Articles     | None                                                                                | List of Article objects          |
| GET    | `/api/source/article/{id}` | Get an Article by ID | Path param: id (Long)                                                               | Article object or 404            |
| PUT    | `/api/source/article/{id}` | Update an Article    | Path param: id (Long), Article JSON                                                 | Updated Article object or 404    |
| DELETE | `/api/source/article/{id}` | Delete an Article    | Path param: id (Long)                                                               | 204 No Content or 404            |

*Required fields

### CitationController
| Method | Endpoint                          | Description                                                           | Input                                                                                            | Output                     |
|--------|-----------------------------------|-----------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|----------------------------|
| GET    | `/api/cite/book/{id}`             | Generate MLA citation for a stored Book by ID                         | Path param: id (Long)                                                                            | Citation string or 404     |
| POST   | `/api/cite/book`                  | Generate MLA citation from provided Book JSON (no save)               | Book JSON + Query param: style (default: "MLA")                                                  | Citation string            |
| GET    | `/api/cite/video/{id}`            | Generate MLA citation for a stored Video by ID                        | Path param: id (Long)                                                                            | Citation string or 404     |
| POST   | `/api/cite/video/citation`        | Generate MLA citation from provided Video JSON (no save)              | Video JSON + Query param: style (default: "MLA")                                                 | Citation string            |
| GET    | `/api/cite/article/{id}/citation` | Generate MLA citation for a stored Article by ID                      | Path param: id (Long)                                                                            | Citation string or 404     |
| POST   | `/api/cite/article/citation`      | Generate MLA citation from provided Article JSON (no save)            | Article JSON + Query param: style (default: "MLA")                                               | Citation string            |
| GET    | `/api/cite/source/{sourceId}`     | Generate citation for a single source with style and backfill options | Path param: sourceId (Long), Query params: style (default: "MLA"), backfill (default: false)     | CitationResponse JSON      |
| GET    | `/api/cite/group/{submissionId}`  | Generate citations for all sources in a submission group              | Path param: submissionId (Long), Query params: style (default: "MLA"), backfill (default: false) | GroupCitationResponse JSON |


## API Usage Examples

### Creating a Book

**POST** `http://localhost:8080/api/books`

```json
{
  "title": "To Kill a Mockingbird",
  "author": "Harper Lee",
  "publisher": "J.B. Lippincott & Co.",
  "publicationYear": 1960,
  "city": "Philadelphia"
}
```

### Generating a Citation (Direct)

**POST** `http://localhost:8080/api/books/citation`

```json
{
  "title": "1984",
  "author": "George Orwell",
  "publisher": "Secker & Warburg",
  "publicationYear": 1949,
  "city": "London"
}
```

**Response:**
```
Orwell, George. _1984_. Secker & Warburg, 1949.
```

### Generating Citations (New Proposal Endpoints)

**GET** `http://localhost:8080/api/cite/source/{sourceId}?style=APA&backfill=false`

Generate a citation for a single source with specified style and backfill options.

**Parameters:**
- `sourceId` (path): The unique identifier of the source
- `style` (query, optional): Citation style - "MLA", "APA", or "Chicago" (default: "MLA")
- `backfill` (query, optional): Whether to include backfill information (default: false)

**Response:**
```json
{
  "CitationID": "123",
  "CitationString": "Orwell, G. (1949). 1984. Secker & Warburg, London."
}
```

**GET** `http://localhost:8080/api/cite/group/{submissionId}?style=Chicago&backfill=false`

Generate citations for all sources in a submission group.

**Parameters:**
- `submissionId` (path): The unique identifier of the submission group
- `style` (query, optional): Citation style - "MLA", "APA", or "Chicago" (default: "MLA")
- `backfill` (query, optional): Whether to include backfill information (default: false)

**Response:**
```json
{
  "submissionId": 456,
  "Citations": {
    "123": "Orwell, George. \"1984.\" London: Secker & Warburg, 1949.",
    "124": "Lee, Harper. \"To Kill a Mockingbird.\" Philadelphia: J.B. Lippincott & Co., 1960."
  }
}
```

### Book JSON Schema

```json
{
  "id": "number (auto-generated)",
  "title": "string (required)",
  "author": "string (required)",
  "publisher": "string (optional)",
  "publicationYear": "number (optional)",
  "city": "string (optional)",
  "edition": "string (optional)",
  "isbn": "string (optional)"
}
```

## Testing with Postman
These are API tests you can use to exercise the service endpoints (create, retrieve, update, delete sources and generate citations).

[Postman collection — API tests](https://web.postman.co/workspace/My-Workspace~944de483-7347-4047-89cb-e75c81e1ba7b/collection/32914220-d96cfb1c-3cc6-48fc-993f-46baea892753?action=share&source=copy-link&creator=32914220)

## Development Notes
- CORS is enabled for all origins (development setup)

### Project Management
For project management and task tracking, we used Notion. You can find the project board here: [T2: First Iteration Project Board](https://www.notion.so/27bb9d8608c380aaaeb7f3426296a032?v=27bb9d8608c381eab889000c5e024d6d&source=copy_link)

### Testing Framework
For unit testing, we utilized JUnit and Mockito. Integration tests can be added in future iterations. We also used Postman for API endpoint testing. Our test cases are located in the `src/test/java/com/columbia/coms4156/citationservice/src/test` directory.

For branch coverage analysis, we used JaCoCo. To generate a branch coverage report, run the following Maven command:

```bash
mvn jacoco:report
```

We have included a sample branch coverage report generated using JaCoCo, which can be found in the root directory as `t2-branch-coverage-report.html`. This report shows that we reached approximately 69% branch coverage for this iteration; for the next iteration, we plan to expand our testing to the controller and service layers and meet a minimum of 80% branch coverage for the whole project.

### Style Checking
We used Checkstyle to ensure code quality and adherence to coding standards. The Checkstyle configuration file is located in the root directory as `checkstyle.xml`. To run Checkstyle, use the following Maven command:

```bash
mvn checkstyle:check
```

### AI Usage
[waiting for info from team]

## Next Steps
Currently, the service supports basic citation generation and source management. Before the first demo, the team will be refactoring the APIs
to support a more flexible citation generation mechanism that allows for backfilling missing information with AI, a feature to be added in the second iteration.
These changes will include:
- Modifying GET endpoints to utilize both a Citation object and a Source object according to the linked [API design](https://docs.google.com/document/d/1h68plDcqBSd3OXQ8W1byPptmb5XWFZ081d9pRdjwgis/edit?usp=sharing).
- Fix controller methods associated with these endpoints.
- Update README to reflect new API usage.
- Complete AI documentation used for the first iteration.
