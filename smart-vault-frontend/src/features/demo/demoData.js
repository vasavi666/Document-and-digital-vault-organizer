export const demoData = {
  user: { 
    id: 1, 
    email: 'demo@smartvault.com', 
    fullName: 'Alex Johnson', 
    role: 'USER', 
    avatarUrl: null 
  },
  dashboard: {
    totalDocuments: 20,
    totalLinks: 10,
    expiringSoon: 5,
    storageUsed: 156000000,
    recentUploads: [
      { id: 20, title: 'Birth Certificate', categoryName: 'Identity', fileType: 'application/pdf', fileSize: 380000, createdAt: new Date(Date.now() - 2 * 86400000).toISOString() },
      { id: 19, title: 'Tax Return ITR - FY 2023-24', categoryName: 'Tax', fileType: 'application/pdf', fileSize: 890000, createdAt: new Date(Date.now() - 5 * 86400000).toISOString() },
      { id: 18, title: 'Warranty Card - MacBook Pro', categoryName: 'Warranty', fileType: 'application/pdf', fileSize: 540000, createdAt: new Date(Date.now() - 8 * 86400000).toISOString() },
      { id: 17, title: 'Resume - Alex Johnson', categoryName: 'Professional', fileType: 'application/pdf', fileSize: 156000, createdAt: new Date(Date.now() - 10 * 86400000).toISOString() },
      { id: 16, title: 'Electricity Bill - June 2024', categoryName: 'Bills', fileType: 'application/pdf', fileSize: 210000, createdAt: new Date(Date.now() - 15 * 86400000).toISOString() }
    ],
    expiringDocuments: [
      { id: 6, title: 'Vehicle Insurance - Honda City', categoryName: 'Insurance', fileType: 'application/pdf', expiryDate: new Date(Date.now() + 7 * 86400000).toISOString().split('T')[0] },
      { id: 3, title: 'Passport - Alex Johnson', categoryName: 'Identity', fileType: 'application/pdf', expiryDate: new Date(Date.now() + 10 * 86400000).toISOString().split('T')[0] },
      { id: 14, title: 'Apartment Rental Agreement', categoryName: 'Property', fileType: 'application/pdf', expiryDate: new Date(Date.now() + 14 * 86400000).toISOString().split('T')[0] },
      { id: 4, title: 'Driving License', categoryName: 'Identity', fileType: 'application/pdf', expiryDate: new Date(Date.now() + 25 * 86400000).toISOString().split('T')[0] },
      { id: 5, title: 'Health Insurance Policy', categoryName: 'Insurance', fileType: 'application/pdf', expiryDate: new Date(Date.now() + 45 * 86400000).toISOString().split('T')[0] }
    ],
    favoriteLinks: [
      { id: 1, title: 'GitHub Profile', url: 'https://github.com/alexjohnson', isFavorite: true },
      { id: 2, title: 'LinkedIn Profile', url: 'https://linkedin.com/in/alexjohnson', isFavorite: true },
      { id: 3, title: 'Portfolio Website', url: 'https://alexjohnson.dev', isFavorite: true },
      { id: 6, title: 'Resume on Google Drive', url: 'https://drive.google.com/file/d/abc123/view', isFavorite: true }
    ],
    categoryBreakdown: [
      { category: 'Identity', count: 5 },
      { category: 'Education', count: 4 },
      { category: 'Insurance', count: 2 },
      { category: 'Medical', count: 3 },
      { category: 'Property', count: 2 },
      { category: 'Professional', count: 1 },
      { category: 'Bills', count: 1 },
      { category: 'Warranty', count: 1 },
      { category: 'Tax', count: 1 }
    ]
  },
  documents: [], // Will populate in a real scenario
  links: [],
  shares: [],
  notifications: []
};
