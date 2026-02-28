package Chap34;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Enroll.java
 * - Shows courses that the student did NOT register (ListView: courseId + title)
 * - Add -> inserts into Enrollment then returns to registered list (refresh)
 * - Cancel -> close window
 */
public class Enroll {

    public static void showWindow(String ssn, DBUtil db, Runnable onSuccessRefresh) {
        Stage stage = new Stage();

        Label title = new Label("Enroll (Courses you didn't register)");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        ListView<Course> lv = new ListView<>();
        ObservableList<Course> options = FXCollections.observableArrayList();
        lv.setItems(options);

        db.getCoursesNotRegistered(ssn, options);

        Button btnAdd = new Button("ADD");
        Button btnCancel = new Button("Cancel");

        Label msg = new Label("");
        msg.setStyle("-fx-text-fill: red;");

        btnAdd.setOnAction(e -> {
            Course selected = lv.getSelectionModel().getSelectedItem();
            if (selected == null) {
                msg.setText("Select a course first.");
                return;
            }

            String out = db.registerCourse(ssn, selected.getCourseId());
            if (out != null && out.toLowerCase().contains("executed")) {
                onSuccessRefresh.run(); // refresh myClassRegistered
                stage.close();          // go back
            } else {
                msg.setText("Error: " + out);
            }
        });

        btnCancel.setOnAction(e -> stage.close());

        HBox buttons = new HBox(10, btnCancel, btnAdd);
        VBox root = new VBox(10, title, lv, buttons, msg);
        root.setPadding(new Insets(15));

        stage.setTitle("Enroll");
        stage.setScene(new Scene(root, 520, 400));
        stage.show();
    }
}
