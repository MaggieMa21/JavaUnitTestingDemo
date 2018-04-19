package tst;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import demo.Student;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

@RunWith(MockitoJUnitRunner.class)

public class StudentTest {

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(Student.class).withRedefinedSuperclass()
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void serialization() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Student expected = Student.builder().firstName("Maggie").lastName("Ma").GPA(4.0).age(25).build();
        String content = mapper.writeValueAsString(expected);
        Student actual = mapper.readValue(content, Student.class);

        assertEquals(expected, actual);
    }
}
