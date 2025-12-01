#!/usr/bin/env python3

"""
Quick test script to verify the client application components work
"""

def test_client_setup():
    """Test basic client functionality"""
    try:
        # Test imports
        import flask
        import requests
        from citation_client import CitationClient
        print("✅ All imports successful")

        # Test CitationClient initialization
        client = CitationClient()
        print("✅ CitationClient initialized successfully")

        # Test Flask app import
        from app import app
        print("✅ Flask app imported successfully")

        print("\n🎉 Client application setup is working correctly!")
        print("\nTo run the application:")
        print("1. Start your Citation Service: mvn spring-boot:run")
        print("2. Run the client: python3 app.py")
        print("3. Visit: http://localhost:5000")

        return True

    except ImportError as e:
        print(f"❌ Import error: {e}")
        return False
    except Exception as e:
        print(f"❌ Error: {e}")
        return False

if __name__ == "__main__":
    test_client_setup()
