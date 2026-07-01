import React from 'react';
import { FiUsers, FiSettings, FiActivity, FiDatabase } from 'react-icons/fi';

const AdminDashboard = () => {
  return (
    <div className="space-y-6 animate-fade-in pb-10">
      <div>
        <h1 className="text-2xl md:text-3xl font-bold text-slate-900 dark:text-white tracking-tight">System Administration</h1>
        <p className="text-slate-500 dark:text-slate-400 mt-1">Manage the platform and monitor system health.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="card p-6">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-sm font-medium text-slate-500">Total Users</p>
              <h3 className="text-2xl font-bold text-slate-900 dark:text-white mt-1">128</h3>
            </div>
            <div className="p-3 rounded-xl bg-blue-50 dark:bg-blue-900/20 text-blue-600 dark:text-blue-400">
              <FiUsers size={24} />
            </div>
          </div>
        </div>
        
        <div className="card p-6">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-sm font-medium text-slate-500">Total Storage</p>
              <h3 className="text-2xl font-bold text-slate-900 dark:text-white mt-1">45.2 GB</h3>
            </div>
            <div className="p-3 rounded-xl bg-emerald-50 dark:bg-emerald-900/20 text-emerald-600 dark:text-emerald-400">
              <FiDatabase size={24} />
            </div>
          </div>
        </div>

        <div className="card p-6">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-sm font-medium text-slate-500">System Status</p>
              <h3 className="text-2xl font-bold text-slate-900 dark:text-white mt-1">Healthy</h3>
            </div>
            <div className="p-3 rounded-xl bg-green-50 dark:bg-green-900/20 text-green-600 dark:text-green-400">
              <FiActivity size={24} />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
