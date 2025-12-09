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
│   │   ├── `CitationServiceApplication.java`
│   │   ├── controller/                             # API Endpoints
│   │   │   ├── `CitationController.java`           # endpoints updated (e.g. GET /api/cite/{citationId})
│   │   │   ├── `SourceController.java`             # POST /api/source/sources, etc.
│   │   │   └── dto/
│   │   │       ├── `BulkSourceRequest.java`
│   │   │       └── `SourceBatchResponse.java`      # field renamed: `citationIds` (was `sourceIds`)
│   │   ├── exception/
│   │   │   ├── `GlobalExceptionHandler.java`
│   │   │   ├── `ResourceNotFoundException.java`
│   │   │   └── `ValidationException.java`
│   │   ├── model/
│   │   │   ├── `Article.java`
│   │   │   ├── `Book.java`
│   │   │   ├── `Citation.java`
│   │   │   ├── `Source.java`
│   │   │   ├── `Submission.java`
│   │   │   ├── `User.java`
│   │   │   └── `Video.java`
│   │   ├── repository/                             # Database Method Management
│   │   │   ├── `ArticleRepository.java`
│   │   │   ├── `BookRepository.java`
│   │   │   ├── `CitationRepository.java`
│   │   │   ├── `SubmissionRepository.java`
│   │   │   ├── `UserRepository.java`
│   │   │   └── `VideoRepository.java`
│   │   ├── service/                                # Backend Logic
│   │   │   ├── `CitationService.java`
│   │   │   ├── `CrossRefDoiService.java`          # CrossRef API for article backfill using DOI
│   │   │   ├── `GoogleBooksService.java`          # Google API for book backfill using ISBN
│   │   │   └── `SourceService.java`
│   │   └── utils/
│   │       └── `DatabaseStartupCheck.java`        # Checks database connection
│   └── resources/
│       ├── `application.properties`
│       └── `(local) application-dev.properties`   # NOT committed — see 'Running the Application'
└── test/
    └── java/com/columbia/coms4156/citationservice/
        ├── controller/
        │   ├── CitationControllerIntegrationTest.java
        │   ├── CitationControllerTest.java
        │   └── SourceControllerTest.java
        │
        ├── model/
        │   ├── ArticleTest.java
        │   ├── BookTest.java
        │   ├── CitationResponseTest.java
        │   ├── CitationTest.java
        │   ├── ErrorResponseTest.java
        │   ├── GroupCitationResponseTest.java
        │   ├── SourceTest.java
        │   ├── SubmissionTest.java
        │   ├── UserTest.java
        │   └── VideoTest.java
        │
        ├── service/
        │   ├── CitationServiceTest.java
        │   ├── CrossRefDoiServiceTest.java
        │   ├── GoogleBooksServiceTest.java
        │   └── SourceServiceTest.java
        │
        └── CitationServiceApplicationTests.java
```

## Class and Database Design
See [here](https://www.canva.com/design/DAG2NLXV3-U/WCSwNCgI2ZkAA9SOC6vNbQ/edit) for design.

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
   You should see the following text in your terminal: ✅ Successfully connected to database: jdbc:postgresql://34.67.102.29:5432/ase-project?sslmode=require

4. **Access the application**
   - API Base URL: `http://localhost:8080`
   - Database: https://console.cloud.google.com/sql/instances/ase-project/studio?authuser=1&project=not-founders 
      - Access given upon request.

5. **Tear Down**
  - Once done with using the application, ensure you turn off the CloudSQL server on the [Google Console CloudSQL](https://console.cloud.google.com/sql/instances/ase-project/overview?authuser=1&project=not-founders) page.

## API Endpoints

### SourceController

| Method | Endpoint                   | Description             | Input                                                                               | Output                                                                                 |
|--------|----------------------------|-------------------------|-------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------|
| POST   | `/api/source/book`         | Create a new Book       | Book JSON (title*, author*, publisher, publicationYear, city, edition, isbn)        | 201 CREATED w/ Book object with generated ID or 404 BAD REQUEST                        |
| GET    | `/api/source/book`         | Get all Books           | None                                                                                | 200 OK w/ List of Book objects or 404 ERROR                                            |
| GET    | `/api/source/book/{id}`    | Get a Book by ID        | Path param: id (Long)                                                               | 200 OK w/ Book object or 404 ERROR                                                     |
| PUT    | `/api/source/book/{id}`    | Update a Book           | Path param: id (Long), Book JSON                                                    | 200 OK w/ Updated Book object or 404 ERROR                                             |
| DELETE | `/api/source/book/{id}`    | Delete a Book           | Path param: id (Long)                                                               | 204 No Content or 404 NOT FOUND                                                        |
| POST   | `/api/source/video`        | Create a new Video      | Video JSON (title*, author*, director, durationSeconds, platform, url, releaseYear) | 201 CREATED w/ Video object with generated ID or 404 BAD REQUEST                       |
| GET    | `/api/source/video`        | Get all Videos          | None                                                                                | 200 OK w/ List of Video objects or 404 ERROR                                           |
| GET    | `/api/source/video/{id}`   | Get a Video by ID       | Path param: id (Long)                                                               | 200 OK w/ Video object or 404 ERROR                                                    |
| PUT    | `/api/source/video/{id}`   | Update a Video          | Path param: id (Long), Video JSON                                                   | 200 OK w/ Updated Video object or 404 ERROR                                            |
| DELETE | `/api/source/video/{id}`   | Delete a Video          | Path param: id (Long)                                                               | 204 No Content or 404 NOT FOUND                                                        |
| POST   | `/api/source/article`      | Create a new Article    | Article JSON (title*, author*, journal, volume, issue, pages, doi, publicationYear) | 201 CREATED w/ Article object with generated ID or 404 BAD REQUEST                     |
| GET    | `/api/source/article`      | Get all Articles        | None                                                                                | 200 OK w/ List of Article objects or 404 ERROR                                         |
| GET    | `/api/source/article/{id}` | Get an Article by ID    | Path param: id (Long)                                                               | 200 OK w/ Article object or 404 ERROR                                                  |
| PUT    | `/api/source/article/{id}` | Update an Article       | Path param: id (Long), Article JSON                                                 | 200 OK w/ Updated Article object or 404 ERROR                                          |
| DELETE | `/api/source/article/{id}` | Delete an Article       | Path param: id (Long)                                                               | 204 NO CONTENT or 404 NOT FOUND                                                        |
| POST   | `/api/source/sources`      | Create multiple sources | Query param: submissionId (long) (optional)                                         | 201 CREATED w/ SubmissionId and List of citationIds for each source or 404 BAD REQUEST |

*Required fields

### CitationController
| Method | Endpoint                       | Description                                                           | Input                                                                                            | Output                                                |
|--------|--------------------------------|-----------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|-------------------------------------------------------|
| GET    | `/api/cite/book/{id}`          | Generate MLA citation for a stored Book by ID                         | Path param: id (Long)                                                                            | 200 OK w/ Citation string or 404 NOT FOUND            |
| POST   | `/api/cite/book`               | Generate MLA citation from provided Book JSON (no save)               | Book JSON + Query param: style (default: "MLA")                                                  | 200 OK w/ Citation string or 404 BAD REQUEST          |
| GET    | `/api/cite/video/{id}`         | Generate MLA citation for a stored Video by ID                        | Path param: id (Long)                                                                            | 200 OK w/ Citation string or 404 NOT FOUND            |
| POST   | `/api/cite/video`       | Generate MLA citation from provided Video JSON (no save)              | Video JSON + Query param: style (default: "MLA")                                                 | 200 OK w/ Citation string or 404 BAD REQUEST          |
| GET    | `/api/cite/article/{id}` | Generate MLA citation for a stored Article by ID                      | Path param: id (Long)                                                                            | 200 OK w/ Citation string or 404 NOT FOUND            |
| POST   | `/api/cite/article`    | Generate MLA citation from provided Article JSON (no save)            | Article JSON + Query param: style (default: "MLA")                                               | 200 OK w/ Citation string or 404 BAD REQUEST          |
| GET    | `/api/cite/{citationId}`  | Generate citation for a single source with style and backfill options | Path param: citationId (Long), Query params: style (default: "MLA"), backfill (default: false)     | 200 OK w/ CitationResponse JSON or 404 NOT FOUND      |
| GET    | `/api/cite/group/{submissionId}`  | Generate citations for all sources in a submission group              | Path param: submissionId (Long), Query params: style (default: "MLA"), backfill (default: false) | 200 OK w/ GroupCitationResponse JSON or 404 NOT FOUND |

## API Usage
This section outlines the most important API endpoints for our project. It will outline a series a API endpoints you can use to view all available sources, how to upload your own sources, and how to cite sources from the available list or the sources that you uploaded yourself (with the option of backfilling or specific style selection. 
**Note:** Backfill capabilities are only available for sources that are uploaded through the **POST** `http://localhost:8080/api/source/sources`, as a `citationId` is needed.
`<sourcetype>` can be replaced with any of the following sources: `book`, `video`, `article`. The source data (if needed) should be included in the body of the request using the [Source Object JSON Schemas](#source-object-json-schemas) documentation below.

### SourceController API Examples: Uploading or finding a source to cite

**POST** `http://localhost:8080/api/source/<sourcetype>`
- Takes in a source object and saves it to the database. Returns the ID of the object.      

**Request Body (with `<sourcetype> = Book`)**
```json
{
  "title": "Neural Networks for Humans",
  "author": "Andrew Ng",
  "publisher": "DeepLearning Media",
  "publicationYear": 2023,
  "city": "San Francisco",
  "edition": "2nd",
  "isbn": "9789876543002"
}
```
- Returns the created source object (in this case, a Book), with the ID.

**Response**
```json
{
    "id": 73,
    "title": "Neural Networks for Humans",
    "author": "Andrew Ng",
    "publisher": "DeepLearning Media",
    "publicationYear": 2023,
    "city": "San Francisco",
    "edition": "2nd",
    "isbn": "9789876543002"
}
```

**GET** `http://localhost:8080/api/source/<sourcetype>`  
- Retrieves all the sources in the databse of this source type.

**Response**:
```json
[
  {
    "title": "To Kill a Mockingbird",
    "author": "Harper Lee",
    "publisher": "J.B. Lippincott & Co.",
    "publicationYear": 1960,
    "city": "Philadelphia"
  },
  {
    "title": "To Kill a Mockingbird 2",
    "author": "Harper Lee",
    "publisher": "J.B. Lippincott & Co.",
    "publicationYear": 1965,
    "city": "Philadelphia"
  }
]
```

**POST** `http://localhost:8080/api/source/sources`  
- Upload one or more sources to the database. Will create a submission object with a "submissionId" that you can use to refer to these sources later. The submission object will hold citationIds, which can be used to cite the sources you uploaded.

**Request Body:**
```json
{
  "user": { 
    "username": "mattlabasan"
  },
  "sources": [
    {
      "mediaType": "book",
      "title": "Deep Learning with Python",
      "author": "François Chollet",
      "ISBN": "9781617294433",
      "publisher": "Manning Publications",
      "year": 2018,
      "URL": "https://www.manning.com/books/deep-learning-with-python",
      "accessDate": "2025-10-22"
    },
    {
      "mediaType": "video",
      "title": "Understanding Neural Networks",
      "author": "3Blue1Brown",
      "platform": "YouTube",
      "URL": "https://www.youtube.com/watch?v=aircAruvnKk",
      "accessDate": "2025-10-22",
      "duration": "19:33",
      "channel": "3Blue1Brown"
    }
  ]
}
```

**Response**
```json
{
    "submissionId": 75,
    "citationIds": [
        "107", // CitationId for "Deep Learning with Python". 
        "108"  // CitationId for "Understanding Neural Networks"
    ],
    "errors": []
}
```

### CitationController API: Creating your citations

**POST** `http://localhost:8080/api/cite/<sourcetype>`
- Retrieve the ad-hoc generation of a citation.

**Request Body (with `<sourcetype> = Book`)**
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

**GET** `http://localhost:8080/api/cite/{citationId}?style=APA&backfill=true`
- Generate a citation for a single source with specified style and backfill options. The citationId can be retrieved when uploading sources using the **POST** `http://localhost:8080/api/source/sources` endpoint.

**Parameters:**
- `citationId` (path): The unique identifier of the citation object.
- `style` (query, optional): Citation style - "MLA", "APA", or "Chicago" (default: "MLA")
- `backfill` (query, optional): Boolean for whether to include backfill information (default: false)

**Response:**
- Where `{citationId}` = 180
```json
{
  "CitationID": "180",
  "CitationString": "Sarker, Iqbal. \"Machine Learning: Algorithms, Real-World Applications and Research Directions.\" SN Computer Science, vol. 2, no. 3, 2021."
}
```

**GET** `http://localhost:8080/api/cite/group/{submissionId}?style=Chicago&backfill=false`
- Generate citations for all sources in a submission group. The submissionId can be retrieved when uploading sources using the **POST** `http://localhost:8080/api/source/sources` endpoint.

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

### Source Object JSON Schemas
This is the format of JSON objects to be submitted with the various requests above in the Body of the request. All attributes for these objects are optional except for `author` and `title`.

#### Book Schema
```json
{
  "id": "number (auto-generated)",
  "title": "string (required)",
  "author": "string (required)",
  "publisher": "string (optional)",
  "publicationYear": "number (optional)",
  "city": "string (optional)",
  "edition": "string (optional)",
  "isbn": "string (optional)" // Required for backfill capabilities
}
```

#### Video Schema
```json
{
  "id": "number (auto-generated)",
  "title": "string (required)",
  "author": "string (required)",
  "director": "string (optional)",
  "duration_seconds": "number (optional)",
  "platform": "string (optional)",
  "release_year": "number (optional)",
  "url": "string (optional)"
}
```

#### Article Schema
```json
{
  "id": "number (auto-generated)",
  "title": "string (required)",
  "author": "string (required)",
  "journal": "string (optional)",
  "volume": "string (optional)",
  "issue": "string (optional)",
  "pages": "string (optional)",
  "publicationYear": "number (optional)",
  "doi": "string (optional)", // Required for backfill capabilities
  "url": "string (optional)"
}
```

### Backfilling
Backfilling is currently available for the Books and Articles. Books are backfilled using the external API, [Google Books API](https://developers.google.com/books) and Articles are backfilled using the external API, [CrossRef API](https://api.crossref.org/swagger-ui/index.html). Book sources that you desire to be backfilled must include an ISBN, and Article sources must include a DOI.

## Client Application

A demo client application is included in the `client` directory of this repository. This client is a simple React application built with TypeScript that demonstrates how to interact with the Citation Service API.

### Client Functionality

The client application simulates a university library management system for a course's "Required Readings" list. It displays a predefined list of resources (books, articles, and videos) and allows the user to generate citations for them in MLA, APA, or Chicago style.

When the "Generate Citation" button is clicked for a resource, the client sends a request to the service's API. The API then processes the request and returns the formatted citation, which is then displayed on the page.

### Running the Client

To run the client application, navigate to the `client` directory and install the dependencies:

```bash
cd client
npm install
```

Then, start the development server:

```bash
npm run dev
```

The client will be available at `http://localhost:5173`.

### Connecting to the Service

The client is configured to connect to the service API running at `http://localhost:8080`. Ensure that the main Spring Boot application is running before you start the client.

The service is able to handle multiple instances of the client running simultaneously. Since the client is a stateless, frontend application, each instance maintains its own state and makes independent API calls to the service. The service processes each request without needing to differentiate between clients, as each API call is a self-contained transaction.

## Testing with Postman
These are API tests you can use to exercise the service endpoints (create, retrieve, update, delete sources and generate citations).

[Postman collection — API tests](https://web.postman.co/workspace/My-Workspace~944de483-7347-4047-89cb-e75c81e1ba7b/collection/32914220-d96cfb1c-3cc6-48fc-993f-46baea892753?action=share&source=copy-link&creator=32914220)

## Development Notes

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

### Logging
We use **SLF4J** with **Logback** for logging. Logs are persisted to the `logs/` directory in the project root.
- **File**: `logs/application.log`
- **Rotation**: Daily rotation with a 30-day history and 3GB total size cap.
- **Configuration**: `src/main/resources/logback-spring.xml`

### AI Usage
For this project, we used GitHub Copilot to assist with code generation and troubleshooting. GitHub Copilot is freely available for students through the [GitHub Student Developer Pack](https://education.github.com/pack).

For brevity, our use cases are summarized in the table below. While not every prompt and response is documented here, these examples illustrate the types of assistance we received from the AI tool throughout the development process, as well as what files were affected by the changes.

| Task                                                         | Prompt                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                | Summary of Response                                                                  | File(s) Affected                                                                               |
|--------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|
| Set up general Spring Boot project structure                 | "I want your help in establishing starter code for a project in which we will be creating an API-based citation creation and management service that allows clients to build, maintain, and enrich libraries of sources while automatically generating formatted citations and bibliographies. To start, we are only going to focus on MLA citations and books, so that we can set up demoable functionality. The end goal of this demo deliverable is the ability to locally call the API from postman or other methods with a JSON body containing information about a book, with the API returning the citation text. First, I want your help in setting up the files needed for this API service to run successfully, as well as setting up skeleton code for the book model file, service file etc (actual functionality will be worked on later). After this is done, I will provide further specifications for the service and model. For this project, we would like to use Java/SpringBoot." | Generated basic Spring Boot project structure with necessary files and dependencies. | Multiple files including main application, controller, model, repository, and service classes. |
| Set up persistent database code and model set up             | “How do I connect a PostgreSQL Cloud SQL instance to a Spring Boot app?” & “What files do I need to edit to connect Spring Boot to Cloud SQL?”, “What files do I need to edit to connect Spring Boot to Cloud SQL?”, etc.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             | Helped set up persistent data storage connection and troubleshoot connection issues. | application.properties, DatabaseStartupCheck.java, pom.xml, model layer, repository layer, etc |
| Set up parameter validation and unit testing for model layer | "Set up basic parameter validation in this class for the setter methods and then create a unit test file for the class which tests the setting methods"                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               | Helped set up parameter validation and unit tests for model layer classes.           | Book.java, BookTest.java, Video.java, VideoTest.java, Article.java, ArticleTest.java, etc      |
| PR Description                                               | "Please help me write a detailed PR description for the following code changes: ...                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | Created a comprehensive PR description outlining the changes made.                   | N/A (Documentation)                                                                            |
| Fixing Checkstyle Errors/Updating Checkstyle Config          | "Help fix the checkstyle errors in this file: ... Also, please update the checkstyle configuration file to allow for longer line lengths (up to 100 characters)", "What script can fix the trailing spaces and line length issues in the controllers?"                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                | Resolved Checkstyle errors and updated configuration as requested.                   | Multiple Java files and checkstyle.xml                                                         |
| Updating README                                              | "Update the file structure in the README"                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             | New file structure as seen above.                                                    | README.md                                                                                      |


## Next Steps
- Add video backfilling capabilities
- Add user authentication
- Deployment & sample client creation
