import api from './api';

export const dashboardService = {
  getDashboard: async () => {
    return await api.get('/dashboard');
  }
};
