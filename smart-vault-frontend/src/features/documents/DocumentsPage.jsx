import React, { useState, useEffect, useCallback } from 'react';
import { documentService } from '../../services/documentService';
import { FiUploadCloud, FiSearch, FiFilter, FiMoreVertical, FiEye, FiDownload, FiShare2, FiTrash2, FiFile, FiImage, FiClock, FiFileText, FiX } from 'react-icons/fi';
import toast from 'react-hot-toast';

const DocumentsPage = () => {
  const [documents, setDocuments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [isUploadModalOpen, setIsUploadModalOpen] = useState(false);
  const [uploadTitle, setUploadTitle] = useState('');
  const [selectedFile, setSelectedFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [activeMenu, setActiveMenu] = useState(null);

  useEffect(() => {
    loadDocuments();
  }, []);

  const loadDocuments = async () => {
    setLoading(true);
    try {
      const response = await documentService.getDocuments({ size: 50 });
      if (response.success) {
        setDocuments(response.data.content || []);
      }
    } catch (error) {
      console.error('Failed to load documents:', error);
      setDocuments([]);
    } finally {
      setLoading(false);
    }
  };

  const handleUpload = async () => {
    if (!selectedFile) {
      toast.error('Please select a file to upload');
      return;
    }
    if (!uploadTitle.trim()) {
      toast.error('Please enter a document title');
      return;
    }

    setUploading(true);
    try {
      const formData = new FormData();
      formData.append('file', selectedFile);
      formData.append('metadata', JSON.stringify({
        title: uploadTitle,
        description: '',
        folder: 'General'
      }));
      
      const response = await documentService.uploadDocument(formData);
      if (response.success) {
        toast.success('Document uploaded successfully!');
        setIsUploadModalOpen(false);
        setUploadTitle('');
        setSelectedFile(null);
        loadDocuments();
      } else {
        toast.error(response.message || 'Upload failed');
      }
    } catch (error) {
      toast.error('Failed to upload document. Please ensure the backend is running.');
    } finally {
      setUploading(false);
    }
  };

  const handleDelete = async (docId) => {
    if (!window.confirm('Are you sure you want to delete this document?')) return;
    try {
      const response = await documentService.deleteDocument(docId);
      if (response.success) {
        toast.success('Document deleted');
        setDocuments(prev => prev.filter(d => d.id !== docId));
      }
    } catch (error) {
      toast.error('Failed to delete document');
    }
    setActiveMenu(null);
  };

  const getFileIcon = (fileType) => {
    if (!fileType) return <FiFile size={24} className="text-slate-400" />;
    if (fileType.includes('pdf')) return <FiFileText size={24} className="text-rose-500" />;
    if (fileType.includes('image')) return <FiImage size={24} className="text-blue-500" />;
    if (fileType.includes('word') || fileType.includes('doc')) return <FiFileText size={24} className="text-blue-600" />;
    return <FiFile size={24} className="text-slate-500" />;
  };

  const formatBytes = (bytes) => {
    if (!bytes || bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  };

  const filteredDocs = documents.filter(d => 
    d.title?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="space-y-6 animate-fade-in pb-10">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl md:text-3xl font-bold text-slate-900 dark:text-white tracking-tight">My Documents</h1>
          <p className="text-slate-500 dark:text-slate-400 mt-1">
            Manage and securely store your files.
            {documents.length > 0 && <span className="ml-1">({documents.length} total)</span>}
          </p>
        </div>
        <button 
          id="upload-doc-btn"
          onClick={() => setIsUploadModalOpen(true)}
          className="btn-primary flex items-center justify-center gap-2 whitespace-nowrap"
        >
          <FiUploadCloud size={18} />
          Upload Document
        </button>
      </div>

      <div className="card p-4">
        <div className="flex flex-col md:flex-row gap-4 items-center justify-between">
          <div className="relative w-full md:max-w-md">
            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-slate-400">
              <FiSearch size={18} />
            </div>
            <input
              id="doc-search"
              type="text"
              placeholder="Search documents by name, tag, or OCR content..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="input-field pl-10 bg-slate-50 dark:bg-slate-800/50"
            />
          </div>
          <div className="flex gap-2 w-full md:w-auto overflow-x-auto pb-2 md:pb-0 hide-scrollbar">
            <button className="px-4 py-2 bg-slate-100 hover:bg-slate-200 dark:bg-slate-700 dark:hover:bg-slate-600 rounded-lg text-sm font-medium text-slate-700 dark:text-slate-200 flex items-center gap-2 whitespace-nowrap transition-colors">
              <FiFilter size={16} /> Filter by Category
            </button>
          </div>
        </div>
      </div>

      {loading ? (
        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
          {[1,2,3,4,5,6].map(i => <div key={i} className="h-48 bg-white dark:bg-slate-800 rounded-2xl shimmer border border-slate-100 dark:border-slate-700"></div>)}
        </div>
      ) : filteredDocs.length > 0 ? (
        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
          {filteredDocs.map((doc) => (
            <div key={doc.id} className="card p-5 group hover:border-primary-300 dark:hover:border-primary-700 transition-all hover:shadow-md">
              <div className="flex justify-between items-start mb-4">
                <div className="w-12 h-12 rounded-xl bg-slate-100 dark:bg-slate-700 flex items-center justify-center shadow-inner">
                  {getFileIcon(doc.fileType)}
                </div>
                <div className="relative">
                  <button 
                    onClick={() => setActiveMenu(activeMenu === doc.id ? null : doc.id)}
                    className="p-1.5 text-slate-400 hover:text-slate-700 dark:hover:text-slate-200 rounded-md hover:bg-slate-100 dark:hover:bg-slate-700 transition-colors opacity-0 group-hover:opacity-100"
                  >
                    <FiMoreVertical size={20} />
                  </button>
                  {activeMenu === doc.id && (
                    <>
                      <div className="fixed inset-0 z-10" onClick={() => setActiveMenu(null)}></div>
                      <div className="absolute right-0 mt-1 w-40 bg-white dark:bg-slate-800 rounded-xl shadow-lg border border-slate-200 dark:border-slate-700 z-20 py-1 animate-fade-in">
                        <button className="w-full text-left px-4 py-2 text-sm text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700 flex items-center gap-2">
                          <FiDownload size={14} /> Download
                        </button>
                        <button className="w-full text-left px-4 py-2 text-sm text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700 flex items-center gap-2">
                          <FiShare2 size={14} /> Share
                        </button>
                        <button 
                          onClick={() => handleDelete(doc.id)}
                          className="w-full text-left px-4 py-2 text-sm text-rose-600 dark:text-rose-400 hover:bg-rose-50 dark:hover:bg-rose-900/20 flex items-center gap-2"
                        >
                          <FiTrash2 size={14} /> Delete
                        </button>
                      </div>
                    </>
                  )}
                </div>
              </div>
              
              <h3 className="text-lg font-semibold text-slate-900 dark:text-white truncate mb-1" title={doc.title}>{doc.title}</h3>
              <p className="text-sm text-slate-500 dark:text-slate-400 mb-4">{doc.categoryName || 'General'} • {formatBytes(doc.fileSize)}</p>
              
              <div className="flex flex-wrap gap-2 mb-6">
                {doc.expiryDate && (
                  <span className={`text-xs font-medium px-2 py-1 rounded border flex items-center gap-1 ${new Date(doc.expiryDate) < new Date() ? 'border-rose-200 bg-rose-50 text-rose-700 dark:border-rose-900 dark:bg-rose-900/20 dark:text-rose-400' : 'border-amber-200 bg-amber-50 text-amber-700 dark:border-amber-900 dark:bg-amber-900/20 dark:text-amber-400'}`}>
                    <FiClock size={12} /> {new Date(doc.expiryDate).toLocaleDateString()}
                  </span>
                )}
                {doc.ocrStatus === 'COMPLETED' && (
                  <span className="text-xs font-medium px-2 py-1 rounded border border-emerald-200 bg-emerald-50 text-emerald-700 dark:border-emerald-900 dark:bg-emerald-900/20 dark:text-emerald-400">
                    Searchable
                  </span>
                )}
              </div>

              <div className="flex items-center gap-2 border-t border-slate-100 dark:border-slate-700/50 pt-4 mt-auto">
                <button className="flex-1 py-2 rounded-lg bg-slate-50 hover:bg-primary-50 text-slate-600 hover:text-primary-600 dark:bg-slate-700/50 dark:hover:bg-primary-900/30 dark:text-slate-300 dark:hover:text-primary-400 transition-colors flex items-center justify-center gap-2 text-sm font-medium">
                  <FiEye size={16} /> View
                </button>
                <button className="p-2 text-slate-400 hover:text-primary-600 dark:hover:text-primary-400 rounded-lg hover:bg-slate-50 dark:hover:bg-slate-700 transition-colors" title="Share">
                  <FiShare2 size={18} />
                </button>
                <button 
                  onClick={() => handleDelete(doc.id)}
                  className="p-2 text-slate-400 hover:text-rose-600 dark:hover:text-rose-400 rounded-lg hover:bg-slate-50 dark:hover:bg-slate-700 transition-colors" 
                  title="Delete"
                >
                  <FiTrash2 size={18} />
                </button>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="card p-12 flex flex-col items-center justify-center text-center">
          <div className="w-24 h-24 bg-gradient-to-br from-primary-50 to-purple-50 dark:from-primary-900/20 dark:to-purple-900/20 rounded-full flex items-center justify-center mb-6">
            <FiFileText size={40} className="text-primary-400" />
          </div>
          <h3 className="text-xl font-bold text-slate-900 dark:text-white mb-2">No documents found</h3>
          <p className="text-slate-500 max-w-sm mb-6">Upload your important documents to keep them secure, organized, and accessible anytime.</p>
          <button onClick={() => setIsUploadModalOpen(true)} className="btn-primary flex items-center gap-2">
            <FiUploadCloud size={18} /> Upload Document
          </button>
        </div>
      )}
      
      {/* Upload Modal */}
      {isUploadModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-sm animate-fade-in">
          <div className="bg-white dark:bg-slate-800 rounded-2xl shadow-2xl max-w-lg w-full p-6 relative animate-slide-up border border-slate-200 dark:border-slate-700">
             <button 
                onClick={() => { setIsUploadModalOpen(false); setSelectedFile(null); setUploadTitle(''); }}
                className="absolute top-4 right-4 p-2 text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 bg-slate-100 dark:bg-slate-700 rounded-full transition-colors"
             >
                <FiX size={18} />
             </button>
             <h2 className="text-2xl font-bold mb-6 text-slate-900 dark:text-white">Upload Document</h2>
             
             <div 
               className="border-2 border-dashed border-slate-300 dark:border-slate-600 rounded-xl p-8 text-center bg-slate-50 dark:bg-slate-800/50 mb-6 cursor-pointer hover:bg-slate-100 dark:hover:bg-slate-700 hover:border-primary-400 dark:hover:border-primary-600 transition-colors"
               onClick={() => document.getElementById('file-input').click()}
             >
                <input 
                  id="file-input"
                  type="file" 
                  className="hidden" 
                  accept=".pdf,.jpg,.jpeg,.png,.doc,.docx"
                  onChange={(e) => {
                    const file = e.target.files[0];
                    if (file) {
                      setSelectedFile(file);
                      if (!uploadTitle) setUploadTitle(file.name.replace(/\.[^/.]+$/, ''));
                    }
                  }}
                />
                <FiUploadCloud size={48} className="mx-auto text-primary-500 mb-4" />
                {selectedFile ? (
                  <div>
                    <p className="text-slate-900 dark:text-white font-semibold">{selectedFile.name}</p>
                    <p className="text-xs text-slate-500 mt-1">{formatBytes(selectedFile.size)}</p>
                  </div>
                ) : (
                  <>
                    <p className="text-slate-600 dark:text-slate-300 font-medium">Click or drag file to this area to upload</p>
                    <p className="text-xs text-slate-400 mt-2">Support for PDF, JPG, PNG, DOC files. Max 10MB.</p>
                  </>
                )}
             </div>
             
             <div className="space-y-4">
                <div>
                   <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">Document Title *</label>
                   <input 
                     type="text" 
                     className="input-field py-2" 
                     placeholder="e.g., Passport Copy" 
                     value={uploadTitle}
                     onChange={(e) => setUploadTitle(e.target.value)}
                   />
                </div>
                <div className="flex justify-end gap-3 mt-6">
                   <button 
                     onClick={() => { setIsUploadModalOpen(false); setSelectedFile(null); setUploadTitle(''); }} 
                     className="btn-secondary py-2"
                   >
                     Cancel
                   </button>
                   <button 
                     onClick={handleUpload} 
                     disabled={uploading}
                     className="btn-primary py-2 flex items-center gap-2"
                   >
                     {uploading ? (
                       <>
                         <div className="w-4 h-4 border-2 border-white/30 border-t-white rounded-full animate-spin"></div>
                         Uploading...
                       </>
                     ) : (
                       <>
                         <FiUploadCloud size={16} />
                         Upload File
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

export default DocumentsPage;
