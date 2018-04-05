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
 * Note: Do not use getInstance with the same Emailgenerator. It will cause
 * different instances in different tests to be the same, instead use new
 * operator.
 */
/**
 * @version 2.0 05/04/2018
 * @author Alejandro Moya García
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
    public void testWithSurname2() {
        String email = new EmailGenerator().generateEmail("juan", "perez",
                "osvaldo", "empresa");
        assertEquals("juapeos00@empresa.es", email);
    }

    @Test
    public void testWithoutSurname2() {
        String email = new EmailGenerator().generateEmail("juan", "perez",
                "", "empresa");
        assertEquals("juape00@empresa.es", email);
    }

    @Test
    public void testTwoInstances() {
        EmailGenerator eG = new EmailGenerator();
        eG.generateEmail("daniela",
                "rivera", "gonzo", "empresa");

        String email = eG.generateEmail("daniel",
                "riva", "gonzalez", "empresa");
        assertEquals("danrigo01@empresa.es", email);
    }

    @Test
    public void testSeveralInstances() {
        EmailGenerator eG = new EmailGenerator();
        for (int i = 0; i < 11; i++) {
            eG.generateEmail("pedro",
                    "martinez", "garcia", "empresa");
        }
        String email = eG.generateEmail("pedro",
                "martinez", "garcia", "empresa");
        assertEquals("pedmaga11@empresa.es", email);
    }

    @Test
    public void testMayus() {
        String email = new EmailGenerator().generateEmail("JuAn", "pErEz",
                "", "emPrEsA");
        assertEquals("juape00@empresa.es", email);

    }

    @Test
    public void testSpaces() {
        String email = new EmailGenerator().generateEmail("J u An", "p Er Ez",
                "", "e m Pr EsA");
        assertEquals("juape00@empresa.es", email);
    }

    @Test
    public void testAccents() {
        String email = new EmailGenerator().generateEmail("J ú Án", "p Ér Ez",
                "", "é m Pr EsÁ");
        assertEquals("juape00@empresa.es", email);
    }

}
