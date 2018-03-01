/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sis2.ID;

/**
 *
 * @author alex
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
     public void correctDNI() {
         ID dni = new ID("57649727C");
         assertTrue(!dni.correct());//DNI has not been corrected
         assertEquals("57649727C", dni.toString());//Values remain the same
     }
     
     @Test
     public void wrongDNI(){
         ID dni = new ID("57649727D");
         assertTrue(dni.correct());//Must have been corrected
         assertEquals("57649727C", dni.toString());//Letter should have been corrected to C
     }
     
     @Test
     public void validForeignDNI(){
        ID dni = new ID("X9924125Q");
        assertTrue(!dni.correct());
        assertEquals("X9924125Q", dni.toString());
         
     }
     
     @Test
     public void wrongForeignDNI(){
        ID dni = new ID("X9924125E");
        //Must be corrected because the correct letter is Q, not E
        assertTrue(dni.correct());
        assertEquals("X9924125Q", dni.toString());
         
     }
}
