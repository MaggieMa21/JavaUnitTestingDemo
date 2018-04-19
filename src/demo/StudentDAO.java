package demo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import lombok.AllArgsConstructor;

/**
 * Detail of student data access object.
 */
@AllArgsConstructor
public class StudentDAO implements StudentStore {
    private static final String PREPARE_STATEMENT_SQL = "select first_name, last_name, GPA, age from student_data "
            + "where date>=? and date<=? and school = ? ";
    private DataSource dataSource;

    public List<Student> getStudents(LocalDate beginTime, LocalDate endTime, String userDefinedSchool) {

        try (Connection dbConnection = dataSource.getConnection();) {
            List<Student> resultList = new ArrayList<Student>();
            try (PreparedStatement stmt = dbConnection.prepareStatement(PREPARE_STATEMENT_SQL);) {
                stmt.setDate(1, Date.valueOf(beginTime));
                stmt.setDate(2, Date.valueOf(endTime));
                stmt.setString(3, userDefinedSchool);
                try (ResultSet rs = stmt.executeQuery();) {
                    while (rs.next()) {
                        String firstName = rs.getString(1);
                        String lastName = rs.getString(2);
                        double GPA = Double.parseDouble(rs.getString(3));
                        int age = Integer.parseInt(rs.getString(4));

                        Student temp = new Student(firstName, lastName, GPA, age);
                        resultList.add(temp);
                    }
                    return resultList;
                } catch (SQLException e) {
                    throw new IllegalStateException("Unable to parse student object.", e);
                }

            } catch (SQLException e) {
                throw new IllegalStateException("Unable to perform querying.", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create connection", e);
        }
    }
}
