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
import sis2.BankAccountCode;

/**
 *
 * @author alex
 */
public class BankAccountCodeTest {
    
    public BankAccountCodeTest() {
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
    public void testCorrectCode() {
        BankAccountCode code=new BankAccountCode("20960043043554600000");
        assertTrue(!code.correct());
    }
    
    @Test
    public void testWrongDigits(){
        BankAccountCode code= new BankAccountCode("32145464978452163421");
        assertTrue(code.correct());
        assertEquals(code.getControlDigit1(),"1");
        assertEquals(code.getControlDigit2(),"3");
          
    }
    
    @Test
    public void testWrongDigit1(){
        BankAccountCode code= new BankAccountCode("32145464338452163421");
        assertTrue(code.correct());
        assertEquals(code.getControlDigit1(),"1");
        assertEquals(code.getControlDigit2(),"3");
    }
    
     @Test
    public void testWrongDigit2(){
        BankAccountCode code= new BankAccountCode("32145464198452163421");
        assertTrue(code.correct());
        assertEquals(code.getControlDigit1(),"1");
        assertEquals(code.getControlDigit2(),"3");
    }
    
    @Test
    public void calculateIBAN(){
        BankAccountCode code=new BankAccountCode("20960043043554600000");
        code.setCountryCode("ES");
        
        assertEquals("ES3520960043043554600000", code.calculateIBAN() );
    }
    
    @Test
    public void calculateIBAN2(){
        BankAccountCode code=new BankAccountCode("11112222004444444444");
        code.setCountryCode("ES");
        assertEquals("ES8011112222004444444444", code.calculateIBAN() );
    }
    
    @Test
    public void calculateIBAN3(){
        BankAccountCode code=new BankAccountCode("370400440532013000");
        code.setCountryCode("DE");
        assertEquals("DE89370400440532013000", code.calculateIBAN() );
    }
}
