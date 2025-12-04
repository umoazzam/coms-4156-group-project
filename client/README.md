# University Library Citation Client

This is a Python client application that demonstrates the integration with the Citation Service API. The application simulates a university library management system where students and faculty can generate citations for required course readings.

## Features

- **Course Reading List**: Displays a list of required readings for a course
- **Multiple Citation Styles**: Supports MLA, APA, and Chicago citation formats
- **Real-time Citation Generation**: Integrates with the Citation Service API to generate properly formatted citations
- **Simple Web Interface**: Clean, user-friendly interface for easy navigation

## Requirements

- Python 3.8+
- Flask 3.0.0
- Requests 2.31.0

## Installation

1. Navigate to the client directory:
   ```bash
   cd client
   ```

2. Install dependencies:
   ```bash
   pip install -r requirements.txt
   ```

## Running the Application

1. Start the Citation Service API (from the main project directory):
   ```bash
   mvn spring-boot:run
   ```

2. In a separate terminal, start the client application:
   ```bash
   cd client
   python app.py
   ```

3. Open your browser and navigate to `http://localhost:5000`

## Usage

1. The application will display the "Required Readings" page for COMS W 4156: Advanced Software Engineering
2. You'll see a list of required readings including books, articles, and videos
3. For each reading, select a citation style from the dropdown (MLA, APA, or Chicago)
4. Click "Generate Citation" to get a properly formatted citation
5. The generated citation will appear below the reading information

## API Integration

The client communicates with the Citation Service API running on `http://localhost:8080` by default. You can modify the API base URL in `citation_client.py` if your service runs on a different port.

## Project Structure

```
client/
├── app.py                 # Main Flask application
├── citation_client.py     # API client for Citation Service
├── requirements.txt       # Python dependencies
├── templates/            # HTML templates
│   └── index.html
└── static/              # Static files (CSS, JS)
    └── style.css
```
