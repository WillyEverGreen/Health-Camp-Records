-- Health Camp Records Database Setup Script
-- Run this in MySQL to create the database

CREATE DATABASE IF NOT EXISTS healthcamp_db;
USE healthcamp_db;

CREATE TABLE IF NOT EXISTS patients (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10),
    phone VARCHAR(15),
    symptoms TEXT,
    diagnosis VARCHAR(200),
    treatment TEXT,
    visit_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sample data (optional)
INSERT INTO patients (name, age, gender, phone, symptoms, diagnosis, treatment, visit_date) VALUES
('John Doe', 45, 'Male', '9876543210', 'Fever, headache', 'Viral fever', 'Paracetamol 500mg, Rest', CURDATE()),
('Jane Smith', 32, 'Female', '9876543211', 'Cough, cold', 'Common cold', 'Cough syrup, warm water', CURDATE());

SELECT * FROM patients;
