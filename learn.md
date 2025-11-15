## Health Camp Records — Learning Guide

This document explains the Health Camp Records application: its architecture, how JavaFX is used, how the app connects to MySQL via JDBC, how Maven builds and runs the app, and practical tips to extend or debug it.

If anything below looks different from your workspace, open the named file(s) and let me know — I can adjust the guide to match your exact version.

---

## 1) High-level architecture

- UI layer (JavaFX): `src/healthcamp/Main.java` — builds windows, scenes and UI controls (TableView, forms, buttons). The Main class orchestrates UI actions (search, add, update, delete).
- Data access layer (DAO): `src/healthcamp/database/PatientDAO.java` — contains CRUD methods using JDBC PreparedStatement to talk to MySQL.
- Database bootstrap: `src/healthcamp/database/DatabaseManager.java` — provides `getConnection()` and `initializeDatabase()` which creates tables if missing.
- Domain / models: `src/healthcamp/model/PatientRecord.java` — simple POJO representing a patient visit row returned/used by the UI.
- SQL scripts: `database_setup.sql` (schema) and `INSERT_DATA.sql` (optional sample data).
- Build/run: `pom.xml` (Maven) and `run.bat` (Windows helper that invokes Maven/goals).

Flow: UI events (button presses) -> call DAO methods -> DAO runs SQL via JDBC -> results map to model objects -> UI updates TableView, forms, alerts.

---

## 2) File map (important files)

- `src/healthcamp/Main.java` — Application entrypoint. Creates Stage, Scene and builds the main screen layout (BorderPane, TableView center, form right). Handles event wiring.
- `src/healthcamp/model/PatientRecord.java` — fields like id, name, age, gender, phone, symptoms, diagnosis, treatment, visitDate and getters/setters.
- `src/healthcamp/database/DatabaseManager.java` — JDBC URL, username and password constants; getConnection() method and SQL used at startup to create `patients` table.
- `src/healthcamp/database/PatientDAO.java` — methods: addPatient, getAllPatients, searchPatients, updatePatient, deletePatient, getTodayPatientCount.
- `database_setup.sql` — canonical SQL you can use to create DB and table manually (if you prefer to run SQL in Workbench).
- `pom.xml` — project dependencies (JavaFX artifacts, MySQL Connector/J), and the JavaFX Maven plugin configuration that lets `mvn javafx:run` launch the app.
- `run.bat` — convenience wrapper for Windows to run the app using Maven.

---

## 3) How the database and DAO tie into the UI (practical sequence)

1. Application starts: `Main.start()` calls `DatabaseManager.initializeDatabase()` to ensure the `patients` table exists.
2. `Main` constructs the table (TableView) via `setupTable()` and loads rows by calling `patientDAO.getAllPatients()` which executes `SELECT` and maps rows to `PatientRecord` instances.
3. When the user fills the form and clicks "Add Patient", the event handler builds a `PatientRecord` object and calls `patientDAO.addPatient(patient)`.
4. `PatientDAO.addPatient` uses a PreparedStatement like `INSERT INTO patients (...) VALUES (?, ?, ...)` to persist the row. On success the UI reloads the table.
5. Updating / deleting follows the same pattern (select row in table -> populate form -> call DAO.updatePatient / DAO.deletePatient -> refresh table).

Key code patterns to look for in DAO:

- Use `try-with-resources (Connection/PreparedStatement/ResultSet)` to ensure resources are closed.
- Use `PreparedStatement` with `?` parameters to avoid SQL injection and ensure proper escaping.
- Map `ResultSet` columns to model fields carefully and handle nulls/time conversions (e.g., java.sql.Date -> java.time.LocalDate).

---

## 4) JavaFX concepts used in this app (what to learn)

This app uses a modest but common subset of JavaFX. Focus areas:

- Stage and Scene

  - Stage is the window; Scene holds a root Node.
  - `primaryStage.setScene(scene); primaryStage.show();`

- Layouts

  - BorderPane: top/left/center/right/bottom areas. Main layout uses BorderPane with the center as the Table and the right as a form.
  - HBox / VBox: simple horizontal/vertical boxes for arranging controls.
  - SplitPane: for resizable panes (used in alternate/earlier responsive designs). Use HGrow/VGrow to let nodes expand: `HBox.setHgrow(node, Priority.ALWAYS)` and `VBox.setVgrow(node, Priority.ALWAYS)`.

- Controls

  - TableView<T>: displays lists of domain objects. Important parts: TableColumn<T,S> with cell value factories (PropertyValueFactory) and selection model to get the selected row. Example:
    - nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
  - TextField, TextArea, ComboBox, DatePicker, Buttons for the form.
  - Alerts for dialogs: new Alert(Alert.AlertType.INFORMATION, "message").showAndWait();

- Event handling

  - Set actions: `button.setOnAction(e -> doSomething());`
  - Listeners: `table.getSelectionModel().selectedItemProperty().addListener((obs, old, neu) -> { ... });`

- Threading

  - Long-running IO should not run on the JavaFX Application Thread. Use `Task<V>` or `Service` and `Platform.runLater()` to push UI updates back to the UI thread.
  - For this app, simple synchronous JDBC calls are short enough for modest datasets, but consider background tasks if DB operations or network calls get slow.

- Styling
  - JavaFX supports CSS. If `style.css` exists, it's loaded with `scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());`.
  - Use `.getStyleClass().add("className")` on nodes to attach styles.

If you're new to JavaFX, go through small focused examples: build a window, add a button, build a TableView and populate it with an ObservableList.

---

## 5) Maven and running the app

- `pom.xml` includes:

  - JavaFX dependencies (platform classifier or use javafx-bom). The `javafx-maven-plugin` can launch the app with `mvn javafx:run`.
  - MySQL Connector/J dependency so JDBC can connect.

- To run locally on Windows (from project root):

```powershell
.\run.bat
# or directly
mvn -DskipTests package
mvn javafx:run
```

- Important: Maven needs to be on your PATH or `run.bat` needs to point to the correct `mvn.cmd` (it attempts to find `mvn` and falls back to a hard-coded path in `run.bat`).

---

## 6) SQL / MySQL specifics used here

- DB connection: `DatabaseManager` contains JDBC URL, user and password (hard-coded in this project). Example: `jdbc:mysql://localhost:3306/healthcamp_db`.
- Make sure the MySQL server is running and the database `healthcamp_db` exists (or alter the code to create it). You can run `database_setup.sql` in MySQL Workbench.

Common SQL steps (MySQL):

```sql
CREATE DATABASE IF NOT EXISTS healthcamp_db;
USE healthcamp_db;
-- Run the CREATE TABLE statement from database_setup.sql
```

Security note: avoid hard-coding DB credentials in code for production. Use environment variables or a config file outside source control.

---

## 7) Common troubleshooting

- JavaFX not found or module errors: ensure your JDK and JavaFX library versions are compatible (pom.xml handles javafx deps for Maven builds). When running from IDE you may need to configure VM options with --module-path and --add-modules if not using the maven plugin.
- Maven not found: add Maven to PATH or edit `run.bat` to point to your mvn.cmd.
- DB connection errors: check MySQL service, credentials, firewall, and that `healthcamp_db` exists and user has privileges.
- Table empty: run `SELECT * FROM patients;` in Workbench to confirm rows exist.

---

## 8) How to extend the app (practical suggestions)

- Add paging to the TableView for large datasets.
- Use connection pooling (HikariCP) instead of direct DriverManager to improve DB performance and stability.
- Move DB credentials to environment variables and read them at startup.
- Add a proper authentication layer (User model + UserDAO, password hashing) and gate the UI behind a login. If you want, I can re-implement this as a proper branch and add tests.
- Add unit tests for DAOs using an in-memory DB (H2) to validate SQL and mapping.

---

## 9) Quick reference: common code patterns in the project

- Get DB connection (DatabaseManager):

```java
try (Connection conn = DatabaseManager.getConnection()) {
    // use conn
}
```

- PreparedStatement insert example:

```java
String sql = "INSERT INTO patients (name, age, ...) VALUES (?, ?, ... )";
try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
    ps.setString(1, patient.getName());
    ps.setInt(2, patient.getAge());
    ps.executeUpdate();
}
```

- TableView column binding (property name must match getter name):

```java
TableColumn<PatientRecord, String> nameCol = new TableColumn<>("Name");
nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
```

---

## 10) Where to learn more (short list)

- JavaFX official docs and tutorials (OpenJFX): https://openjfx.io
- Oracle JavaFX tutorials (controls, layout, properties)
- Java JDBC tutorial (official Oracle / Baeldung) for PreparedStatement and result mapping
- Maven guide for JavaFX and the javafx-maven-plugin docs

---

If you'd like, I can:

- add a small runnable example showing a TableView loaded from a tiny H2 in-memory DB (fast to iterate), or
- re-introduce a proper login flow on a new git branch with tests and clear commits, or
- create targeted
