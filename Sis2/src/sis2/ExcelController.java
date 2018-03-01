package sis2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;



/**
 *
 * @author Alejandro Moya García
 */
public class ExcelController {
    public static ExcelController instance;
    private  String filePath; 
    private Workbook workbook;
    private final int SHEET_NUMBER=0, ID_COL=0;
    private Document doc;
    private Element workers;
    private final String WORKER_ATTRIBUTES []= {"Nombre", "PrimerApellido", "SegundoApellido",
        "Categoria", "Empresa"};
    private final int ATTRIBUTES_INDEXES []={3,1,2,5,6};
    private  static final Logger LOGGER = MyLogger.getLogger();
    
  public ExcelController(String filePath){
      this.filePath=filePath;
      this.workers=new Element("Trabajadores");
      this.doc=new Document(workers);
  }
  
  
    /**
     * Reads the file spcified in the in the filePath attribute
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void readFile() throws FileNotFoundException, IOException{
         FileInputStream excelFile = new FileInputStream(new File(this.filePath));
         this.workbook = new XSSFWorkbook(excelFile);
    }
    
    
    /**
     * Validates the IDs of the excel workbook in the SHEET_NUMBER sheet.
     * IDs with a wrong control digit are updated with the correct control digit.
     * Blank cells or duplicated IDs are exported to a XML file along other data.
     *
     */
    public void validateIDs(){
        Set<String> ids= new HashSet();
        String cellValue;
        Sheet datatypeSheet = workbook.getSheetAt(SHEET_NUMBER);
        Iterator<Row> iterator = datatypeSheet.iterator();
        if(iterator.hasNext());//Skip headers row
            iterator.next();
        
        while(iterator.hasNext()){
            Row currentRow = iterator.next();
            Cell currentCell=currentRow.getCell(ID_COL,MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    
            if(currentCell.getCellTypeEnum() == CellType.BLANK){//empty cell
                LOGGER.info("Row number: "+String.valueOf(currentRow.getRowNum()+1)+" contains an empty ID ");
                addWorker(currentRow); 
            }else if(currentCell.getCellTypeEnum() == CellType.STRING){
                cellValue=currentCell.getStringCellValue();
                ID id= new ID(cellValue);
                if(id.correct()){//ID was wrong
                    currentCell.setCellValue(id.toString());
                }
                if(ids.contains(cellValue)){//duplicated
                    LOGGER.info("Row number: "+String.valueOf(currentRow.getRowNum()+1)+
                        " contains a duplicated ID: "+cellValue);   
                    addWorker(currentRow);
                }else{
                    ids.add(cellValue);
                }   
            }
            
        }
    }
    
    
    /**
     * Saves the changes made in the IDs
     * @throws IOException 
     */
    public void updateFile() throws IOException{
        FileOutputStream outputStream = new FileOutputStream(this.filePath);
        this.workbook.write(outputStream);
        this.workbook.close();
    }
    
    /**
     * Adds a worker and its row, Name, FirstSurname, SecondSurname, Category
     * and Company to the list of workers whose ID is blank or duplicated
     * @param currentRow 
     */
    private void addWorker(Row currentRow){
        Element worker= new Element("Trabajador");
        worker.setAttribute("id", String.valueOf(currentRow.getRowNum()+1));
        for (int i=0 ; i< WORKER_ATTRIBUTES.length;i++){
            Element ele= new Element(WORKER_ATTRIBUTES[i]);
            ele.setText(currentRow.getCell(ATTRIBUTES_INDEXES[i]).getStringCellValue());
            worker.addContent(ele);
        }
        workers.addContent(worker);        
    }
    
    
    /**
     * Saves the XML with information of workers whose ID is blank or duplicated
     * @param path
     * @throws IOException 
     */
    public void generateXML(String path) throws IOException{
        XMLOutputter output = new XMLOutputter();
        output.setFormat(Format.getPrettyFormat());
	output.output(doc, new FileWriter(path));

    }
    
    
}
