/*
 * Test class for email generator
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sis2.EmailGenerator;

/**
 *
 * @author alex
 */
public class EmailGeneratorTest {
    
    public EmailGeneratorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testWithSurname2(){
        String email=EmailGenerator.getInstance().generateEmail("juan", "perez",
                "osvaldo", "empresa");
        assertEquals("juapeos00@empresa.es",email);
    }
    
    
    @Test
    public void testWithoutSurname2(){
        String email=EmailGenerator.getInstance().generateEmail("juan", "perez",
                "", "empresa");
        assertEquals("juape00@empresa.es",email);
    }
    
    @Test
    public void testTwoInstances(){
        EmailGenerator.getInstance().generateEmail("daniela",
            "rivera","gonzo", "empresa");
        
        String email=EmailGenerator.getInstance().generateEmail("daniel",
                "riva","gonzalez", "empresa");
        assertEquals("danrigo01@empresa.es",email);
    }
    
    @Test
    public void testSeveralInstances(){
        for(int i=0;i<11;i++){
            EmailGenerator.getInstance().generateEmail("pedro",
                "martinez","garcia", "empresa");
        }
        String email=EmailGenerator.getInstance().generateEmail("pedro",
                "martinez","garcia", "empresa");
        assertEquals("pedmaga11@empresa.es",email);
    }
    
    
}
