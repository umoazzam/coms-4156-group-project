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

The service can interface with multiple instances of this client running simultaneously. Since the client is a stateless, frontend application, each instance maintains its own state and makes independent API calls to the service. The service processes each request as a self-contained transaction and does not need to differentiate between individual client instances.
