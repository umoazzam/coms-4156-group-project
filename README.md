# Citation Service API

An API-based citation creation and management service that allows clients to build, maintain, and enrich libraries of sources while automatically generating formatted citations and bibliographies.

## Current Features

- **MLA Citation Generation**: Automatically formats book information into proper MLA citations
- **Book Management**: Full CRUD operations for book records
- **REST API**: Clean RESTful endpoints for all operations
- **In-Memory Database**: H2 database for development and testing

## Technology Stack

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Data JPA**
- **H2 Database** (in-memory)
- **Maven** for dependency management

## Project Structure

```
src/
├── main/
│   ├── java/com/columbia/coms4156/citationservice/
│   │   ├── CitationServiceApplication.java     # Main application class
│   │   ├── controller/
│   │   │   └── BookController.java             # REST API endpoints
│   │   ├── model/
│   │   │   └── Book.java                       # Book entity model
│   │   ├── repository/
│   │   │   └── BookRepository.java             # Data access layer
│   │   └── service/
│   │       └── BookService.java                # Business logic layer
│   └── resources/
│       └── application.properties              # Application configuration
└── test/
    └── java/com/columbia/coms4156/citationservice/
        └── CitationServiceApplicationTests.java
```

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

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - API Base URL: `http://localhost:8080`
   - H2 Console (for database inspection): `http://localhost:8080/h2-console`
     - JDBC URL: `jdbc:h2:mem:citationdb`
     - Username: `sa`
     - Password: (leave empty)

## API Endpoints

### Book Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/books` | Create a new book |
| GET | `/api/books` | Get all books |
| GET | `/api/books/{id}` | Get a book by ID |
| PUT | `/api/books/{id}` | Update a book |
| DELETE | `/api/books/{id}` | Delete a book |

### Citation Generation

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/books/{id}/citation` | Generate MLA citation for stored book |
| POST | `/api/books/citation` | Generate MLA citation from book data (without saving) |

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

1. Import the following endpoints into Postman
2. Set the base URL to `http://localhost:8080`
3. Use the JSON examples above for request bodies
4. Set Content-Type header to `application/json` for POST/PUT requests

## Development Notes

- The application uses an in-memory H2 database that resets on each restart
- MLA citation format follows basic academic standards
- Input validation is implemented for required fields
- CORS is enabled for all origins (development setup)

## Next Steps

This starter code provides:
- ✅ Complete Spring Boot application structure
- ✅ REST API endpoints for book management
- ✅ Basic MLA citation generation
- ✅ In-memory database setup
- ✅ Input validation
- ✅ Error handling

Ready for enhancement with additional citation formats, persistent storage, and advanced features.

