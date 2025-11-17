package healthcamp.database;

import java.sql.*;

public class DatabaseManager {
 
   
    private static final String URL = "jdbc:mysql://localhost:3306/healthcamp_db";
    private static final String USER = "root";
    private static final String PASSWORD = "sbimpn222";  
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            String createTable = """
                CREATE TABLE IF NOT EXISTS patients (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    user_id INT NOT NULL,
                    name VARCHAR(100) NOT NULL,
                    age INT NOT NULL,
                    gender VARCHAR(10),
                    phone VARCHAR(15),
                    symptoms TEXT,
                    diagnosis VARCHAR(200),
                    treatment TEXT,
                    visit_date DATE NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                )
            """;
            
            stmt.execute(createTable);
            
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    username VARCHAR(50) NOT NULL UNIQUE,
                    email VARCHAR(100) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            
            stmt.execute(createUsersTable);
            System.out.println("Database initialized successfully");
            
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
