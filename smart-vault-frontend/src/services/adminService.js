import api from './api';

export const adminService = {
  getUsers: async (params) => {
    return await api.get('/admin/users', { params });
  },

  toggleUserStatus: async (id) => {
    return await api.put(`/admin/users/${id}/status`);
  },

  getSystemStats: async () => {
    return await api.get('/admin/stats');
  },

  getAuditLogs: async (params) => {
    return await api.get('/admin/audit-logs', { params });
  },

  getCategories: async () => {
    return await api.get('/admin/categories');
  },

  createCategory: async (data) => {
    return await api.post('/admin/categories', data);
  },

  deleteCategory: async (id) => {
    return await api.delete(`/admin/categories/${id}`);
  },

  getSharedDocuments: async (params) => {
    return await api.get('/admin/shared-documents', { params });
  }
};
