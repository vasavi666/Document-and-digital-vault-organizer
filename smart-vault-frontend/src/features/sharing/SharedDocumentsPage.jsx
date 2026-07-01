import React from 'react';
import { FiShare2, FiShield } from 'react-icons/fi';

const SharedDocumentsPage = () => {
  return (
    <div className="space-y-6 animate-fade-in pb-10">
      <div>
        <h1 className="text-2xl md:text-3xl font-bold text-slate-900 dark:text-white tracking-tight">Shared Documents</h1>
        <p className="text-slate-500 dark:text-slate-400 mt-1">Manage documents you've shared or received.</p>
      </div>

      <div className="card p-12 flex flex-col items-center justify-center text-center mt-8">
        <div className="relative mb-6">
          <div className="w-24 h-24 bg-gradient-to-tr from-primary-100 to-purple-100 dark:from-primary-900/30 dark:to-purple-900/30 rounded-full flex items-center justify-center">
            <FiShare2 size={40} className="text-primary-500" />
          </div>
          <div className="absolute -bottom-2 -right-2 w-10 h-10 bg-white dark:bg-slate-800 rounded-full flex items-center justify-center shadow-md">
            <FiShield size={20} className="text-emerald-500" />
          </div>
        </div>
        <h3 className="text-xl font-bold text-slate-900 dark:text-white mb-2">Secure Sharing feature coming soon</h3>
        <p className="text-slate-500 max-w-md mx-auto mb-8">
          Soon you'll be able to generate secure, time-limited, password-protected links to share your sensitive documents with verified recipients.
        </p>
        <button className="btn-secondary" disabled>Feature in Development</button>
      </div>
    </div>
  );
};

export default SharedDocumentsPage;
