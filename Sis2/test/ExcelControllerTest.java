
/**
 * Test class used for testing ExcelController.java. This class creates a
 * temporal copy of the excelTest file and perfoms operations in that temporal
 * copy. After finish the testing the temporal copy is deleted.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sis2.ExcelController;

/**
 * @version 2.0 05/04/2018
 * @author Alejandro Moya García
 */
public class ExcelControllerTest {

    ExcelController eC;
    static File original;
    static File tmpCopy;

    public ExcelControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        try {
            original = new File(System.getProperty("user.dir")
                    + "/resources/excelTest.xlsx");
            tmpCopy = new File(System.getProperty("user.dir")
                    + "/resources/excelTestTmp.xlsx");
            copyFile(original, tmpCopy);
            eC = new ExcelController(System.getProperty("user.dir")
                    + "/resources/excelTestTmp.xlsx");
            eC.readFile();
        } catch (IOException ex) {
            Logger.getLogger(ExcelControllerTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {
        tmpCopy.delete();
    }

    public static void copyFile(File sourceFile, File destFile)
            throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new RandomAccessFile(sourceFile, "rw").getChannel();
            destination = new RandomAccessFile(destFile, "rw").getChannel();

            long position = 0;
            long count = source.size();

            source.transferTo(position, count, destination);
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    /**
     * Checks if pattern is contained in the generated XML file.
     *
     * @param pattern
     * @return true if it is contained, false otherwise.
     */
    private boolean checkPatternXML(String pattern, String fileName) {
        File file;
        FileReader fileReader;
        BufferedReader bufferedReader;
        String line;
        boolean contains = false;

        String path = System.getProperty("user.dir") + "/resources/"
                + fileName + ".xml";
        eC.iterateDocument();
        try {
            eC.generateXML();
            file = new File(path);
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(pattern)) {
                    contains = true;
                }
            }
            fileReader.close();

        } catch (IOException ex) {
            Logger.getLogger(ExcelControllerTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return contains;
    }

    @Test
    public void wrongID() {
        try {
            eC.iterateDocument();
            eC.updateFile();
            assertEquals("Y1337652D", eC.getCell(7, 0));
        } catch (IOException ex) {
            Logger.getLogger(ExcelControllerTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testDupXML() {
        assertTrue(checkPatternXML("Mohamed", "Errores"));

    }

    @Test
    public void testBlankXML() {
        assertTrue(checkPatternXML("Demetrio", "Errores"));

    }

    /**
     * Checks that an ID that becomes dup when corrected is added to the xml
     */
    @Test
    public void dupAfterCorrection() {
        assertTrue(checkPatternXML("Marta", "Errores"));
    }

    @Test
    public void generatedIBAN() {
        eC.iterateDocument();
        try {
            eC.updateFile();
        } catch (IOException ex) {
            Logger.getLogger(ExcelControllerTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        assertEquals("ES3520960043043554600000", eC.getCell(1, 16));
    }

    @Test
    public void generatedEmail() {
        eC.iterateDocument();
        try {
            eC.updateFile();
        } catch (IOException ex) {
            Logger.getLogger(ExcelControllerTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        assertEquals("MohAh00@TecnoLeonSL.es", eC.getCell(1, 4));
    }

}
