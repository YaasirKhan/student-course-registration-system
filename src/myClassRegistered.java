package Chap34;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * myClassRegistered.java
 * - Shows ONLY courses enrolled for the given SSN in a ListView (courseId + title)
 * - Add -> opens Enroll window
 * - Delete -> confirmation alert (dropClass.java behavior) then refresh
 */
public class myClassRegistered {

    public static void showWindow(String ssn, DBUtil db) {
        Stage stage = new Stage();

        Label title = new Label("My Class Registered (SSN: " + ssn + ")");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ListView<Course> lv = new ListView<>();
        ObservableList<Course> registered = FXCollections.observableArrayList();
        lv.setItems(registered);

        Button btnAdd = new Button("Add");
        Button btnDelete = new Button("Delete");
        Button btnExit = new Button("Exit");

        Label msg = new Label("");
        msg.setStyle("-fx-text-fill: red;");

        Runnable refresh = () -> {
            registered.clear();
            db.getRegisteredCourses(ssn, registered);
        };
        refresh.run();

        btnAdd.setOnAction(e -> Enroll.showWindow(ssn, db, refresh));

        btnDelete.setOnAction(e -> {
            Course selected = lv.getSelectionModel().getSelectedItem();
            if (selected == null) {
                msg.setText("Please select a course to delete.");
                return;
            }
            dropClass.confirmAndDrop(ssn, selected, db, refresh);
        });

        btnExit.setOnAction(e -> {
            db.closeDB();
            stage.close();
        });

        HBox buttons = new HBox(10, btnAdd, btnDelete, btnExit);
        VBox root = new VBox(10, title, lv, buttons, msg);
        root.setPadding(new Insets(15));

        stage.setTitle("myClassRegistered");
        stage.setScene(new Scene(root, 520, 400));
        stage.show();
    }
}
