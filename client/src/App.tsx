import { useState, useEffect } from 'react';
import { createSource, getCitation } from './api';
import './App.css';

interface Resource {
  id: string;
  type: string;
  title: string;
  author: string;
  [key: string]: any;
  dbId?: number;
}

function App() {
  const [citations, setCitations] = useState<{ [key: string]: string }>({});
  const [styles, setStyles] = useState<{ [key: string]: string }>({});
  const [isInitializing, setIsInitializing] = useState(true);
  const [resources, setResources] = useState<Resource[]>([]);

  useEffect(() => {
    const initialResources: Omit<Resource, 'dbId'>[] = [
      {
        id: 'book1',
        type: 'book',
        title: 'Deep Learning with Python',
        author: 'François Chollet',
        publisher: 'Manning Publications',
        publicationYear: 2018,
        isbn: '9781617294433',
      },
      {
        id: 'article1',
        type: 'article',
        title: 'Attention Is All You Need',
        author: 'Ashish Vaswani, et al.',
        journal: 'Advances in Neural Information Processing Systems',
        volume: '30',
        publicationYear: 2017,
        doi: '10.48550/arXiv.1706.03762',
      },
      {
        id: 'video1',
        type: 'video',
        title: 'The Cherno',
        author: 'The Cherno',
        platform: 'YouTube',
        url: 'https://www.youtube.com/channel/UCQ-W1l_UtsHyR2YRGnItdYw',
        releaseYear: 2024,
      },
    ];

    const createInitialSources = async () => {
      try {
        const updatedResources = await Promise.all(
          initialResources.map(async (resource) => {
            const { id, type, ...sourceData } = resource;
            const createdSource = await createSource(type, sourceData);
            console.log('API Response for', type, ':', createdSource);
            return { ...resource, dbId: createdSource.id };
          })
        );
        setResources(updatedResources);
      } catch (error) {
        console.error("Failed to create one or more initial sources:", error);
        // In case of an error, populate with initial data so UI isn't empty
        setResources(initialResources.map(r => ({...r, dbId: undefined})));
      } finally {
        setIsInitializing(false);
      }
    };

    createInitialSources();
  }, []);

  const handleGenerateCitation = async (resource: Resource) => {
    if (!resource.dbId) {
      setCitations({ ...citations, [resource.id]: 'Source not created yet.' });
      return;
    }
    const style = styles[resource.id] || 'MLA';
    try {
      const citation = await getCitation(resource.type, resource.dbId, style);
      setCitations({ ...citations, [resource.id]: citation });
    } catch (error: any) {
      let errorMessage = 'An unexpected error occurred.';
      if (error.response) {
        errorMessage = `Error: ${error.response.status} ${error.response.statusText}. `;
        if (error.response.data) {
          if (typeof error.response.data === 'string') {
            errorMessage += error.response.data;
          } else if (error.response.data.message) {
            errorMessage += error.response.data.message;
          } else if (error.response.data.error) {
            errorMessage += error.response.data.error;
          }
        }
      } else if (error.request) {
        errorMessage = 'Error: No response from server. Please check if the API is running.';
      } else {
        errorMessage = error.message;
      }
      setCitations({ ...citations, [resource.id]: errorMessage });
    }
  };

  const handleStyleChange = (resourceId: string, style: string) => {
    setStyles({ ...styles, [resourceId]: style });
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>COMS 4156: Advanced Software Engineering</h1>
        <h2>Required Readings</h2>
      </header>
      {isInitializing ? (
        <p>Loading resources...</p>
      ) : (
        <div className="resource-list">
          {resources.map((resource) => (
            <div key={resource.id} className="resource-item">
              <h3>{resource.title}</h3>
              <p>by {resource.author}</p>
              <div className="citation-controls">
                <select
                  value={styles[resource.id] || 'MLA'}
                  onChange={(e) => handleStyleChange(resource.id, e.target.value)}
                >
                  <option value="MLA">MLA</option>
                  <option value="APA">APA</option>
                  <option value="Chicago">Chicago</option>
                </select>
                <button
                  onClick={() => handleGenerateCitation(resource)}
                  disabled={!resource.dbId}
                >
                  Generate Citation
                </button>
              </div>
              {citations[resource.id] && (
                <div className="citation-result">
                  <p>{citations[resource.id]}</p>
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default App;
