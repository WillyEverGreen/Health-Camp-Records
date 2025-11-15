package healthcamp.model;

import java.time.LocalDate;

public class PatientRecord {
    private int id;
    private String name;
    private int age;
    private String gender;
    private String phone;
    private String symptoms;
    private String diagnosis;
    private String treatment;
    private LocalDate visitDate;
    
    public PatientRecord() {
        this.visitDate = LocalDate.now();
    }
    
    public PatientRecord(String name, int age, String gender, String phone, 
                        String symptoms, String diagnosis, String treatment, LocalDate visitDate) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.visitDate = visitDate;
    }
    

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    
    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    
    public LocalDate getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDate visitDate) { this.visitDate = visitDate; }
    
    @Override
    public String toString() {
        return String.format("%s (Age: %d, Date: %s)", name, age, visitDate);
    }
}
