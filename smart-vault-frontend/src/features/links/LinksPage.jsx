import React, { useState, useEffect } from 'react';
import { linkService } from '../../services/linkService';
import { FiPlus, FiLink, FiSearch, FiExternalLink, FiStar, FiMoreVertical, FiCopy, FiTrash2, FiEdit2, FiX } from 'react-icons/fi';
import toast from 'react-hot-toast';

const LinksPage = () => {
  const [links, setLinks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [newLinkTitle, setNewLinkTitle] = useState('');
  const [newLinkUrl, setNewLinkUrl] = useState('');
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    loadLinks();
  }, []);

  const loadLinks = async () => {
    setLoading(true);
    try {
      const response = await linkService.getLinks({ size: 50 });
      if (response.success) {
        setLinks(response.data.content || []);
      }
    } catch (error) {
      console.error('Failed to load links:', error);
      setLinks([]);
    } finally {
      setLoading(false);
    }
  };

  const handleAddLink = async () => {
    if (!newLinkTitle.trim() || !newLinkUrl.trim()) {
      toast.error('Please fill in both title and URL');
      return;
    }

    setSaving(true);
    try {
      const response = await linkService.createLink({ title: newLinkTitle, url: newLinkUrl });
      if (response.success) {
        toast.success('Link saved successfully!');
        setIsAddModalOpen(false);
        setNewLinkTitle('');
        setNewLinkUrl('');
        loadLinks();
      } else {
        toast.error(response.message || 'Failed to save link');
      }
    } catch (error) {
      toast.error('Failed to save link. Please ensure the backend is running.');
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (linkId) => {
    if (!window.confirm('Are you sure you want to delete this link?')) return;
    try {
      const response = await linkService.deleteLink(linkId);
      if (response.success) {
        toast.success('Link deleted');
        setLinks(prev => prev.filter(l => l.id !== linkId));
      }
    } catch (error) {
      toast.error('Failed to delete link');
    }
  };

  const handleToggleFavorite = async (linkId) => {
    try {
      const response = await linkService.toggleFavorite(linkId);
      if (response.success) {
        setLinks(prev => prev.map(l => l.id === linkId ? { ...l, isFavorite: !l.isFavorite } : l));
      }
    } catch (error) {
      toast.error('Failed to update link');
    }
  };

  const copyToClipboard = (url) => {
    navigator.clipboard.writeText(url);
    toast.success('Link copied to clipboard');
  };

  const filteredLinks = links.filter(l => 
    l.title?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    l.url?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="space-y-6 animate-fade-in pb-10">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl md:text-3xl font-bold text-slate-900 dark:text-white tracking-tight">Important Links</h1>
          <p className="text-slate-500 dark:text-slate-400 mt-1">
            Keep track of your digital assets across the web.
            {links.length > 0 && <span className="ml-1">({links.length} saved)</span>}
          </p>
        </div>
        <button 
          id="add-link-btn"
          onClick={() => setIsAddModalOpen(true)}
          className="btn-primary flex items-center justify-center gap-2 whitespace-nowrap"
        >
          <FiPlus size={18} />
          Add New Link
        </button>
      </div>

      <div className="card p-4">
        <div className="relative w-full md:max-w-md">
          <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-slate-400">
            <FiSearch size={18} />
          </div>
          <input
            id="link-search"
            type="text"
            placeholder="Search your links..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="input-field pl-10 bg-slate-50 dark:bg-slate-800/50"
          />
        </div>
      </div>

      {loading ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {[1,2,3].map(i => <div key={i} className="h-32 bg-white dark:bg-slate-800 rounded-2xl shimmer border border-slate-100 dark:border-slate-700"></div>)}
        </div>
      ) : filteredLinks.length > 0 ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredLinks.map((link) => (
            <div key={link.id} className="card p-5 group hover:border-purple-300 dark:hover:border-purple-700 transition-all hover:shadow-md flex flex-col">
              <div className="flex justify-between items-start mb-3">
                <div className="flex items-center gap-3 min-w-0">
                  <div className="w-10 h-10 rounded-xl bg-purple-50 dark:bg-purple-900/20 text-purple-600 dark:text-purple-400 flex items-center justify-center flex-shrink-0">
                    <FiLink size={20} />
                  </div>
                  <h3 className="font-semibold text-slate-900 dark:text-white truncate" title={link.title}>{link.title}</h3>
                </div>
                <button 
                  onClick={() => handleToggleFavorite(link.id)}
                  className="text-amber-400 hover:text-amber-500 transition-colors flex-shrink-0"
                >
                  <FiStar size={20} fill={link.isFavorite ? "currentColor" : "none"} />
                </button>
              </div>
              
              <a href={link.url} target="_blank" rel="noopener noreferrer" className="text-sm text-slate-500 dark:text-slate-400 hover:text-purple-600 dark:hover:text-purple-400 flex items-center gap-1 truncate mb-4 transition-colors">
                {link.url}
              </a>

              <div className="flex items-center gap-2 border-t border-slate-100 dark:border-slate-700/50 pt-4 mt-auto">
                <a href={link.url} target="_blank" rel="noopener noreferrer" className="flex-1 py-2 rounded-lg bg-slate-50 hover:bg-purple-50 text-slate-600 hover:text-purple-600 dark:bg-slate-700/50 dark:hover:bg-purple-900/30 dark:text-slate-300 dark:hover:text-purple-400 transition-colors flex items-center justify-center gap-2 text-sm font-medium">
                  <FiExternalLink size={16} /> Open
                </a>
                <div className="flex gap-1">
                  <button onClick={() => copyToClipboard(link.url)} className="p-2 text-slate-400 hover:text-primary-600 dark:hover:text-primary-400 rounded-lg hover:bg-slate-50 dark:hover:bg-slate-700 transition-colors" title="Copy URL">
                    <FiCopy size={18} />
                  </button>
                  <button onClick={() => handleDelete(link.id)} className="p-2 text-slate-400 hover:text-rose-600 dark:hover:text-rose-400 rounded-lg hover:bg-slate-50 dark:hover:bg-slate-700 transition-colors" title="Delete">
                    <FiTrash2 size={18} />
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="card p-12 flex flex-col items-center justify-center text-center">
          <div className="w-24 h-24 bg-gradient-to-br from-purple-50 to-primary-50 dark:from-purple-900/20 dark:to-primary-900/20 rounded-full flex items-center justify-center mb-6">
            <FiLink size={40} className="text-purple-400" />
          </div>
          <h3 className="text-xl font-bold text-slate-900 dark:text-white mb-2">No links found</h3>
          <p className="text-slate-500 max-w-sm mb-6">Save important URLs to keep them organized and accessible anytime, anywhere.</p>
          <button onClick={() => setIsAddModalOpen(true)} className="btn-primary flex items-center gap-2">
            <FiPlus size={18} /> Add Link
          </button>
        </div>
      )}

      {/* Add Link Modal */}
      {isAddModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-sm animate-fade-in">
          <div className="bg-white dark:bg-slate-800 rounded-2xl shadow-2xl max-w-md w-full p-6 relative animate-slide-up border border-slate-200 dark:border-slate-700">
            <button 
              onClick={() => { setIsAddModalOpen(false); setNewLinkTitle(''); setNewLinkUrl(''); }}
              className="absolute top-4 right-4 p-2 text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 bg-slate-100 dark:bg-slate-700 rounded-full transition-colors"
            >
              <FiX size={18} />
            </button>
            <h2 className="text-2xl font-bold mb-6 text-slate-900 dark:text-white flex items-center gap-3">
              <div className="p-2 rounded-xl bg-purple-50 dark:bg-purple-900/20 text-purple-600 dark:text-purple-400">
                <FiLink size={24} />
              </div>
              Add New Link
            </h2>
            
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">Title *</label>
                <input 
                  type="text" 
                  className="input-field py-2" 
                  placeholder="e.g., LinkedIn Profile"
                  value={newLinkTitle}
                  onChange={(e) => setNewLinkTitle(e.target.value)}
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">URL *</label>
                <input 
                  type="url" 
                  className="input-field py-2" 
                  placeholder="https://example.com"
                  value={newLinkUrl}
                  onChange={(e) => setNewLinkUrl(e.target.value)}
                />
              </div>
              <div className="flex justify-end gap-3 mt-6">
                <button 
                  onClick={() => { setIsAddModalOpen(false); setNewLinkTitle(''); setNewLinkUrl(''); }}
                  className="btn-secondary py-2"
                >
                  Cancel
                </button>
                <button 
                  onClick={handleAddLink} 
                  disabled={saving}
                  className="btn-primary py-2 flex items-center gap-2"
                >
                  {saving ? (
                    <>
                      <div className="w-4 h-4 border-2 border-white/30 border-t-white rounded-full animate-spin"></div>
                      Saving...
                    </>
                  ) : (
                    <>
                      <FiPlus size={16} />
                      Save Link
                    </>
                  )}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default LinksPage;
