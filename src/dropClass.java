package Chap34;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * dropClass.java
 * - Confirmation alert like FinalProject.png
 * - If YES -> delete from Enrollment then refresh registered list
 */
public class dropClass {

    public static void confirmAndDrop(String ssn, Course course, DBUtil db, Runnable refresh) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Drop: " + course.getCourseId() + " - " + course.getTitle());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            db.dropCourse(ssn, course.getCourseId());
            refresh.run();
        }
    }
}
