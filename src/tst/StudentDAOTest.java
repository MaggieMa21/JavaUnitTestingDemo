package tst;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import demo.Student;
import demo.StudentDAO;
import lombok.SneakyThrows;

@RunWith(MockitoJUnitRunner.class)
public class StudentDAOTest {

    /**
     * Mock creates a mock. @InjectMocks creates an instance of the class and injects the mocks that are created with
     * the @Mock (or @Spy) annotations into this instance.
     */
    @InjectMocks
    private StudentDAO instance;

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection dbConnection;

    @Mock
    private PreparedStatement stmt;

    @Mock
    private ResultSet resultSet;

    @Before
    public void setup() throws SQLException {
        instance = new StudentDAO(dataSource);
    }

    @Test
    @SneakyThrows
    public void getExecutedRouteDetails() {
        when(dataSource.getConnection()).thenReturn(dbConnection);
        when(dbConnection.prepareStatement(Mockito.anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString(1)).thenReturn("Maggie");
        when(resultSet.getString(2)).thenReturn("Ma");
        when(resultSet.getString(3)).thenReturn("4.0");
        when(resultSet.getString(4)).thenReturn("25");

        List<Student> actual = instance.getStudents(LocalDate.of(1999, 12, 22), LocalDate.of(1999, 12, 23),
                "Washington State University");

        List<Student> expected = new ArrayList<Student>();
        expected.add(new Student("Maggie", "Ma", 4.0, 25));

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    @SneakyThrows
    public void getExecutedRouteDetails_PrepareStatement_SQLException() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        when(dataSource.getConnection()).thenReturn(dbConnection);
        when(dbConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
        instance.getStudents(LocalDate.parse("06/01/2017", formatter), LocalDate.parse("06/03/2017", formatter),
                "Washington State University");
    }

    @Test(expected = IllegalStateException.class)
    @SneakyThrows
    public void getUnableToLocateEvents_ExecuteQuery_SQLException() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        when(dataSource.getConnection()).thenReturn(dbConnection);
        when(dbConnection.prepareStatement(Mockito.anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenThrow(new SQLException());
        instance.getStudents(LocalDate.parse("06/01/2017", formatter), LocalDate.parse("06/03/2017", formatter),
                "Washington State University");
    }
}
