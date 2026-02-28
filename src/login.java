package Chap34;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * login.java
 * - Ask user for DB ID, DB password, and SSN
 * - If ID/password fails -> show error label under Login
 * - If SSN doesn't exist in Students table -> show error label
 * - If OK -> open myClassRegistered window
 */
public class login extends Application {

    // Your professor's DB server info (from your screenshot)
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL =
        "jdbc:mysql://database-cuny.c4piq2ndsfvh.us-west-1.rds.amazonaws.com:3306/CUNY_DB"
      + "?useSSL=false&serverTimezone=UTC";

    @Override
    public void start(Stage primaryStage) {
        Label lblId = new Label("DB ID:");
        TextField tfId = new TextField();

        Label lblPass = new Label("DB Password:");
        PasswordField pfPass = new PasswordField();

        Label lblSsn = new Label("SSN:");
        TextField tfSsn = new TextField();

        Button btnExit = new Button("Exit");
        Button btnLogin = new Button("Login");

        // Error label under login button (per rubric)
        Label lblMsg = new Label("");
        lblMsg.setStyle("-fx-text-fill: red;");

        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(15));

        gp.add(lblId, 0, 0);
        gp.add(tfId, 1, 0);

        gp.add(lblPass, 0, 1);
        gp.add(pfPass, 1, 1);

        gp.add(lblSsn, 0, 2);
        gp.add(tfSsn, 1, 2);

        HBox hbButtons = new HBox(10, btnExit, btnLogin);
        VBox root = new VBox(10, gp, hbButtons, lblMsg);
        root.setPadding(new Insets(15));

        btnExit.setOnAction(e -> primaryStage.close());

        btnLogin.setOnAction(e -> {
            lblMsg.setText("");

            String userId = tfId.getText().trim();
            String password = pfPass.getText();
            String ssn = tfSsn.getText().trim();

            if (userId.isEmpty() || password.isEmpty() || ssn.isEmpty()) {
                lblMsg.setText("Please enter DB ID, DB password, and SSN.");
                return;
            }

            DBUtil db = new DBUtil();
            try {
                // Connect using the user-entered DB ID/password (per rubric)
                db.connect(userId, password, DRIVER, URL);

                // Check SSN exists in Students table
                if (!db.ssnExists(ssn)) {
                    lblMsg.setText("SSN doesn't exist.");
                    db.closeDB();
                    return;
                }

                // Open next window
                myClassRegistered.showWindow(ssn, db);

                // Close login window
                primaryStage.close();

            } catch (Exception ex) {
                lblMsg.setText("Your ID/password is wrong.");
            }
        });

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 420, 220));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
