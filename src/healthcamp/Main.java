package healthcamp;

import healthcamp.database.DatabaseManager;
import healthcamp.database.PatientDAO;
import healthcamp.database.UserDAO;
import healthcamp.model.PatientRecord;
import healthcamp.model.User;
import healthcamp.ui.LoginSignupView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDate;

public class Main extends Application {
    
    private PatientDAO patientDAO = new PatientDAO();
    private TableView<PatientRecord> table = new TableView<>();
    private TextField searchField = new TextField();
    private Stage primaryStage;
    private int currentUserId;
    
    @Override
    public void start(Stage stage) {
        DatabaseManager.initializeDatabase();
        this.primaryStage = stage;
        
        stage.setTitle("Health Camp Records");
        
        showLoginScreen();
    }
    
    private void showLoginScreen() {
        LoginSignupView loginView = new LoginSignupView(user -> {
            currentUserId = user.getId();
            showMainApp();
        });
        Scene loginScene = new Scene(loginView, 500, 550);
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
    
    private void showMainApp() {
        BorderPane root = buildMainUI();
        Scene scene = new Scene(root, 1100, 600);
        try {
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        } catch (Exception ex) {
            System.err.println("Could not load stylesheet: " + ex.getMessage());
        }
        primaryStage.setScene(scene);
        loadAllPatients();
    }
    
    private BorderPane buildMainUI() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        
     
    Label header = new Label("Health Camp Records System");
    header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
    header.getStyleClass().add("header-label");
    
        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        logoutBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Logout");
            confirm.setHeaderText("Are you sure you want to logout?");
            if (confirm.showAndWait().get() == ButtonType.OK) {
                showLoginScreen();
            }
        });
        
        Button viewUsersBtn = new Button("View Users");
        viewUsersBtn.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white;");
        viewUsersBtn.setOnAction(e -> showUsersWindow());
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        HBox headerBox = new HBox(10, header, spacer, viewUsersBtn, logoutBtn);
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        VBox top = new VBox(10, headerBox);
        top.setPadding(new Insets(0, 0, 10, 0));
        
    
    setupTable();

    
    VBox rightPanel = createFormPanel();

   
    SplitPane split = new SplitPane();
    VBox tableContainer = new VBox(table);
    tableContainer.setPadding(new Insets(0));
    VBox.setVgrow(table, Priority.ALWAYS);
    split.getItems().addAll(tableContainer, rightPanel);
    split.setDividerPositions(0.68); 

   
        HBox searchBox = new HBox(10);
    searchField.setPromptText("Search by name, phone, symptoms...");
    searchField.setPrefWidth(300);
    searchField.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(searchField, Priority.ALWAYS);
        Button searchBtn = new Button("Search");
        Button showAllBtn = new Button("Show All");
        Button reportBtn = new Button("Today's Report");
        
        searchBtn.setOnAction(e -> searchPatients());
        showAllBtn.setOnAction(e -> loadAllPatients());
        reportBtn.setOnAction(e -> showTodayReport());
        
        searchBox.getChildren().addAll(searchField, searchBtn, showAllBtn, reportBtn);
        searchBox.setPadding(new Insets(10, 0, 0, 0));
        
        root.setTop(top);
        root.setCenter(split);
        root.setBottom(searchBox);
        
        return root;
    }
    
    private void setupTable() {
        table.getColumns().clear();
        
        TableColumn<PatientRecord, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);
        
        TableColumn<PatientRecord, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(120);
        
        TableColumn<PatientRecord, Integer> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        ageCol.setPrefWidth(50);
        
        TableColumn<PatientRecord, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        genderCol.setPrefWidth(70);
        
        TableColumn<PatientRecord, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneCol.setPrefWidth(100);
        
        TableColumn<PatientRecord, String> symptomsCol = new TableColumn<>("Symptoms");
        symptomsCol.setCellValueFactory(new PropertyValueFactory<>("symptoms"));
        symptomsCol.setPrefWidth(150);
        
        TableColumn<PatientRecord, String> diagnosisCol = new TableColumn<>("Diagnosis");
        diagnosisCol.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        diagnosisCol.setPrefWidth(120);
        
        TableColumn<PatientRecord, LocalDate> dateCol = new TableColumn<>("Visit Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("visitDate"));
        dateCol.setPrefWidth(100);
        
    table.getColumns().addAll(idCol, nameCol, ageCol, genderCol, phoneCol,
                  symptomsCol, diagnosisCol, dateCol);

    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private VBox createFormPanel() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(0, 0, 0, 10));
        form.setPrefWidth(320);
        
    Label formTitle = new Label("Patient Information");
    formTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    formTitle.getStyleClass().add("form-title");
        
    TextField nameField = new TextField();
        nameField.setPromptText("Patient Name");
    nameField.setMaxWidth(Double.MAX_VALUE);
        
    TextField ageField = new TextField();
    ageField.setPromptText("Age");
    ageField.setMaxWidth(Double.MAX_VALUE);
        
    ComboBox<String> genderBox = new ComboBox<>();
        genderBox.getItems().addAll("Male", "Female", "Other");
        genderBox.setPromptText("Gender");
        genderBox.setPrefWidth(Double.MAX_VALUE);
        
    TextField phoneField = new TextField();
    phoneField.setPromptText("Phone Number");
    phoneField.setMaxWidth(Double.MAX_VALUE);
        
    TextArea symptomsArea = new TextArea();
    symptomsArea.setPromptText("Symptoms");
    symptomsArea.setPrefHeight(60);
    VBox.setVgrow(symptomsArea, Priority.ALWAYS);
    symptomsArea.setMaxWidth(Double.MAX_VALUE);
        
    TextField diagnosisField = new TextField();
    diagnosisField.setPromptText("Diagnosis");
    diagnosisField.setMaxWidth(Double.MAX_VALUE);
        
    TextArea treatmentArea = new TextArea();
    treatmentArea.setPromptText("Treatment/Prescription");
    treatmentArea.setPrefHeight(60);
    VBox.setVgrow(treatmentArea, Priority.ALWAYS);
    treatmentArea.setMaxWidth(Double.MAX_VALUE);
        
        DatePicker datePicker = new DatePicker(LocalDate.now());
        
        HBox buttonBox = new HBox(10);
        Button addBtn = new Button("Add Patient");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        Button clearBtn = new Button("Clear");
        
    addBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
    updateBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
    deleteBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
   
    addBtn.getStyleClass().add("add-button");
    updateBtn.getStyleClass().add("update-button");
    deleteBtn.getStyleClass().add("delete-button");
    clearBtn.getStyleClass().add("clear-button");
        
        addBtn.setOnAction(e -> {
            if (validateInput(nameField, ageField, genderBox)) {
                PatientRecord patient = new PatientRecord(
                    nameField.getText(),
                    Integer.parseInt(ageField.getText()),
                    genderBox.getValue(),
                    phoneField.getText(),
                    symptomsArea.getText(),
                    diagnosisField.getText(),
                    treatmentArea.getText(),
                    datePicker.getValue()
                );
                
                if (patientDAO.addPatient(patient, currentUserId)) {
                    showAlert("Success", "Patient record added successfully!", Alert.AlertType.INFORMATION);
                    clearForm(nameField, ageField, genderBox, phoneField, symptomsArea, diagnosisField, treatmentArea);
                    loadAllPatients();
                } else {
                    showAlert("Error", "Failed to add patient record", Alert.AlertType.ERROR);
                }
            }
        });
        
        updateBtn.setOnAction(e -> {
            PatientRecord selected = table.getSelectionModel().getSelectedItem();
            if (selected != null && validateInput(nameField, ageField, genderBox)) {
                selected.setName(nameField.getText());
                selected.setAge(Integer.parseInt(ageField.getText()));
                selected.setGender(genderBox.getValue());
                selected.setPhone(phoneField.getText());
                selected.setSymptoms(symptomsArea.getText());
                selected.setDiagnosis(diagnosisField.getText());
                selected.setTreatment(treatmentArea.getText());
                selected.setVisitDate(datePicker.getValue());
                
                if (patientDAO.updatePatient(selected)) {
                    showAlert("Success", "Patient record updated!", Alert.AlertType.INFORMATION);
                    loadAllPatients();
                } else {
                    showAlert("Error", "Failed to update record", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Warning", "Please select a patient from the table first", Alert.AlertType.WARNING);
            }
        });
        
        deleteBtn.setOnAction(e -> {
            PatientRecord selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirm Delete");
                confirm.setHeaderText("Delete patient record?");
                confirm.setContentText("This action cannot be undone.");
                
                if (confirm.showAndWait().get() == ButtonType.OK) {
                    if (patientDAO.deletePatient(selected.getId())) {
                        showAlert("Success", "Patient record deleted", Alert.AlertType.INFORMATION);
                        clearForm(nameField, ageField, genderBox, phoneField, symptomsArea, diagnosisField, treatmentArea);
                        loadAllPatients();
                    }
                }
            } else {
                showAlert("Warning", "Please select a patient to delete", Alert.AlertType.WARNING);
            }
        });
        
        clearBtn.setOnAction(e -> clearForm(nameField, ageField, genderBox, phoneField, symptomsArea, diagnosisField, treatmentArea));
        
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                nameField.setText(newVal.getName());
                ageField.setText(String.valueOf(newVal.getAge()));
                genderBox.setValue(newVal.getGender());
                phoneField.setText(newVal.getPhone());
                symptomsArea.setText(newVal.getSymptoms());
                diagnosisField.setText(newVal.getDiagnosis());
                treatmentArea.setText(newVal.getTreatment());
                datePicker.setValue(newVal.getVisitDate());
            }
        });
        
        buttonBox.getChildren().addAll(addBtn, updateBtn);
        HBox buttonBox2 = new HBox(10, deleteBtn, clearBtn);
        
        form.getChildren().addAll(formTitle, 
            new Label("Name:"), nameField,
            new Label("Age:"), ageField,
            new Label("Gender:"), genderBox,
            new Label("Phone:"), phoneField,
            new Label("Symptoms:"), symptomsArea,
            new Label("Diagnosis:"), diagnosisField,
            new Label("Treatment:"), treatmentArea,
            new Label("Visit Date:"), datePicker,
            buttonBox, buttonBox2
        );
        
        return form;
    }
    
    private void loadAllPatients() {
        table.getItems().clear();
        table.getItems().addAll(patientDAO.getAllPatients(currentUserId));
    }
    
    private void searchPatients() {
        String keyword = searchField.getText().trim();
        if (!keyword.isEmpty()) {
            table.getItems().clear();
            table.getItems().addAll(patientDAO.searchPatients(keyword, currentUserId));
        }
    }
    
    private void showTodayReport() {
        int count = patientDAO.getTodayPatientCount(currentUserId);
        showAlert("Today's Report", 
            "Total patients seen today: " + count, 
            Alert.AlertType.INFORMATION);
    }
    
    private boolean validateInput(TextField name, TextField age, ComboBox<String> gender) {
        if (name.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter patient name", Alert.AlertType.WARNING);
            return false;
        }
        try {
            int ageValue = Integer.parseInt(age.getText());
            if (ageValue < 0 || ageValue > 150) {
                showAlert("Validation Error", "Please enter a valid age", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Please enter a valid age", Alert.AlertType.WARNING);
            return false;
        }
        if (gender.getValue() == null) {
            showAlert("Validation Error", "Please select gender", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
    
    private void clearForm(TextField name, TextField age, ComboBox<String> gender, 
                          TextField phone, TextArea symptoms, TextField diagnosis, TextArea treatment) {
        name.clear();
        age.clear();
        gender.setValue(null);
        phone.clear();
        symptoms.clear();
        diagnosis.clear();
        treatment.clear();
        table.getSelectionModel().clearSelection();
    }
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showUsersWindow() {
        Stage userStage = new Stage();
        userStage.initModality(Modality.APPLICATION_MODAL);
        userStage.setTitle("Registered Users");
        
        TableView<User> userTable = new TableView<>();
        userTable.setPrefHeight(400);
        
        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);
        
        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.setPrefWidth(150);
        
        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(200);
        
        TableColumn<User, String> createdCol = new TableColumn<>("Created At");
        createdCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        createdCol.setPrefWidth(180);
        
        userTable.getColumns().addAll(idCol, usernameCol, emailCol, createdCol);
        
        UserDAO userDAO = new UserDAO();
        userTable.getItems().addAll(userDAO.getAllUsers());
        
        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> userStage.close());
        
        VBox layout = new VBox(10, userTable, closeBtn);
        layout.setPadding(new Insets(10));
        layout.setAlignment(javafx.geometry.Pos.CENTER);
        
        Scene scene = new Scene(layout, 600, 500);
        userStage.setScene(scene);
        userStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
