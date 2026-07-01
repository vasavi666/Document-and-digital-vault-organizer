import api from './api';

export const documentService = {
  uploadDocument: async (formData) => {
    return await api.post('/documents', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
  },

  getDocuments: async (params) => {
    return await api.get('/documents', { params });
  },

  getDocument: async (id) => {
    return await api.get(`/documents/${id}`);
  },

  updateDocument: async (id, data) => {
    return await api.put(`/documents/${id}`, data);
  },

  deleteDocument: async (id) => {
    return await api.delete(`/documents/${id}`);
  },

  searchDocuments: async (query, page = 0, size = 10) => {
    return await api.get('/documents/search', { params: { query, page, size } });
  },

  getExpiringDocuments: async (days = 30) => {
    return await api.get('/documents/expiring', { params: { days } });
  },

  toggleFavorite: async (id) => {
    return await api.patch(`/documents/${id}/favorite`);
  }
};
