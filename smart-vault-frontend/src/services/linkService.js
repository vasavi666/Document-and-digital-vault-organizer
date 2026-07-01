import api from './api';

export const linkService = {
  createLink: async (data) => {
    return await api.post('/links', data);
  },

  getLinks: async (params) => {
    return await api.get('/links', { params });
  },

  getLink: async (id) => {
    return await api.get(`/links/${id}`);
  },

  updateLink: async (id, data) => {
    return await api.put(`/links/${id}`, data);
  },

  deleteLink: async (id) => {
    return await api.delete(`/links/${id}`);
  },

  toggleFavorite: async (id) => {
    return await api.patch(`/links/${id}/favorite`);
  },

  searchLinks: async (query) => {
    return await api.get('/links/search', { params: { query } });
  }
};
