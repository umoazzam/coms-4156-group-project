import requests
import json
from typing import Dict, Any, Optional


class CitationClient:
    """
    Client for interacting with the Citation Service API
    """

    def __init__(self, base_url: str = "http://localhost:8080"):
        """
        Initialize the citation client

        Args:
            base_url: Base URL of the Citation Service API
        """
        self.base_url = base_url.rstrip('/')
        self.session = requests.Session()
        # Set default headers
        self.session.headers.update({
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        })

    def create_source(self, source_data: Dict[str, Any]) -> Optional[Dict[str, Any]]:
        """
        Create a new source in the citation service

        Args:
            source_data: Dictionary containing source information

        Returns:
            Created source data or None if failed
        """
        try:
            url = f"{self.base_url}/api/sources"
            response = self.session.post(url, json=source_data)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            print(f"Error creating source: {e}")
            return None

    def get_source(self, source_id: int) -> Optional[Dict[str, Any]]:
        """
        Get a source by ID

        Args:
            source_id: ID of the source to retrieve

        Returns:
            Source data or None if not found
        """
        try:
            url = f"{self.base_url}/api/sources/{source_id}"
            response = self.session.get(url)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            print(f"Error getting source {source_id}: {e}")
            return None

    def generate_citation(self, source_id: int, style: str = "MLA",
                         backfill: bool = True) -> Optional[str]:
        """
        Generate a citation for a source

        Args:
            source_id: ID of the source to cite
            style: Citation style (MLA, APA, Chicago)
            backfill: Whether to backfill missing information

        Returns:
            Formatted citation string or None if failed
        """
        try:
            url = f"{self.base_url}/api/citations/generate"
            params = {
                'sourceId': source_id,
                'style': style.upper(),
                'backfill': str(backfill).lower()
            }
            response = self.session.post(url, params=params)
            response.raise_for_status()

            # The API returns a CitationResponse object
            citation_response = response.json()
            return citation_response.get('citation', '')
        except requests.exceptions.RequestException as e:
            print(f"Error generating citation for source {source_id}: {e}")
            return None

    def create_book(self, title: str, author: str, publisher: str = None,
                   year: int = None, isbn: str = None) -> Optional[Dict[str, Any]]:
        """
        Create a book source

        Args:
            title: Book title
            author: Book author
            publisher: Publisher name
            year: Publication year
            isbn: ISBN number

        Returns:
            Created book data or None if failed
        """
        book_data = {
            "title": title,
            "author": author,
            "type": "BOOK"
        }

        if publisher:
            book_data["publisher"] = publisher
        if year:
            book_data["year"] = year
        if isbn:
            book_data["isbn"] = isbn

        return self.create_source(book_data)

    def create_article(self, title: str, author: str, journal: str = None,
                      year: int = None, doi: str = None, volume: str = None,
                      issue: str = None, pages: str = None) -> Optional[Dict[str, Any]]:
        """
        Create an article source

        Args:
            title: Article title
            author: Article author
            journal: Journal name
            year: Publication year
            doi: DOI
            volume: Volume number
            issue: Issue number
            pages: Page range

        Returns:
            Created article data or None if failed
        """
        article_data = {
            "title": title,
            "author": author,
            "type": "ARTICLE"
        }

        if journal:
            article_data["journal"] = journal
        if year:
            article_data["year"] = year
        if doi:
            article_data["doi"] = doi
        if volume:
            article_data["volume"] = volume
        if issue:
            article_data["issue"] = issue
        if pages:
            article_data["pages"] = pages

        return self.create_source(article_data)

    def create_video(self, title: str, author: str, platform: str = None,
                    year: int = None, url: str = None, duration: int = None) -> Optional[Dict[str, Any]]:
        """
        Create a video source

        Args:
            title: Video title
            author: Video creator/author
            platform: Video platform (YouTube, Vimeo, etc.)
            year: Publication year
            url: Video URL
            duration: Duration in seconds

        Returns:
            Created video data or None if failed
        """
        video_data = {
            "title": title,
            "author": author,
            "type": "VIDEO"
        }

        if platform:
            video_data["platform"] = platform
        if year:
            video_data["year"] = year
        if url:
            video_data["url"] = url
        if duration:
            video_data["duration"] = duration

        return self.create_source(video_data)

    def health_check(self) -> bool:
        """
        Check if the Citation Service API is healthy

        Returns:
            True if service is healthy, False otherwise
        """
        try:
            url = f"{self.base_url}/health"
            response = self.session.get(url, timeout=5)
            return response.status_code == 200
        except requests.exceptions.RequestException:
            return False
