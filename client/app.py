from flask import Flask, render_template, request, jsonify
from citation_client import CitationClient
import json

app = Flask(__name__)

# Initialize the citation client
citation_client = CitationClient()

# Sample data for required readings - these would typically come from a database
SAMPLE_READINGS = []

def initialize_sample_data():
    """Initialize sample readings by creating them through the API"""
    global SAMPLE_READINGS

    # Sample book
    book_data = citation_client.create_book(
        title="Design Patterns: Elements of Reusable Object-Oriented Software",
        author="Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides",
        publisher="Addison-Wesley Professional",
        year=1994,
        isbn="978-0201633610"
    )

    # Sample article
    article_data = citation_client.create_article(
        title="The Cathedral and the Bazaar",
        author="Eric S. Raymond",
        journal="First Monday",
        year=1998,
        doi="10.5210/fm.v3i3.578",
        volume="3",
        issue="3"
    )

    # Sample video
    video_data = citation_client.create_video(
        title="Clean Code: A Handbook of Agile Software Craftsmanship",
        author="Robert C. Martin",
        platform="YouTube",
        year=2019,
        url="https://www.youtube.com/watch?v=7EmboKQH8lM",
        duration=3600
    )

    # Store the created readings
    if book_data:
        SAMPLE_READINGS.append(book_data)
    if article_data:
        SAMPLE_READINGS.append(article_data)
    if video_data:
        SAMPLE_READINGS.append(video_data)

    # If API calls failed, use fallback static data
    if not SAMPLE_READINGS:
        SAMPLE_READINGS.extend([
            {
                "id": 1,
                "title": "Design Patterns: Elements of Reusable Object-Oriented Software",
                "author": "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides",
                "type": "BOOK",
                "publisher": "Addison-Wesley Professional",
                "year": 1994,
                "isbn": "978-0201633610"
            },
            {
                "id": 2,
                "title": "The Cathedral and the Bazaar",
                "author": "Eric S. Raymond",
                "type": "ARTICLE",
                "journal": "First Monday",
                "year": 1998,
                "doi": "10.5210/fm.v3i3.578",
                "volume": "3",
                "issue": "3"
            },
            {
                "id": 3,
                "title": "Clean Code: A Handbook of Agile Software Craftsmanship",
                "author": "Robert C. Martin",
                "type": "VIDEO",
                "platform": "YouTube",
                "year": 2019,
                "url": "https://www.youtube.com/watch?v=7EmboKQH8lM",
                "duration": 3600
            }
        ])

@app.route('/')
def index():
    """Main page showing the required readings list"""
    # Check if citation service is available
    service_status = citation_client.health_check()

    # Initialize sample data if not already done
    if not SAMPLE_READINGS:
        initialize_sample_data()

    return render_template('index.html',
                         readings=SAMPLE_READINGS,
                         service_status=service_status)

@app.route('/generate_citation', methods=['POST'])
def generate_citation():
    """Generate citation for a specific source"""
    try:
        data = request.get_json()
        source_id = data.get('source_id')
        style = data.get('style', 'MLA').upper()

        if not source_id:
            return jsonify({
                'success': False,
                'error': 'Source ID is required'
            }), 400

        # Generate citation using the API
        citation = citation_client.generate_citation(source_id, style)

        if citation:
            return jsonify({
                'success': True,
                'citation': citation,
                'style': style
            })
        else:
            return jsonify({
                'success': False,
                'error': f'Failed to generate {style} citation. Please check if the Citation Service is running.'
            }), 500

    except Exception as e:
        return jsonify({
            'success': False,
            'error': f'Internal error: {str(e)}'
        }), 500

@app.route('/health')
def health_check():
    """Health check endpoint for the client application"""
    service_status = citation_client.health_check()
    return jsonify({
        'client_status': 'healthy',
        'citation_service_status': 'connected' if service_status else 'disconnected'
    })

@app.route('/api/readings')
def get_readings():
    """API endpoint to get all readings (for potential API consumers)"""
    return jsonify({
        'readings': SAMPLE_READINGS,
        'count': len(SAMPLE_READINGS)
    })

@app.route('/refresh_data', methods=['POST'])
def refresh_data():
    """Endpoint to refresh sample data from the API"""
    global SAMPLE_READINGS
    SAMPLE_READINGS.clear()
    initialize_sample_data()

    return jsonify({
        'success': True,
        'message': f'Sample data refreshed. {len(SAMPLE_READINGS)} readings loaded.',
        'readings_count': len(SAMPLE_READINGS)
    })

if __name__ == '__main__':
    print("Starting University Library Citation Client...")
    print("This client will connect to the Citation Service at http://localhost:8080")
    print("Make sure your Citation Service is running before generating citations.")
    print("\nAccess the application at: http://localhost:5000")

    # Initialize sample data on startup
    initialize_sample_data()

    # Run the Flask development server
    app.run(debug=True, host='0.0.0.0', port=5000)
