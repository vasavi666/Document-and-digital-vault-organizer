import React from 'react';
import { NavLink, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { 
  FiHome, 
  FiFileText, 
  FiLink, 
  FiShare2, 
  FiUsers, 
  FiSettings,
  FiX
} from 'react-icons/fi';
import { RiSecurePaymentLine } from 'react-icons/ri';

const Sidebar = ({ isOpen, setIsOpen }) => {
  const { user } = useAuth();
  const location = useLocation();

  const navLinks = [
    { name: 'Dashboard', path: '/', icon: <FiHome size={20} /> },
    { name: 'My Documents', path: '/documents', icon: <FiFileText size={20} /> },
    { name: 'Important Links', path: '/links', icon: <FiLink size={20} /> },
    { name: 'Shared with Me', path: '/sharing', icon: <FiShare2 size={20} /> },
  ];

  const adminLinks = [
    { name: 'User Management', path: '/admin/users', icon: <FiUsers size={20} /> },
    { name: 'System Settings', path: '/admin', icon: <FiSettings size={20} /> },
  ];

  return (
    <>
      {/* Mobile sidebar backdrop */}
      <div 
        className={`fixed inset-0 z-40 bg-slate-900/80 backdrop-blur-sm lg:hidden transition-opacity duration-300 ${isOpen ? 'opacity-100' : 'opacity-0 pointer-events-none'}`}
        onClick={() => setIsOpen(false)}
      />

      {/* Sidebar */}
      <aside 
        className={`fixed lg:static inset-y-0 left-0 z-50 w-72 bg-white dark:bg-slate-900 border-r border-slate-200 dark:border-slate-800 transition-transform duration-300 transform flex flex-col ${isOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'}`}
      >
        <div className="flex items-center justify-between h-20 px-6 border-b border-slate-200 dark:border-slate-800">
          <div className="flex items-center gap-3 text-primary-600 dark:text-primary-500">
            <RiSecurePaymentLine size={32} className="animate-pulse" />
            <span className="text-xl font-bold tracking-tight text-slate-900 dark:text-white">Smart Vault</span>
          </div>
          <button 
            className="lg:hidden p-2 text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-200"
            onClick={() => setIsOpen(false)}
          >
            <FiX size={24} />
          </button>
        </div>

        <div className="flex-1 overflow-y-auto py-6 px-4 space-y-8">
          <div>
            <p className="px-4 text-xs font-semibold text-slate-400 uppercase tracking-wider mb-3">Main Menu</p>
            <nav className="space-y-1">
              {navLinks.map((link) => {
                const isActive = location.pathname === link.path || (link.path !== '/' && location.pathname.startsWith(link.path));
                return (
                  <NavLink
                    key={link.name}
                    to={link.path}
                    onClick={() => setIsOpen(false)}
                    className={`sidebar-link ${isActive ? 'sidebar-link-active' : ''}`}
                  >
                    {link.icon}
                    <span>{link.name}</span>
                  </NavLink>
                );
              })}
            </nav>
          </div>

          {user?.role === 'ADMIN' && (
            <div>
              <p className="px-4 text-xs font-semibold text-slate-400 uppercase tracking-wider mb-3">Administration</p>
              <nav className="space-y-1">
                {adminLinks.map((link) => {
                  const isActive = location.pathname === link.path;
                  return (
                    <NavLink
                      key={link.name}
                      to={link.path}
                      onClick={() => setIsOpen(false)}
                      className={`sidebar-link ${isActive ? 'sidebar-link-active' : ''}`}
                    >
                      {link.icon}
                      <span>{link.name}</span>
                    </NavLink>
                  );
                })}
              </nav>
            </div>
          )}
        </div>

        <div className="p-4 border-t border-slate-200 dark:border-slate-800">
          <div className="flex items-center gap-3 px-4 py-3 bg-slate-50 dark:bg-slate-800 rounded-xl">
            <div className="w-10 h-10 rounded-full bg-gradient-to-tr from-primary-500 to-primary-300 flex items-center justify-center text-white font-bold text-sm shadow-md">
              {user?.fullName?.charAt(0) || 'U'}
            </div>
            <div className="flex-1 min-w-0">
              <p className="text-sm font-medium text-slate-900 dark:text-white truncate">
                {user?.fullName}
              </p>
              <p className="text-xs text-slate-500 dark:text-slate-400 truncate">
                {user?.email}
              </p>
            </div>
          </div>
        </div>
      </aside>
    </>
  );
};

export default Sidebar;
