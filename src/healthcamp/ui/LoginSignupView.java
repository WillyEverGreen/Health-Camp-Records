package healthcamp.ui;

import healthcamp.database.UserDAO;
import healthcamp.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class LoginSignupView extends VBox {
    
    private UserDAO userDAO = new UserDAO();
    private Runnable onLoginSuccess;
    
    public LoginSignupView(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
        setupUI();
    }
    
    private void setupUI() {
        setAlignment(Pos.CENTER);
        setSpacing(20);
        setPadding(new Insets(40));
        setStyle("-fx-background-color: #f5f5f5;");
        
        Label titleLabel = new Label("Health Camp Records");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        TabPane tabPane = new TabPane();
        tabPane.setMaxWidth(400);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        Tab loginTab = new Tab("Login");
        loginTab.setContent(createLoginForm());
        
        Tab signupTab = new Tab("Sign Up");
        signupTab.setContent(createSignupForm());
        
        tabPane.getTabs().addAll(loginTab, signupTab);
        
        getChildren().addAll(titleLabel, tabPane);
    }
    
    private VBox createLoginForm() {
        VBox form = new VBox(15);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.CENTER);
        
        Label infoLabel = new Label("Login with username or email");
        infoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");
        
        TextField usernameOrEmailField = new TextField();
        usernameOrEmailField.setPromptText("Username or Email");
        usernameOrEmailField.setMaxWidth(300);
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);
        
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 30;");
        loginButton.setMaxWidth(300);
        
        loginButton.setOnAction(e -> {
            String usernameOrEmail = usernameOrEmailField.getText().trim();
            String password = passwordField.getText();
            
            if (usernameOrEmail.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please fill in all fields");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            User user = userDAO.findByUsernameOrEmail(usernameOrEmail);
            
            if (user == null) {
                messageLabel.setText("User not found");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            if (!user.getPassword().equals(password)) {
                messageLabel.setText("Incorrect password");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            messageLabel.setText("Login successful!");
            messageLabel.setStyle("-fx-text-fill: green;");
            
            if (onLoginSuccess != null) {
                onLoginSuccess.run();
            }
        });
        
        form.getChildren().addAll(infoLabel, usernameOrEmailField, passwordField, messageLabel, loginButton);
        return form;
    }
    
    private VBox createSignupForm() {
        VBox form = new VBox(15);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.CENTER);
        
        Label infoLabel = new Label("Create a new account");
        infoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");
        
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(300);
        
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(300);
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);
        
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.setMaxWidth(300);
        
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        
        Button signupButton = new Button("Sign Up");
        signupButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 30;");
        signupButton.setMaxWidth(300);
        
        signupButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                messageLabel.setText("Please fill in all fields");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            if (username.length() < 3) {
                messageLabel.setText("Username must be at least 3 characters");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            if (!email.contains("@") || !email.contains(".")) {
                messageLabel.setText("Please enter a valid email");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            if (password.length() < 4) {
                messageLabel.setText("Password must be at least 4 characters");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                messageLabel.setText("Passwords do not match");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            if (userDAO.isUsernameTaken(username)) {
                messageLabel.setText("Username already taken");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            if (userDAO.isEmailTaken(email)) {
                messageLabel.setText("Email already registered");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            User newUser = new User(username, email, password);
            if (userDAO.createUser(newUser)) {
                messageLabel.setText("Account created! Please login.");
                messageLabel.setStyle("-fx-text-fill: green;");
                usernameField.clear();
                emailField.clear();
                passwordField.clear();
                confirmPasswordField.clear();
            } else {
                messageLabel.setText("Failed to create account. Please try again.");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });
        
        form.getChildren().addAll(infoLabel, usernameField, emailField, passwordField, 
                                   confirmPasswordField, messageLabel, signupButton);
        return form;
    }
}
