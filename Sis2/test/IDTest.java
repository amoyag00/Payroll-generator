import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sis2.ID;

/**
 * @version 1.0 07/03/2018
 * @author Alejandro Moya García
 */
public class IDTest {

    public IDTest() {
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
    public void correctDNI() {
        ID dni = new ID("57649727C");
        assertTrue(!dni.correct());    /* DNI has not been corrected */
        assertEquals("57649727C", dni.toString());  /* Values remain the same */
    }

    @Test
    public void wrongDNI() {
        ID dni = new ID("57649727D");
        
        assertTrue(dni.correct());       /* Must have been corrected */
        assertEquals("57649727C", dni.toString());  
        /* Letter should have been corrected to C */
    }

    @Test
    public void validForeignDNI() {
        ID dni = new ID("X9924125Q");
        
        assertTrue(!dni.correct());
        assertEquals("X9924125Q", dni.toString());

    }

    @Test
    public void wrongForeignDNI() {
        ID dni = new ID("X9924125E");
        
        /* Must be corrected because the correct letter is Q, not E */
        assertTrue(dni.correct());
        assertEquals("X9924125Q", dni.toString());

    }
}
