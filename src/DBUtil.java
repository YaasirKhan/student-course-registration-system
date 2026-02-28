package Chap34;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.ObservableList;

/**
 * Keep ALL database-related methods here (per rubric).
 */
public class DBUtil {

    public static Connection connection;
    private Statement statement;

    public void connect(String userID, String password, String driver, String URL)
            throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        connection = DriverManager.getConnection(URL, userID, password);
    }

    public void closeDB() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Execute SQL SELECT commands (kept for compatibility with your existing DBUtil). */
    public String processSQLSelect(String sqlCommand) {
        String row = "";
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlCommand);
            int columnCount = resultSet.getMetaData().getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                row += resultSet.getMetaData().getColumnName(i) + "\t\t";
            }
            row += '\n';

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    row += resultSet.getString(i) + "\t\t";
                }
                row += '\n';
            }
            row += '\n';
            resultSet.close();
        } catch (SQLException ex) {
            return ex.toString();
        }
        return row;
    }

    /** Execute SQL DDL / INSERT / UPDATE / DELETE commands. */
    public String processSQLNonSelect(String sqlCommand) {
        try {
            statement = connection.createStatement();
            statement.executeUpdate(sqlCommand);
            return "SQL command executed";
        } catch (SQLException ex) {
            return ex.toString();
        }
    }

    // ===============================
    // Methods required for Final Project
    // ===============================

    /** Check if SSN exists in Students table. (Your DB uses Students, not Student.) */
    public boolean ssnExists(String ssn) {
        try {
            statement = connection.createStatement();
            String sql = "select ssn from Students where ssn = '" + ssn + "'";
            ResultSet rs = statement.executeQuery(sql);
            boolean exists = rs.next();
            rs.close();
            return exists;
        } catch (SQLException ex) {
            return false;
        }
    }

    /** Get ONLY the courses registered for the given SSN (courseId + title). */
    public void getRegisteredCourses(String ssn, ObservableList<Course> list) {
        try {
            statement = connection.createStatement();
            String sql =
                "select Course.courseId, Course.title " +
                "from Course, Enrollment " +
                "where Enrollment.ssn = '" + ssn + "' " +
                "and Enrollment.courseId = Course.courseId";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                list.add(new Course(rs.getString(1), rs.getString(2)));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.toString();
        }
    }

    /** Get courses NOT registered for the given SSN (used in Enroll.java). */
    public void getCoursesNotRegistered(String ssn, ObservableList<Course> options) {
        try {
            statement = connection.createStatement();
            String sql =
                "select courseId, title from Course " +
                "where courseId not in " +
                "(select courseId from Enrollment where ssn = '" + ssn + "')";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                options.add(new Course(rs.getString(1), rs.getString(2)));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.toString();
        }
    }

    /** Add/enroll a new course for the SSN. */
    public String registerCourse(String ssn, String courseId) {
        try {
            String sql =
                "insert into Enrollment values ('" +
                ssn + "','" + courseId + "', now(), 'B')";
            return processSQLNonSelect(sql);
        } catch (Exception ex) {
            return ex.toString();
        }
    }

    /** Drop/delete a course for the SSN. */
    public String dropCourse(String ssn, String courseId) {
        try {
            String sql =
                "delete from Enrollment " +
                "where ssn = '" + ssn + "' and courseId = '" + courseId + "'";
            return processSQLNonSelect(sql);
        } catch (Exception ex) {
            return ex.toString();
        }
    }
}
