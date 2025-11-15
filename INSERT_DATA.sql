
USE healthcamp_db;


SHOW TABLES;


INSERT INTO patients (name, age, gender, phone, symptoms, diagnosis, treatment, visit_date) VALUES
('Rajesh Kumar', 45, 'Male', '9876543210', 'High fever, body ache, headache', 'Viral fever', 'Paracetamol 500mg (3x daily), Rest, Plenty of fluids', '2025-10-25'),
('Priya Sharma', 32, 'Female', '9876543211', 'Persistent cough, cold, sore throat', 'Upper respiratory infection', 'Cough syrup, Antibiotics, Warm water gargle', '2025-10-25'),
('Amit Patel', 28, 'Male', '9876543212', 'Severe headache, nausea', 'Migraine', 'Pain reliever, Rest in dark room, Avoid screens', '2025-10-25'),
('Sunita Devi', 56, 'Female', '9876543213', 'Joint pain, swelling in knees', 'Arthritis', 'Anti-inflammatory drugs, Light exercise, Hot compress', '2025-10-24'),
('Ramesh Singh', 38, 'Male', '9876543214', 'Stomach pain, indigestion', 'Gastritis', 'Antacid, Light diet, Avoid spicy food', '2025-10-24'),
('Anjali Mehta', 24, 'Female', '9876543215', 'Skin rash, itching', 'Allergic reaction', 'Antihistamine, Calamine lotion, Avoid allergens', '2025-10-24'),
('Vikram Reddy', 51, 'Male', '9876543216', 'Chest congestion, breathing difficulty', 'Bronchitis', 'Inhaler, Steam inhalation, Antibiotics', '2025-10-23'),
('Kavita Joshi', 29, 'Female', '9876543217', 'Fatigue, dizziness, weakness', 'Anemia', 'Iron supplements, Nutritious diet, Rest', '2025-10-23'),
('Suresh Gupta', 62, 'Male', '9876543218', 'High blood pressure, chest discomfort', 'Hypertension', 'BP medication, Low salt diet, Regular monitoring', '2025-10-22'),
('Meena Rao', 35, 'Female', '9876543219', 'Ear pain, hearing difficulty', 'Ear infection', 'Ear drops, Antibiotics, Keep ear dry', '2025-10-22'),
('Arun Kumar', 19, 'Male', '9876543220', 'Fever, muscle pain, weakness', 'Dengue suspected', 'Referred to hospital, Blood test, Plenty of fluids', '2025-10-21'),
('Deepa Singh', 41, 'Female', '9876543221', 'Eye redness, itching, watering', 'Conjunctivitis', 'Eye drops, Clean with warm water, Avoid touching', '2025-10-21');


SELECT COUNT(*) as total_records FROM patients;


SELECT * FROM patients ORDER BY visit_date DESC, id DESC;
