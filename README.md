# Health Camp Records System

A desktop application for managing patient records during health camps. Built with JavaFX and MySQL to digitize healthcare in underserved communities.

![Java](https://img.shields.io/badge/Java-17+-orange.svg)
![JavaFX](https://img.shields.io/badge/JavaFX-25.0.1-blue.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)
![Maven](https://img.shields.io/badge/Maven-3.9+-red.svg)

---

## 🎯 Problem Statement

Many health camps in underserved communities still rely on **paper-based systems**, leading to:

- ❌ Lost or misplaced patient records
- ❌ Poor follow-up care
- ❌ Inefficient reporting and data analysis
- ❌ Difficulty in tracking patient history

**This application solves these problems** by providing a simple, offline-capable digital record system that helps health workers manage patient data efficiently.

---

## ✨ Features

- ✅ **Add Patient Records** - Capture complete patient information (name, age, gender, phone, symptoms, diagnosis, treatment)
- ✅ **View All Records** - Browse all patients in an organized table
- ✅ **Search Functionality** - Find patients by name, phone, symptoms, or diagnosis
- ✅ **Update Records** - Edit existing patient information
- ✅ **Delete Records** - Remove records with confirmation
- ✅ **Daily Reports** - Quick statistics (patients seen today)
- ✅ **Offline Operation** - Works without internet connectivity
- ✅ **Local Database** - Secure MySQL storage

---

## 🛠️ Technology Stack

- **Frontend:** JavaFX 25.0.1
- **Backend:** Java 17+
- **Database:** MySQL 8.x
- **Build Tool:** Maven 3.9+
- **JDBC Driver:** MySQL Connector/J 8.4.0

---

## 📋 Prerequisites

Before running the application, install:

1. **Java JDK 17 or higher**

   - Download: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
   - Verify: `java -version`

2. **Apache Maven 3.9 or higher**

   - Download: [Maven](https://maven.apache.org/download.cgi)
   - Verify: `mvn -version`

3. **MySQL Server 8.0 or higher**
   - Option A: [XAMPP](https://www.apachefriends.org/) (Recommended - includes MySQL + phpMyAdmin)
   - Option B: [MySQL Standalone](https://dev.mysql.com/downloads/mysql/)
   - Option C: [MySQL Workbench](https://dev.mysql.com/downloads/workbench/)

---

## 🚀 Installation & Setup

### Step 1: Clone the Repository

```bash
git clone https://github.com/WillyEverGreen/Health-Camp-Records.git
cd Health-Camp-Records
```

### Step 2: Setup MySQL Database

#### Option A: Using MySQL Workbench (Recommended)

1. Open MySQL Workbench
2. Connect to your local MySQL instance
3. Open and execute `database_setup.sql` (⚡ Execute button)
4. Verify `healthcamp_db` database was created

#### Option B: Using XAMPP phpMyAdmin

1. Start XAMPP → Start MySQL
2. Open http://localhost/phpmyadmin
3. Click "New" → Database name: `healthcamp_db` → Create
4. Click on `healthcamp_db` → Import → Select `database_setup.sql` → Go

#### Option C: Using MySQL Command Line

```bash
mysql -u root -p
CREATE DATABASE healthcamp_db;
USE healthcamp_db;
source database_setup.sql;
exit;
```

### Step 3: Configure Database Connection

Open `src/healthcamp/database/DatabaseManager.java` and update line 10:

```java
private static final String PASSWORD = "your_mysql_password";  // Update this!
```

**Note:** If using XAMPP with default settings, leave as `""` (empty string)

### Step 4: Add Sample Data (Optional but Recommended)

To test with sample patients, run `INSERT_DATA.sql`:

- In MySQL Workbench: Open file → Execute (⚡ button)
- Should see: "12 rows affected"

This adds 12 realistic patient records with different symptoms and visit dates.

### Step 5: Run the Application

**Windows (PowerShell):**

```powershell
.\run.bat
```

**Windows (Command Prompt):**

```cmd
run.bat
```

**Mac/Linux:**

```bash
mvn clean javafx:run
```

The application window will open showing all patient records!

---

## 📖 How to Use

### Adding a Patient

1. Fill in the form on the right side
2. Required fields: Name, Age, Gender
3. Click "Add Patient"
4. Record appears in the table instantly

### Searching Patients

1. Enter search term in the search box (bottom)
2. Click "Search"
3. Results filter in real-time
4. Click "Show All" to reset

### Updating a Record

1. Click on any patient row in the table
2. Details populate in the form
3. Edit the information
4. Click "Update"

### Deleting a Record

1. Select a patient from the table
2. Click "Delete"
3. Confirm the action

### Daily Report

- Click "Today's Report" button
- Shows count of patients seen today

---

## 📁 Project Structure

```
HealthCampFX/
├── src/healthcamp/
│   ├── Main.java                    # Main application & UI
│   ├── model/
│   │   └── PatientRecord.java       # Patient data model
│   └── database/
│       ├── DatabaseManager.java     # Database connection
│       └── PatientDAO.java          # CRUD operations
├── pom.xml                          # Maven dependencies
├── run.bat                          # Windows run script
├── database_setup.sql               # Database schema
├── INSERT_DATA.sql                  # Sample patient data
├── .gitignore                       # Git ignore rules
├── LICENSE                          # MIT License
└── README.md                        # This file
```

---

## 🤝 Contributing to SDG 3 (Good Health and Well-being)

This project supports **UN Sustainable Development Goal 3** by:

- 📊 **Digitizing health records** in underserved communities
- 🏥 **Improving follow-up care** through organized patient data
- 📈 **Enabling better health reporting** and trend analysis
- 🌍 **Reducing paper waste** and environmental impact
- 💪 **Empowering health workers** with efficient digital tools

---

## 🐛 Troubleshooting

### Issue: "Access denied for user 'root'"

**Solution:** Update password in `src/healthcamp/database/DatabaseManager.java` (line 10)

### Issue: "Unknown database 'healthcamp_db'"

**Solution:** Run `database_setup.sql` to create the database

### Issue: "Communications link failure"

**Solution:** Start MySQL service

- Windows: Services → MySQL80 → Start
- XAMPP: Open XAMPP Control → Start MySQL
- Mac: `brew services start mysql`
- Linux: `sudo systemctl start mysql`

### Issue: "Maven not found"

**Solution:**

- Install Maven and add to PATH
- Or edit `run.bat` with full Maven path: `"C:\Program Files\Apache\apache-maven-3.9.11\bin\mvn.cmd" clean javafx:run`

### Issue: No data showing in app

**Solution:**

1. Verify data exists: `SELECT COUNT(*) FROM patients;` in MySQL
2. If count is 0, run `INSERT_DATA.sql`
3. Restart the application

---

## 🔒 Security Notes

- This application is designed for **local, offline use** in health camps
- Database credentials are in source code (for simplicity)
- **For production use**, consider:
  - Environment variables for credentials
  - User authentication and role-based access
  - Encrypted database connections
  - Regular database backups
  - Compliance with healthcare data regulations (HIPAA, GDPR, etc.)

---

## 👥 Author

**WillyEverGreen** - [@WillyEverGreen](https://github.com/WillyEverGreen)

---

## 👥 Contributing

Contributions are welcome! To contribute:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/YourFeature`
3. Commit changes: `git commit -m "Add: YourFeature"`
4. Push to branch: `git push origin feature/YourFeature`
5. Open a Pull Request

Please ensure:

- Code follows existing style
- All features are tested
- Documentation is updated

---

## 🙏 Acknowledgments

Built to support community health camps in underserved areas. Special thanks to all health workers who inspired this project.

---

## 📞 Support

For issues or questions:

- 🐛 [GitHub Issues](https://github.com/WillyEverGreen/Health-Camp-Records/issues)

---

## 🗺️ Future Roadmap

- [ ] Export records to PDF/Excel
- [ ] Patient photos and documents
- [ ] Multi-user authentication
- [ ] Analytics dashboard

---

## 📝 License

MIT License - Copyright (c) 2025 WillyEverGreen

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

---

**Made with ❤️ for community health workers**

_Empowering healthcare in underserved communities, one digital record at a time._
