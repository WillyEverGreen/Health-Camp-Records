package healthcamp.database;

import healthcamp.model.PatientRecord;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {
    
    public boolean addPatient(PatientRecord patient, int userId) {
        String sql = """
            INSERT INTO patients (user_id, name, age, gender, phone, symptoms, diagnosis, treatment, visit_date)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, patient.getName());
            pstmt.setInt(3, patient.getAge());
            pstmt.setString(4, patient.getGender());
            pstmt.setString(5, patient.getPhone());
            pstmt.setString(6, patient.getSymptoms());
            pstmt.setString(7, patient.getDiagnosis());
            pstmt.setString(8, patient.getTreatment());
            pstmt.setDate(9, Date.valueOf(patient.getVisitDate()));
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<PatientRecord> getAllPatients(int userId) {
        List<PatientRecord> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE user_id = ? ORDER BY visit_date DESC, id DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                patients.add(extractPatientFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return patients;
    }
    
    public List<PatientRecord> searchPatients(String keyword, int userId) {
        List<PatientRecord> patients = new ArrayList<>();
        String sql = """
            SELECT * FROM patients 
            WHERE user_id = ? AND (name LIKE ? OR phone LIKE ? OR symptoms LIKE ? OR diagnosis LIKE ?)
            ORDER BY visit_date DESC
        """;
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchTerm = "%" + keyword + "%";
            pstmt.setInt(1, userId);
            pstmt.setString(2, searchTerm);
            pstmt.setString(3, searchTerm);
            pstmt.setString(4, searchTerm);
            pstmt.setString(5, searchTerm);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                patients.add(extractPatientFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return patients;
    }
    
    public boolean updatePatient(PatientRecord patient) {
        String sql = """
            UPDATE patients 
            SET name=?, age=?, gender=?, phone=?, symptoms=?, diagnosis=?, treatment=?, visit_date=?
            WHERE id=?
        """;
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, patient.getName());
            pstmt.setInt(2, patient.getAge());
            pstmt.setString(3, patient.getGender());
            pstmt.setString(4, patient.getPhone());
            pstmt.setString(5, patient.getSymptoms());
            pstmt.setString(6, patient.getDiagnosis());
            pstmt.setString(7, patient.getTreatment());
            pstmt.setDate(8, Date.valueOf(patient.getVisitDate()));
            pstmt.setInt(9, patient.getId());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deletePatient(int id) {
        String sql = "DELETE FROM patients WHERE id=?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getTodayPatientCount(int userId) {
        String sql = "SELECT COUNT(*) FROM patients WHERE user_id = ? AND visit_date = CURDATE()";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    private PatientRecord extractPatientFromResultSet(ResultSet rs) throws SQLException {
        PatientRecord patient = new PatientRecord();
        patient.setId(rs.getInt("id"));
        patient.setName(rs.getString("name"));
        patient.setAge(rs.getInt("age"));
        patient.setGender(rs.getString("gender"));
        patient.setPhone(rs.getString("phone"));
        patient.setSymptoms(rs.getString("symptoms"));
        patient.setDiagnosis(rs.getString("diagnosis"));
        patient.setTreatment(rs.getString("treatment"));
        patient.setVisitDate(rs.getDate("visit_date").toLocalDate());
        return patient;
    }
}
