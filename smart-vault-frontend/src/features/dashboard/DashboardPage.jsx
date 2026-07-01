import React, { useEffect, useState } from 'react';
import { dashboardService } from '../../services/dashboardService';
import { useAuth } from '../../context/AuthContext';
import { 
  FiFileText, 
  FiLink, 
  FiAlertCircle, 
  FiHardDrive,
  FiArrowRight,
  FiClock,
  FiStar,
  FiUploadCloud,
  FiPlus,
  FiShield
} from 'react-icons/fi';
import { Link } from 'react-router-dom';

const StatCard = ({ title, value, icon, subtitle, gradientFrom, gradientTo }) => (
  <div className="card p-6 relative overflow-hidden group hover:shadow-lg hover:-translate-y-0.5 transition-all duration-300">
    <div className={`absolute -right-6 -top-6 w-28 h-28 rounded-full opacity-[0.08] group-hover:scale-[1.6] transition-transform duration-700 bg-gradient-to-br ${gradientFrom} ${gradientTo}`}></div>
    <div className="flex items-start justify-between relative z-10">
      <div>
        <p className="text-sm font-medium text-slate-500 dark:text-slate-400 mb-1">{title}</p>
        <h3 className="text-3xl font-bold text-slate-900 dark:text-white tracking-tight">{value}</h3>
        {subtitle && <p className="text-xs text-slate-500 dark:text-slate-400 mt-2 font-medium">{subtitle}</p>}
      </div>
      <div className={`p-3 rounded-xl bg-gradient-to-br ${gradientFrom} ${gradientTo} text-white shadow-md`}>
        {icon}
      </div>
    </div>
  </div>
);

const DashboardPage = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();

  useEffect(() => {
    const fetchDashboard = async () => {
      try {
        const response = await dashboardService.getDashboard();
        if (response.success) {
          setData(response.data);
        }
      } catch (error) {
        console.error("Failed to load dashboard data", error);
        // Set empty state if backend is unreachable
        setData({
          totalDocuments: 0,
          totalLinks: 0,
          expiringSoon: 0,
          storageUsed: 0,
          recentUploads: [],
          expiringDocuments: [],
          favoriteLinks: []
        });
      } finally {
        setLoading(false);
      }
    };
    fetchDashboard();
  }, []);

  const formatBytes = (bytes) => {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', { 
      year: 'numeric', month: 'short', day: 'numeric' 
    });
  };

  const getGreeting = () => {
    const hour = new Date().getHours();
    if (hour < 12) return 'Good morning';
    if (hour < 17) return 'Good afternoon';
    return 'Good evening';
  };

  if (loading) {
    return (
      <div className="space-y-6">
        <div className="h-8 w-48 bg-slate-200 dark:bg-slate-700 rounded shimmer"></div>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          {[1,2,3,4].map(i => <div key={i} className="h-32 bg-slate-200 dark:bg-slate-700 rounded-2xl shimmer"></div>)}
        </div>
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <div className="h-96 bg-slate-200 dark:bg-slate-700 rounded-2xl shimmer"></div>
          <div className="h-96 bg-slate-200 dark:bg-slate-700 rounded-2xl shimmer"></div>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-8 animate-fade-in pb-10">
      <div>
        <h1 className="text-2xl md:text-3xl font-bold text-slate-900 dark:text-white tracking-tight">
          {getGreeting()}, {user?.fullName?.split(' ')[0] || 'there'}! 👋
        </h1>
        <p className="text-slate-500 dark:text-slate-400 mt-1">Here's an overview of your digital vault.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <StatCard 
          title="Total Documents" 
          value={data?.totalDocuments || 0} 
          icon={<FiFileText size={24} />} 
          gradientFrom="from-primary-500"
          gradientTo="to-primary-600"
          subtitle="Stored securely"
        />
        <StatCard 
          title="Saved Links" 
          value={data?.totalLinks || 0} 
          icon={<FiLink size={24} />} 
          gradientFrom="from-purple-500"
          gradientTo="to-purple-600"
          subtitle="Quick access"
        />
        <StatCard 
          title="Expiring Soon" 
          value={data?.expiringSoon || 0} 
          icon={<FiAlertCircle size={24} />} 
          gradientFrom="from-amber-500"
          gradientTo="to-orange-500"
          subtitle="Within 30 days"
        />
        <StatCard 
          title="Storage Used" 
          value={formatBytes(data?.storageUsed || 0)} 
          icon={<FiHardDrive size={24} />} 
          gradientFrom="from-emerald-500"
          gradientTo="to-teal-500"
          subtitle="Of unlimited capacity"
        />
      </div>

      {/* Quick Actions */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <Link to="/documents" className="card p-5 flex items-center gap-4 hover:border-primary-300 dark:hover:border-primary-700 group transition-all hover:shadow-md">
          <div className="p-3 rounded-xl bg-primary-50 dark:bg-primary-900/20 text-primary-600 dark:text-primary-400 group-hover:scale-110 transition-transform">
            <FiUploadCloud size={24} />
          </div>
          <div>
            <p className="font-semibold text-slate-900 dark:text-white">Upload Document</p>
            <p className="text-xs text-slate-500 dark:text-slate-400">Add a new file to your vault</p>
          </div>
        </Link>
        <Link to="/links" className="card p-5 flex items-center gap-4 hover:border-purple-300 dark:hover:border-purple-700 group transition-all hover:shadow-md">
          <div className="p-3 rounded-xl bg-purple-50 dark:bg-purple-900/20 text-purple-600 dark:text-purple-400 group-hover:scale-110 transition-transform">
            <FiPlus size={24} />
          </div>
          <div>
            <p className="font-semibold text-slate-900 dark:text-white">Save a Link</p>
            <p className="text-xs text-slate-500 dark:text-slate-400">Bookmark an important URL</p>
          </div>
        </Link>
        <Link to="/sharing" className="card p-5 flex items-center gap-4 hover:border-emerald-300 dark:hover:border-emerald-700 group transition-all hover:shadow-md">
          <div className="p-3 rounded-xl bg-emerald-50 dark:bg-emerald-900/20 text-emerald-600 dark:text-emerald-400 group-hover:scale-110 transition-transform">
            <FiShield size={24} />
          </div>
          <div>
            <p className="font-semibold text-slate-900 dark:text-white">Shared Files</p>
            <p className="text-xs text-slate-500 dark:text-slate-400">View shared documents</p>
          </div>
        </Link>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Recent Documents */}
        <div className="card flex flex-col">
          <div className="p-6 border-b border-slate-200 dark:border-slate-700 flex justify-between items-center">
            <h2 className="text-lg font-bold text-slate-900 dark:text-white flex items-center gap-2">
              <FiClock className="text-primary-500" /> Recent Uploads
            </h2>
            <Link to="/documents" className="text-sm font-medium text-primary-600 dark:text-primary-400 hover:text-primary-700 flex items-center gap-1 group">
              View All <FiArrowRight className="group-hover:translate-x-1 transition-transform" />
            </Link>
          </div>
          <div className="p-2 flex-grow">
            {data?.recentUploads?.length > 0 ? (
              <ul className="divide-y divide-slate-100 dark:divide-slate-700/50">
                {data.recentUploads.map((doc) => (
                  <li key={doc.id} className="p-4 hover:bg-slate-50 dark:hover:bg-slate-800/50 rounded-xl transition-colors flex items-center justify-between group">
                    <div className="flex items-center gap-4">
                      <div className="w-10 h-10 rounded-lg bg-primary-50 dark:bg-primary-900/20 text-primary-600 dark:text-primary-400 flex items-center justify-center">
                        <FiFileText size={20} />
                      </div>
                      <div>
                        <p className="font-medium text-slate-900 dark:text-white group-hover:text-primary-600 dark:group-hover:text-primary-400 transition-colors">{doc.title}</p>
                        <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">{doc.categoryName || 'Uncategorized'} • {formatDate(doc.createdAt)}</p>
                      </div>
                    </div>
                    <span className="text-xs font-medium px-2.5 py-1 bg-slate-100 dark:bg-slate-700 text-slate-600 dark:text-slate-300 rounded-md">
                      {formatBytes(doc.fileSize)}
                    </span>
                  </li>
                ))}
              </ul>
            ) : (
              <div className="flex flex-col items-center justify-center h-48 text-slate-400 text-center px-4">
                <FiFileText size={48} className="mb-3 opacity-20" />
                <p className="font-medium">No documents uploaded yet</p>
                <Link to="/documents" className="text-primary-500 text-sm mt-2 hover:underline font-medium">Upload your first document →</Link>
              </div>
            )}
          </div>
        </div>

        <div className="space-y-8">
          {/* Expiring Soon */}
          <div className="card flex flex-col">
            <div className="p-6 border-b border-slate-200 dark:border-slate-700">
              <h2 className="text-lg font-bold text-slate-900 dark:text-white flex items-center gap-2">
                <FiAlertCircle className="text-amber-500" /> Expiring Soon
              </h2>
            </div>
            <div className="p-2">
              {data?.expiringDocuments?.length > 0 ? (
                <ul className="divide-y divide-slate-100 dark:divide-slate-700/50">
                  {data.expiringDocuments.slice(0, 3).map((doc) => {
                    const isOverdue = new Date(doc.expiryDate) < new Date();
                    return (
                      <li key={doc.id} className="p-4 hover:bg-slate-50 dark:hover:bg-slate-800/50 rounded-xl transition-colors flex items-center justify-between">
                        <div className="flex items-center gap-3">
                           <div className={`w-2 h-2 rounded-full ${isOverdue ? 'bg-rose-500' : 'bg-amber-500'}`}></div>
                           <p className="font-medium text-slate-900 dark:text-white truncate max-w-[200px]">{doc.title}</p>
                        </div>
                        <span className={`text-xs font-semibold px-2.5 py-1 rounded-md ${isOverdue ? 'bg-rose-100 text-rose-700 dark:bg-rose-900/30 dark:text-rose-400' : 'bg-amber-100 text-amber-700 dark:bg-amber-900/30 dark:text-amber-400'}`}>
                          {formatDate(doc.expiryDate)}
                        </span>
                      </li>
                    )
                  })}
                </ul>
              ) : (
                <div className="flex flex-col items-center justify-center py-8 text-slate-400">
                  <FiShield size={32} className="mb-2 opacity-20" />
                  <p className="text-sm font-medium">All documents are up to date</p>
                </div>
              )}
            </div>
          </div>

          {/* Favorite Links */}
          <div className="card flex flex-col">
            <div className="p-6 border-b border-slate-200 dark:border-slate-700 flex justify-between items-center">
              <h2 className="text-lg font-bold text-slate-900 dark:text-white flex items-center gap-2">
                <FiStar className="text-amber-400" fill="currentColor" /> Quick Links
              </h2>
              <Link to="/links" className="text-sm font-medium text-primary-600 dark:text-primary-400 hover:text-primary-700 flex items-center gap-1 group">
                View All <FiArrowRight className="group-hover:translate-x-1 transition-transform" />
              </Link>
            </div>
            <div className="p-4 flex flex-wrap gap-3">
              {data?.favoriteLinks?.length > 0 ? (
                data.favoriteLinks.map((link) => (
                  <a 
                    key={link.id} 
                    href={link.url} 
                    target="_blank" 
                    rel="noopener noreferrer"
                    className="inline-flex items-center gap-2 px-4 py-2 bg-slate-50 hover:bg-primary-50 dark:bg-slate-700/50 dark:hover:bg-primary-900/30 border border-slate-200 dark:border-slate-600 rounded-lg text-sm font-medium text-slate-700 dark:text-slate-300 hover:text-primary-600 dark:hover:text-primary-400 transition-colors"
                  >
                    <FiLink className="opacity-50" />
                    {link.title}
                  </a>
                ))
              ) : (
                <div className="w-full text-center py-4">
                  <p className="text-sm text-slate-500">No favorite links added yet.</p>
                  <Link to="/links" className="text-primary-500 text-sm mt-1 hover:underline font-medium inline-block">Add your first link →</Link>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DashboardPage;
