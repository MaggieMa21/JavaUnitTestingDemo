package demo;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface for student data access object.
 */
public interface StudentStore {
    /**
     * Retrieves student.
     * 
     * @param beginTime begin time frame
     * @param endTime end time frame
     * @param userDefinedSchool text of student name
     * @return list of student
     */
    List<Student> getStudents(LocalDate beginTime, LocalDate endTime, String userDefinedSchool);
}
