import axios from 'axios';

const API_BASE_URL = 'https://citation-service-366055417335.us-central1.run.app/api';

export const createSource = async (sourceType: string, sourceData: any) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/source/${sourceType}`, sourceData);
    return response.data;
  } catch (error) {
    console.error(`Error creating source (${sourceType}):`, error);
    throw error;
  }
};

export const getCitation = async (sourceType: string, sourceId: number, style: string) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/cite/${sourceType}/${sourceId}`, { params: { style } });
    console.log('Citation API Response:', response.data);
    return response.data;
  } catch (error) {
    console.error(`Error getting citation (ID: ${sourceId}):`, error);
    throw error;
  }
};







