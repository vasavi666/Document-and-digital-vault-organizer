import api from './api';

export const authService = {
  login: async (email, password) => {
    return await api.post('/auth/login', { email, password });
  },

  register: async (fullName, email, password) => {
    return await api.post('/auth/register', { fullName, email, password });
  },

  getCurrentUser: async () => {
    return await api.get('/auth/me');
  },

  forgotPassword: async (email) => {
    return await api.post('/auth/forgot-password', { email });
  },

  resetPassword: async (token, newPassword) => {
    return await api.post('/auth/reset-password', { token, newPassword });
  }
};
