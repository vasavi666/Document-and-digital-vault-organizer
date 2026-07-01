-- Smart Vault - Seed Data
USE smart_vault;

-- ============================================================
-- DOCUMENT CATEGORIES (System defaults)
-- ============================================================
INSERT INTO document_categories (name, icon, description, is_system) VALUES
('Aadhaar', 'HiIdentification', 'Aadhaar Card / UID', TRUE),
('PAN Card', 'HiCreditCard', 'Permanent Account Number Card', TRUE),
('Passport', 'HiGlobeAlt', 'International Passport', TRUE),
('Driving License', 'HiTruck', 'Driving License / Permit', TRUE),
('Insurance', 'HiShieldCheck', 'Insurance Policies & Documents', TRUE),
('Educational Certificates', 'HiAcademicCap', 'Degrees, Marksheets, Certificates', TRUE),
('Medical Records', 'HiHeart', 'Medical Reports & Prescriptions', TRUE),
('Property Documents', 'HiHome', 'Property Deeds & Agreements', TRUE),
('Other Documents', 'HiDocumentText', 'Miscellaneous Documents', TRUE);

-- ============================================================
-- USERS (BCrypt hashed passwords)
-- admin123 => $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
-- demo123  => $2a$10$dXJ3SW6G7P50lGmMQgel6uB0hLCp7tJaO0K4iFfJTfG5t7hj0z.Nm
-- ============================================================
INSERT INTO users (email, password, full_name, role, is_active, email_verified) VALUES
('admin@smartvault.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'System Admin', 'ADMIN', TRUE, TRUE),
('demo@smartvault.com', '$2a$10$dXJ3SW6G7P50lGmMQgel6uB0hLCp7tJaO0K4iFfJTfG5t7hj0z.Nm', 'Alex Johnson', 'USER', TRUE, TRUE);

-- ============================================================
-- SAMPLE DOCUMENTS (for demo user, id=2)
-- ============================================================
INSERT INTO documents (user_id, title, description, category_id, file_url, file_name, file_type, file_size, folder, tags, expiry_date, renewal_date, is_favorite, created_at) VALUES
(2, 'Aadhaar Card - Alex Johnson', 'Government issued unique identification', 1, 'https://res.cloudinary.com/demo/raw/upload/sample_aadhaar.pdf', 'aadhaar_card.pdf', 'application/pdf', 524288, 'Identity', 'aadhaar,uid,identity,government', NULL, NULL, TRUE, DATE_SUB(NOW(), INTERVAL 90 DAY)),
(2, 'PAN Card', 'Income tax permanent account number', 2, 'https://res.cloudinary.com/demo/raw/upload/sample_pan.pdf', 'pan_card.pdf', 'application/pdf', 312000, 'Identity', 'pan,tax,income', NULL, NULL, TRUE, DATE_SUB(NOW(), INTERVAL 85 DAY)),
(2, 'Passport - Alex Johnson', 'International travel passport', 3, 'https://res.cloudinary.com/demo/raw/upload/sample_passport.pdf', 'passport.pdf', 'application/pdf', 1048576, 'Identity', 'passport,travel,international', DATE_ADD(NOW(), INTERVAL 10 DAY), DATE_ADD(NOW(), INTERVAL 5 DAY), TRUE, DATE_SUB(NOW(), INTERVAL 80 DAY)),
(2, 'Driving License', 'Two-wheeler and four-wheeler license', 4, 'https://res.cloudinary.com/demo/raw/upload/sample_dl.pdf', 'driving_license.pdf', 'application/pdf', 425000, 'Identity', 'license,driving,vehicle', DATE_ADD(NOW(), INTERVAL 25 DAY), DATE_ADD(NOW(), INTERVAL 20 DAY), FALSE, DATE_SUB(NOW(), INTERVAL 75 DAY)),
(2, 'Health Insurance Policy', 'Family health insurance - Star Health', 5, 'https://res.cloudinary.com/demo/raw/upload/sample_insurance.pdf', 'health_insurance.pdf', 'application/pdf', 2097152, 'Insurance', 'insurance,health,star,family', DATE_ADD(NOW(), INTERVAL 45 DAY), DATE_ADD(NOW(), INTERVAL 30 DAY), TRUE, DATE_SUB(NOW(), INTERVAL 60 DAY)),
(2, 'Vehicle Insurance - Honda City', 'Comprehensive car insurance policy', 5, 'https://res.cloudinary.com/demo/raw/upload/sample_car_insurance.pdf', 'car_insurance.pdf', 'application/pdf', 1572864, 'Insurance', 'insurance,vehicle,car,honda', DATE_ADD(NOW(), INTERVAL 7 DAY), DATE_ADD(NOW(), INTERVAL 3 DAY), FALSE, DATE_SUB(NOW(), INTERVAL 55 DAY)),
(2, 'B.Tech Degree Certificate', 'Bachelor of Technology in Computer Science', 6, 'https://res.cloudinary.com/demo/raw/upload/sample_degree.pdf', 'btech_degree.pdf', 'application/pdf', 3145728, 'Education', 'degree,btech,computer science,university', NULL, NULL, TRUE, DATE_SUB(NOW(), INTERVAL 50 DAY)),
(2, '12th Board Marksheet', 'CBSE Class 12 Board Examination Marksheet', 6, 'https://res.cloudinary.com/demo/raw/upload/sample_marksheet.pdf', '12th_marksheet.pdf', 'application/pdf', 1048576, 'Education', 'marksheet,cbse,12th,board', NULL, NULL, FALSE, DATE_SUB(NOW(), INTERVAL 48 DAY)),
(2, '10th Board Certificate', 'CBSE Class 10 Board Certificate', 6, 'https://res.cloudinary.com/demo/raw/upload/sample_10th.pdf', '10th_certificate.pdf', 'application/pdf', 890000, 'Education', 'certificate,cbse,10th', NULL, NULL, FALSE, DATE_SUB(NOW(), INTERVAL 45 DAY)),
(2, 'AWS Cloud Practitioner Certificate', 'AWS Certified Cloud Practitioner', 6, 'https://res.cloudinary.com/demo/image/upload/sample_aws_cert.png', 'aws_certificate.png', 'image/png', 2400000, 'Education', 'aws,cloud,certificate,professional', DATE_ADD(NOW(), INTERVAL 365 DAY), NULL, TRUE, DATE_SUB(NOW(), INTERVAL 40 DAY)),
(2, 'COVID Vaccination Certificate', 'Both doses completed - CoWIN', 7, 'https://res.cloudinary.com/demo/raw/upload/sample_vaccine.pdf', 'vaccine_certificate.pdf', 'application/pdf', 450000, 'Medical', 'covid,vaccine,cowin,health', NULL, NULL, FALSE, DATE_SUB(NOW(), INTERVAL 35 DAY)),
(2, 'Blood Test Report - Annual Checkup', 'Annual health checkup blood work results', 7, 'https://res.cloudinary.com/demo/raw/upload/sample_blood_test.pdf', 'blood_test_report.pdf', 'application/pdf', 678000, 'Medical', 'blood test,health,checkup,annual', NULL, NULL, FALSE, DATE_SUB(NOW(), INTERVAL 30 DAY)),
(2, 'Eye Prescription', 'Latest eye checkup prescription', 7, 'https://res.cloudinary.com/demo/image/upload/sample_eye_rx.jpg', 'eye_prescription.jpg', 'image/jpeg', 1200000, 'Medical', 'eye,prescription,glasses,vision', NULL, NULL, FALSE, DATE_SUB(NOW(), INTERVAL 28 DAY)),
(2, 'Apartment Rental Agreement', 'Current apartment lease agreement', 8, 'https://res.cloudinary.com/demo/raw/upload/sample_rental.pdf', 'rental_agreement.pdf', 'application/pdf', 4194304, 'Property', 'rental,apartment,lease,agreement', DATE_ADD(NOW(), INTERVAL 14 DAY), DATE_ADD(NOW(), INTERVAL 7 DAY), TRUE, DATE_SUB(NOW(), INTERVAL 25 DAY)),
(2, 'Home Loan EMI Statement', 'Monthly EMI statement from SBI', 8, 'https://res.cloudinary.com/demo/raw/upload/sample_emi.pdf', 'emi_statement.pdf', 'application/pdf', 320000, 'Property', 'loan,emi,sbi,home', NULL, NULL, FALSE, DATE_SUB(NOW(), INTERVAL 20 DAY)),
(2, 'Electricity Bill - June 2024', 'Monthly electricity bill', 9, 'https://res.cloudinary.com/demo/raw/upload/sample_bill.pdf', 'electricity_bill.pdf', 'application/pdf', 210000, 'Bills', 'bill,electricity,utility', NULL, NULL, FALSE, DATE_SUB(NOW(), INTERVAL 15 DAY)),
(2, 'Resume - Alex Johnson', 'Latest updated professional resume', 9, 'https://res.cloudinary.com/demo/raw/upload/sample_resume.pdf', 'resume_alex.pdf', 'application/pdf', 156000, 'Professional', 'resume,cv,job,professional', NULL, NULL, TRUE, DATE_SUB(NOW(), INTERVAL 10 DAY)),
(2, 'Warranty Card - MacBook Pro', 'Apple MacBook Pro warranty document', 9, 'https://res.cloudinary.com/demo/raw/upload/sample_warranty.pdf', 'macbook_warranty.pdf', 'application/pdf', 540000, 'Warranty', 'warranty,apple,macbook,laptop', DATE_ADD(NOW(), INTERVAL 60 DAY), NULL, FALSE, DATE_SUB(NOW(), INTERVAL 8 DAY)),
(2, 'Tax Return ITR - FY 2023-24', 'Income tax return filed for FY 2023-24', 9, 'https://res.cloudinary.com/demo/raw/upload/sample_itr.pdf', 'itr_2024.pdf', 'application/pdf', 890000, 'Tax', 'itr,tax,income,return,filing', NULL, NULL, FALSE, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(2, 'Birth Certificate', 'Official birth certificate', 9, 'https://res.cloudinary.com/demo/raw/upload/sample_birth.pdf', 'birth_certificate.pdf', 'application/pdf', 380000, 'Identity', 'birth,certificate,official', NULL, NULL, FALSE, DATE_SUB(NOW(), INTERVAL 2 DAY));

-- ============================================================
-- OCR DATA (for some documents)
-- ============================================================
INSERT INTO ocr_data (document_id, extracted_text, status, processed_at) VALUES
(1, 'GOVERNMENT OF INDIA\nUnique Identification Authority of India\nAadhaar Number: XXXX XXXX 4532\nName: Alex Johnson\nDOB: 15/03/1998\nGender: Male\nAddress: 42 MG Road, Bangalore, Karnataka 560001', 'COMPLETED', DATE_SUB(NOW(), INTERVAL 89 DAY)),
(2, 'INCOME TAX DEPARTMENT\nGOVERNMENT OF INDIA\nPermanent Account Number Card\nPAN: ABCPJ1234K\nName: ALEX JOHNSON\nFather Name: ROBERT JOHNSON\nDate of Birth: 15/03/1998', 'COMPLETED', DATE_SUB(NOW(), INTERVAL 84 DAY)),
(3, 'REPUBLIC OF INDIA\nPASSPORT\nSurname: JOHNSON\nGiven Names: ALEX\nNationality: INDIAN\nDate of Birth: 15 MAR 1998\nPlace of Birth: BANGALORE\nPassport No: T1234567\nDate of Issue: 20 JAN 2020\nDate of Expiry: 19 JAN 2030', 'COMPLETED', DATE_SUB(NOW(), INTERVAL 79 DAY)),
(7, 'UNIVERSITY OF TECHNOLOGY\nDEGREE CERTIFICATE\nThis is to certify that\nALEX JOHNSON\nhas been conferred the degree of\nBACHELOR OF TECHNOLOGY\nin\nCOMPUTER SCIENCE AND ENGINEERING\nwith First Class Honours\nCGPA: 8.7/10\nDate: June 2020', 'COMPLETED', DATE_SUB(NOW(), INTERVAL 49 DAY));

-- ============================================================
-- IMPORTANT LINKS (for demo user, id=2)
-- ============================================================
INSERT INTO important_links (user_id, title, url, category, subcategory, description, notes, is_favorite) VALUES
(2, 'GitHub Profile', 'https://github.com/alexjohnson', 'Professional', 'GitHub', 'Personal GitHub profile with open source projects', 'Update pinned repos before interviews', TRUE),
(2, 'LinkedIn Profile', 'https://linkedin.com/in/alexjohnson', 'Professional', 'LinkedIn', 'Professional networking profile', 'Keep updated with latest experience', TRUE),
(2, 'Portfolio Website', 'https://alexjohnson.dev', 'Professional', 'Portfolio', 'Personal portfolio website showcasing projects', 'Redesign landing page', TRUE),
(2, 'LeetCode Profile', 'https://leetcode.com/alexjohnson', 'Professional', 'LeetCode', 'Competitive programming profile - 400+ problems solved', 'Target: 500 problems by end of month', FALSE),
(2, 'HackerRank Profile', 'https://hackerrank.com/alexjohnson', 'Professional', 'HackerRank', 'HackerRank coding challenges profile', '5-star in Problem Solving', FALSE),
(2, 'Resume on Google Drive', 'https://drive.google.com/file/d/abc123/view', 'Professional', 'Resume Link', 'Latest resume PDF on Google Drive', 'Updated June 2024', TRUE),
(2, 'SBI Net Banking', 'https://retail.onlinesbi.sbi', 'Personal', 'Banking Portals', 'State Bank of India internet banking', 'Primary savings account', FALSE),
(2, 'HDFC Credit Card Portal', 'https://netbanking.hdfcbank.com', 'Personal', 'Banking Portals', 'HDFC Bank credit card management', 'Bill due date: 15th of every month', FALSE),
(2, 'Income Tax e-Filing', 'https://eportal.incometax.gov.in', 'Personal', 'Government Websites', 'Income tax e-filing portal', 'ITR filing deadline: July 31', FALSE),
(2, 'DigiLocker', 'https://digilocker.gov.in', 'Personal', 'Government Websites', 'Digital document storage by Government of India', 'Linked with Aadhaar', FALSE);

-- ============================================================
-- SHARED LINKS (sample shares)
-- ============================================================
INSERT INTO shared_links (document_id, shared_by, share_token, expires_at, is_revoked, access_count) VALUES
(17, 2, 'sv-share-abc123def456', DATE_ADD(NOW(), INTERVAL 7 DAY), FALSE, 5),
(7, 2, 'sv-share-xyz789ghi012', DATE_ADD(NOW(), INTERVAL 30 DAY), FALSE, 12),
(3, 2, 'sv-share-mno345pqr678', DATE_SUB(NOW(), INTERVAL 2 DAY), FALSE, 3);

-- ============================================================
-- NOTIFICATIONS
-- ============================================================
INSERT INTO notifications (user_id, type, title, message, is_read, created_at) VALUES
(2, 'WELCOME', 'Welcome to Smart Vault!', 'Thank you for joining Smart Vault. Start by uploading your first document or saving an important link.', TRUE, DATE_SUB(NOW(), INTERVAL 90 DAY)),
(2, 'EXPIRY_WARNING', 'Passport Expiring Soon', 'Your document "Passport - Alex Johnson" will expire in 10 days. Consider renewing it soon.', FALSE, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(2, 'EXPIRY_WARNING', 'Car Insurance Expiring', 'Your document "Vehicle Insurance - Honda City" will expire in 7 days. Renew immediately to avoid lapse.', FALSE, NOW()),
(2, 'SHARE_NOTIFICATION', 'Document Shared Successfully', 'Your document "Resume - Alex Johnson" has been shared. Share link has been accessed 5 times.', TRUE, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(2, 'EXPIRY_WARNING', 'Rental Agreement Expiring', 'Your document "Apartment Rental Agreement" will expire in 14 days. Contact your landlord for renewal.', FALSE, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(2, 'SYSTEM', 'Storage Tip', 'You have used 15% of your storage. Organize your documents into folders for better management.', TRUE, DATE_SUB(NOW(), INTERVAL 10 DAY));

-- ============================================================
-- AUDIT LOGS
-- ============================================================
INSERT INTO audit_logs (user_id, action, entity_type, entity_id, details, ip_address, created_at) VALUES
(2, 'LOGIN', 'USER', 2, 'User logged in successfully', '192.168.1.100', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(2, 'UPLOAD_DOCUMENT', 'DOCUMENT', 20, 'Uploaded document: Birth Certificate', '192.168.1.100', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(2, 'SHARE_DOCUMENT', 'SHARED_LINK', 1, 'Shared document: Resume - Alex Johnson', '192.168.1.100', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(2, 'UPDATE_DOCUMENT', 'DOCUMENT', 17, 'Updated document metadata: Resume - Alex Johnson', '192.168.1.100', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(1, 'LOGIN', 'USER', 1, 'Admin logged in successfully', '192.168.1.1', DATE_SUB(NOW(), INTERVAL 6 HOUR)),
(2, 'CREATE_LINK', 'LINK', 10, 'Created link: DigiLocker', '192.168.1.100', DATE_SUB(NOW(), INTERVAL 7 DAY)),
(2, 'DELETE_DOCUMENT', 'DOCUMENT', NULL, 'Deleted expired utility bill', '192.168.1.100', DATE_SUB(NOW(), INTERVAL 10 DAY));
