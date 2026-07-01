import api from './api';

export const shareService = {
  createShareLink: async (documentId, data) => {
    return await api.post(`/share/documents/${documentId}`, data);
  },

  getSharedDocument: async (token) => {
    return await api.get(`/shared/${token}`);
  },

  revokeShareLink: async (id) => {
    return await api.delete(`/share/${id}`);
  },

  getMyShares: async () => {
    return await api.get('/share/my-shares');
  }
};
