import React, { useState } from 'react';
import { FiMenu, FiBell, FiSearch, FiMoon, FiSun, FiLogOut, FiUser, FiSettings } from 'react-icons/fi';
import { useAuth } from '../../context/AuthContext';
import { useTheme } from '../../context/ThemeContext';
import { useNavigate } from 'react-router-dom';

const Header = ({ sidebarOpen, setSidebarOpen }) => {
  const { user, logout } = useAuth();
  const { isDark, toggleTheme } = useTheme();
  const [showDropdown, setShowDropdown] = useState(false);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
    setShowDropdown(false);
  };

  return (
    <header className="sticky top-0 z-30 flex h-20 items-center justify-between px-4 md:px-6 lg:px-8 bg-white/80 dark:bg-slate-900/80 backdrop-blur-lg border-b border-slate-200/50 dark:border-slate-800/50">
      <div className="flex items-center gap-4">
        <button
          className="lg:hidden p-2 text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-200 rounded-lg hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
          onClick={() => setSidebarOpen(true)}
        >
          <FiMenu size={24} />
        </button>

        <div className="hidden md:flex items-center gap-2 px-4 py-2 bg-slate-100 dark:bg-slate-800 rounded-full text-slate-500 dark:text-slate-400 focus-within:ring-2 focus-within:ring-primary-500/50 focus-within:text-primary-500 transition-all w-64 lg:w-96">
          <FiSearch size={18} />
          <input 
            id="global-search"
            type="text" 
            placeholder="Search documents, links..." 
            className="bg-transparent border-none outline-none text-sm w-full placeholder-slate-400 dark:placeholder-slate-500 text-slate-900 dark:text-slate-100"
          />
        </div>
      </div>

      <div className="flex items-center gap-2 sm:gap-4">
        <button 
          id="theme-toggle"
          onClick={toggleTheme}
          className="p-2.5 text-slate-500 hover:text-primary-600 dark:text-slate-400 dark:hover:text-primary-400 rounded-full hover:bg-primary-50 dark:hover:bg-slate-800 transition-colors"
          title={isDark ? 'Switch to light mode' : 'Switch to dark mode'}
        >
          {isDark ? <FiSun size={20} /> : <FiMoon size={20} />}
        </button>
        
        <button 
          id="notifications-btn"
          className="relative p-2.5 text-slate-500 hover:text-primary-600 dark:text-slate-400 dark:hover:text-primary-400 rounded-full hover:bg-primary-50 dark:hover:bg-slate-800 transition-colors"
          title="Notifications"
        >
          <FiBell size={20} />
          <span className="absolute top-2 right-2 w-2 h-2 bg-rose-500 rounded-full ring-2 ring-white dark:ring-slate-900"></span>
        </button>

        <div className="relative">
          <button 
            id="user-menu"
            className="flex items-center gap-3 p-1.5 pr-3 rounded-full hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
            onClick={() => setShowDropdown(!showDropdown)}
          >
            <div className="w-9 h-9 rounded-full bg-gradient-to-tr from-primary-500 to-purple-500 flex items-center justify-center text-white font-bold text-sm shadow-md">
              {user?.fullName?.charAt(0)?.toUpperCase() || 'U'}
            </div>
            <span className="hidden sm:block text-sm font-medium text-slate-700 dark:text-slate-300 max-w-[120px] truncate">
              {user?.fullName || 'User'}
            </span>
          </button>
          
          {showDropdown && (
            <>
              <div className="fixed inset-0 z-10" onClick={() => setShowDropdown(false)}></div>
              <div className="absolute right-0 mt-2 w-56 bg-white dark:bg-slate-800 rounded-xl shadow-lg border border-slate-200 dark:border-slate-700 z-20 py-2 animate-fade-in origin-top-right">
                <div className="px-4 py-3 border-b border-slate-100 dark:border-slate-700">
                  <p className="text-sm font-semibold text-slate-900 dark:text-white truncate">{user?.fullName || 'User'}</p>
                  <p className="text-xs text-slate-500 dark:text-slate-400 truncate mt-0.5">{user?.email || ''}</p>
                </div>
                <button 
                  className="w-full text-left px-4 py-2.5 text-sm text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700 flex items-center gap-3 transition-colors"
                  onClick={() => setShowDropdown(false)}
                >
                  <FiUser size={16} />
                  <span>Profile</span>
                </button>
                <button 
                  className="w-full text-left px-4 py-2.5 text-sm text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700 flex items-center gap-3 transition-colors"
                  onClick={() => setShowDropdown(false)}
                >
                  <FiSettings size={16} />
                  <span>Settings</span>
                </button>
                <div className="border-t border-slate-100 dark:border-slate-700 mt-1 pt-1">
                  <button 
                    id="logout-btn"
                    onClick={handleLogout}
                    className="w-full text-left px-4 py-2.5 text-sm text-rose-600 dark:text-rose-400 hover:bg-rose-50 dark:hover:bg-rose-900/20 flex items-center gap-3 transition-colors"
                  >
                    <FiLogOut size={16} />
                    <span>Sign out</span>
                  </button>
                </div>
              </div>
            </>
          )}
        </div>
      </div>
    </header>
  );
};

export default Header;
