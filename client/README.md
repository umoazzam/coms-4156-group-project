# Citation Service Client

This is a simple React client application for the Citation Service API.

## What it does

This client application simulates a university library management system for a course's "Required Readings" list. It displays a predefined list of resources (books, articles, and videos) and allows the user to generate citations for them in MLA, APA, or Chicago style.

When the "Generate Citation" button is clicked for a resource, the client sends a request to the service's API. The API then processes the request and returns the formatted citation, which is then displayed on the page.

## How to build and run

1.  **Install dependencies:**
    From this directory (`client`), run:
    ```bash
    npm install
    ```

2.  **Run the client:**
    ```bash
    npm run dev
    ```
    The client will be available at `http://localhost:5173`.

## How to connect to the service

The client is configured to connect to the service API running at `http://localhost:8080`. Before starting the client, make sure the main Spring Boot application (the citation service) is running.

### Handling Multiple Clients

The service can interface with multiple instances of this client running simultaneously. This is possible because both the client and the service are designed to be stateless. Each client instance manages its own state, and the server processes each API request as a self-contained transaction without needing to store session information.

Here's how the code supports this:

#### 1. Client-Side State Management

Each instance of the React client maintains its own state independently. In `src/App.tsx`, the `useState` hook is used to manage the list of resources and their generated citations locally within the browser.

```typescript
// client/src/App.tsx

function App() {
  const [citations, setCitations] = useState<{ [key: string]: string }>({});
  const [styles, setStyles] = useState<{ [key: string]: string }>({});
  const [resources, setResources] = useState<Resource[]>([]);
  // ...
}
```

When you open the application in two different browser tabs, each tab will have its own `citations`, `styles`, and `resources` state. Changes in one tab will not affect the other.

#### 2. Stateless API Calls

The client makes atomic API calls that do not depend on a server-side session. Each request contains all the necessary information for the server to process it. For example, the `getCitation` function in `src/api.ts` sends the `sourceType`, `sourceId`, and `style` with every request.

```typescript
// client/src/api.ts

export const getCitation = async (sourceType: string, sourceId: number, style: string) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/cite/${sourceType}/${sourceId}`, { params: { style } });
    // ...
    return response.data;
  } catch (error) {
    // ...
    throw error;
  }
};
```

This stateless approach means the server doesn't need to track which client is making a request, as every request is independent.

#### 3. Stateless and Cross-Origin Enabled API

The backend is a stateless REST API. The controllers do not maintain session state between requests. Furthermore, the `@CrossOrigin(origins = "*")` annotation on the controllers, such as `CitationController`, explicitly allows requests from any origin.

```java
// src/main/java/com/columbia/coms4156/citationservice/controller/CitationController.java

@RestController
@RequestMapping("/api/cite")
@CrossOrigin(origins = "*")
public class CitationController {
    // ...
}
```

This configuration is key to allowing multiple client instances, potentially running on different machines or ports, to all interact with the same API endpoints without issue.

## End-to-End Testing

This section documents user stories and a corresponding set of Postman tests to ensure the end-to-end functionality of the client and the citation service API.

### User Stories

1.  **As a user, I want to generate an MLA citation for a book.**
    - *Given* the application has loaded a list of resources including a book.
    - *When* I click the "Generate Citation" button for the book and select "MLA" style.
    - *Then* I should see the correctly formatted MLA citation for the book displayed on the screen.

2.  **As a user, I want to generate an APA citation for an article.**
    - *Given* the application has loaded a list of resources including an article.
    - *When* I click the "Generate Citation" button for the article and select "APA" style.
    - *Then* I should see the correctly formatted APA citation for the article displayed on the screen.

3.  **As a user, I want to generate a Chicago citation for a website.**
    - *Given* the application has loaded a list of resources including a website.
    - *When* I click the "Generate Citation" button for the website and select "Chicago" style.
    - *Then* I should see the correctly formatted Chicago citation for the website displayed on the screen.

### Postman Tests for API Simulation

The following Postman tests simulate the client's interaction with the API.

1.  **Create a Book Source and Generate a Citation**
    - **Step 1: Create Book Source**
        - **Request:** `POST /api/sources`
        - **Body (raw, JSON):**
          ```json
          {
            "sourceType": "Book",
            "title": "The Great Gatsby",
            "authors": ["F. Scott Fitzgerald"],
            "publisher": "Charles Scribner's Sons",
            "publicationYear": 1925
          }
          ```
        - **Tests:** Assert that the response status is `201 Created` and the response body contains the created source with a valid ID.
    - **Step 2: Generate Citation**
        - **Request:** `GET /api/citations?sourceId={sourceId}&style=MLA` (replace `{sourceId}` with the ID from Step 1).
        - **Tests:** Assert that the response status is `200 OK` and the response body contains a non-empty `citation` string.

2.  **Create an Article Source and Generate a Citation**
    - **Step 1: Create Article Source**
        - **Request:** `POST /api/sources`
        - **Body (raw, JSON):**
          ```json
          {
            "sourceType": "Article",
            "title": "The Structure of Scientific Revolutions",
            "authors": ["Thomas S. Kuhn"],
            "journal": "International Encyclopedia of Unified Science",
            "publicationYear": 1962,
            "volume": "2",
            "issue": "2"
          }
          ```
        - **Tests:** Assert that the response status is `201 Created` and the response body contains the created source with a valid ID.
    - **Step 2: Generate Citation**
        - **Request:** `GET /api/citations?sourceId={sourceId}&style=APA` (replace `{sourceId}` with the ID from Step 1).
        - **Tests:** Assert that the response status is `200 OK` and the response body contains a non-empty `citation` string.

3.  **Create a Website Source and Generate a Citation**
    - **Step 1: Create Website Source**
        - **Request:** `POST /api/sources`
        - **Body (raw, JSON):**
          ```json
          {
            "sourceType": "Website",
            "title": "The Official Website of the Nobel Prize",
            "authors": ["The Nobel Foundation"],
            "url": "https://www.nobelprize.org/",
            "accessDate": "2023-10-27"
          }
          ```
        - **Tests:** Assert that the response status is `201 Created` and the response body contains the created source with a valid ID.
    - **Step 2: Generate Citation**
        - **Request:** `GET /api/citations?sourceId={sourceId}&style=Chicago` (replace `{sourceId}` with the ID from Step 1).
        - **Tests:** Assert that the response status is `200 OK` and the response body contains a non-empty `citation` string.

## Third-Party Client Development

To develop a third-party client for the Citation Service API, you will need to interact with the following endpoints. The service is stateless, so no session management or API keys are required.

### API Endpoints

-   **`POST /api/sources`**: Create a new source to be cited.
    -   **Request Body**: A JSON object representing the source. The `sourceType` field determines the type of source (`Book`, `Article`, `Website`) and the other fields depend on the type.
    -   **Response**: The created source object with its assigned ID.

-   **`GET /api/citations?sourceId={id}&style={style}`**: Generate a citation for a source.
    -   **URL Parameters**:
        -   `sourceId`: The ID of the source to cite.
        -   `style`: The citation style (`MLA`, `APA`, `Chicago`).
    -   **Response**: A JSON object containing the formatted citation string.

### Example Workflow

1.  Send a `POST` request to `/api/sources` with the details of the source you want to cite.
2.  Extract the `id` from the JSON response.
3.  Send a `GET` request to `/api/citations` using the extracted `id` and your desired citation `style`.
4.  The response will contain the formatted citation.
