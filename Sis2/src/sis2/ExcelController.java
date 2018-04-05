/**
 * This class manages the operations with Excel files.
 * Preconditions: the IDs must be located in the specified sheet and column of
 * the excel file. IDs must be correct, blank or containing a wrong control
 * digit. For example, IDs of length 12 are not expected, or words that are not
 * IDs.
 */
package sis2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * @version 2.0 05/04/2018
 * @author Alejandro Moya García
 */
public class ExcelController {

    private int count = 0;
    public static ExcelController instance;
    private String filePath;
    private Workbook workbook;
    private Document idErrors, accountErrors;
    private Element duplicatedBlankID, wrongAccounts;
    Set<String> ids;
    private final int SHEET_NUMBER = 0;
    private final Map<String, Integer> COLUMN_INDEXES;
    private final String[] ID_ATTRIBUTES = {"Nombre", "PrimerApellido",
        "SegundoApellido", "Categoria", "Empresa"};
    private final String[] ACCOUNT_ATTRIBUTES = {"Nombre", "PrimerApellido",
        "SegundoApellido", "Empresa", "CodigoCuentaErroneo", "IBAN"};
    private final String ID_ERRORS_PATH = System.getProperty("user.dir")
            + "/resources/Errores.xml";
    private final String ACCOUNT_ERRORS_PATH = System.getProperty("user.dir")
            + "/resources/cuentasErroneas.xml";
    private static final Logger LOGGER = MyLogger.getLogger();

    public ExcelController(String filePath) {
        this.filePath = filePath;
        this.duplicatedBlankID = new Element("Trabajadores");
        this.wrongAccounts = new Element("Trabajadores");
        this.idErrors = new Document(duplicatedBlankID);
        this.accountErrors = new Document(wrongAccounts);
        this.ids = new HashSet();
        this.COLUMN_INDEXES = new HashMap();
        setColumnIndexes();
    }

    /**
     * Creates a Map which has the association of HeaderName + ColumnIndex
     */
    public void setColumnIndexes() {
        String headers[] = {"ID", "Surname1", "Surname2", "Name", "Email",
            "Category", "CompanyName", "CIFCompany", "CompanyEntryDate",
            "JobEntryDate", "OffWorkDate", "ForcedExtraHours",
            "VolunteerExtraHours", "ProrateExtra", "AccountCode",
            "CountryCode", "IBAN"};

        for (int i = 0; i < headers.length; i++) {
            this.COLUMN_INDEXES.put(headers[i], i);
        }
    }

    /**
     * Returns the index associated to the column of name columnName
     *
     * @param columnName
     * @return the index associated to columnName.
     */
    public int getColumnIndexOf(String columnName) {
        return this.COLUMN_INDEXES.get(columnName);
    }

    /**
     * Reads the file specified in the in the filePath attribute
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void readFile() throws FileNotFoundException, IOException {
        FileInputStream excFile = new FileInputStream(new File(this.filePath));
        this.workbook = new XSSFWorkbook(excFile);
    }

    /**
     * Iterates over the rows and cells of the document, materializes employees
     * and perform actions by calling other methods.
     */
    public void iterateDocument() {
        Sheet datatypeSheet = workbook.getSheetAt(SHEET_NUMBER);
        Iterator<Row> iterator = datatypeSheet.iterator();

        if (iterator.hasNext()) {
            /* skip headers row */
            iterator.next();
        }

        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            Employee employee = initEmployee(currentRow);

            /* validate ID */
            validateID(employee);

            /* validate bank account and calculate IBAN */
            calculateIBAN(employee);

            /* Generate email */
            generateEmail(employee);
        }
    }

    /**
     * Validates the ID contained in the cell passed as paramter. IDs with a
     * wrong control digit are updated with the correct control digit. Blank
     * cells or duplicated IDs are exported to a XML file along other data.
     *
     * @param employee
     */
    public void validateID(Employee employee) {

        if (employee.hasEmptyID()) {
            /* empty cell */
            LOGGER.info("Row number: "
                    + String.valueOf(employee.getRowID() + 1)
                    + " contains an empty ID ");
            addEmployeeXML(employee, "id");
        } else {

            if (employee.correctID()) {
                /* ID was wrong */
                updateCellValue(employee.getRowID(),
                        getColumnIndexOf("ID"), employee.getID().toString());

            }
            if (this.ids.contains(employee.getID().toString()/* cellValue */)) {
                /* duplicated */
                LOGGER.info("Row number: "
                        + String.valueOf(employee.getRowID() + 1)
                        + " contains a duplicated ID: "
                        + employee.getID().toString());
                addEmployeeXML(employee, "id");
            } else {
                this.ids.add(employee.getID().toString());
            }
        }
    }

    /**
     * Verifies that the account code contained in cellCode is correct. If it is
     * wrong it corrects it. Then it calculates the IBAN number using the
     * account number and the country code.
     *
     * @param emp
     */
    public void calculateIBAN(Employee emp) {
        boolean corrected = false;
        if (emp.getWrongCode().isEmpty()) {
            /* empty cell */
            LOGGER.info("Row number: "
                    + String.valueOf(emp.getRowID() + 1)
                    + " contains an empty Bank Account code ");
        } else {

            corrected = emp.correctAccountCode();
            emp.calculateIBAN();
            updateCellValue(emp.getRowID(),
                    getColumnIndexOf("IBAN"), emp.getIBAN());
            if (corrected) {
                /* Account code was wrong */
                updateCellValue(emp.getRowID(),
                        getColumnIndexOf("AccountCode"), emp.getCorrectedAccountCode());
                addEmployeeXML(emp, "account");
                LOGGER.info("Row number: "
                        + String.valueOf(emp.getRowID() + 1)
                        + " contains a wrong account code ");
            }

        }

    }

    /**
     * Generates an email for the employee and stores it in the excel file.
     *
     * @param employee
     */
    public void generateEmail(Employee employee) {
        int emailIndex = getColumnIndexOf("Email");

        String email = employee.generateEmail();
        updateCellValue(employee.getRowID(), emailIndex, email);
    }

    /**
     * Saves the changes made in the excel file.
     *
     * @throws IOException
     */
    public void updateFile() throws IOException {
        FileOutputStream outputStream = new FileOutputStream(this.filePath);
        this.workbook.write(outputStream);
        this.workbook.close();
        outputStream.close();
    }

    /**
     * Adds a employee and its data such as name, surname, company name...
     * to the specified XML.
     *
     * @param currentRow
     */
    private void addEmployeeXML(Employee employee, String type) {

        Element empEle = new Element("Trabajador");
        String[] values;
        if (type.equals("id")) {
            values = new String[ID_ATTRIBUTES.length];
            values[0] = employee.getName();
            values[1] = employee.getSurname1();
            values[2] = employee.getSurname2();
            values[3] = employee.getCategory();
            values[4] = employee.getCompanyName();

            empEle.setAttribute("id", String.valueOf(employee.getRowID() + 1));
            for (int i = 0; i < ID_ATTRIBUTES.length; i++) {
                Element ele = new Element(ID_ATTRIBUTES[i]);
                ele.setText(values[i]);
                empEle.addContent(ele);
            }
            duplicatedBlankID.addContent(empEle);

        } else if (type.equals("account")) {
            values = new String[ACCOUNT_ATTRIBUTES.length];
            values[0] = employee.getName();
            values[1] = employee.getSurname1();
            values[2] = employee.getSurname2();
            values[3] = employee.getCompanyName();
            values[4] = employee.getWrongCode();
            values[5] = employee.getIBAN();

            empEle.setAttribute("id", String.valueOf(employee.getRowID() + 1));
            for (int i = 0; i < ACCOUNT_ATTRIBUTES.length; i++) {
                Element ele = new Element(ACCOUNT_ATTRIBUTES[i]);
                ele.setText(values[i]);
                empEle.addContent(ele);
            }

            wrongAccounts.addContent(empEle);
        }

    }

    /**
     * Saves the XML with information of workers whose ID is blank or duplicated
     * or whose account number is wrong.
     *
     * @throws IOException
     */
    public void generateXML() throws IOException {
        XMLOutputter outputID = new XMLOutputter();
        XMLOutputter outputAccount = new XMLOutputter();

        /* Errores.xml */
        outputID.setFormat(Format.getPrettyFormat());
        outputID.output(idErrors, new FileWriter(ID_ERRORS_PATH));

        /* cuentasErroneas.xml */
        outputAccount.setFormat(Format.getPrettyFormat());
        outputAccount.output(accountErrors, new FileWriter(ACCOUNT_ERRORS_PATH));
    }

    /**
     * Materializes an employee from the excel File.
     * @param row
     * @return the emloyee object initialized
     */
    public Employee initEmployee(Row row) {
        int IDIndex = getColumnIndexOf("ID");
        int accountCodeIndex = getColumnIndexOf("AccountCode");
        int countryCodeIndex = getColumnIndexOf("CountryCode");
        int ibanIndex = getColumnIndexOf("IBAN");
        int nameIndex = getColumnIndexOf("Name");
        int surname1Index = getColumnIndexOf("Surname1");
        int surname2Index = getColumnIndexOf("Surname2");
        int categoryIndex = getColumnIndexOf("Category");
        int companyNameIndex = getColumnIndexOf("CompanyName");
        Employee employee = new Employee(row.getRowNum());
        /* ID */
        employee.setID(new ID(getStringValueOf(row.getCell(IDIndex,
                 MissingCellPolicy.CREATE_NULL_AS_BLANK))));
        /* Bank Account Code and IBAN */
        BankAccountCode bAC = new BankAccountCode(getStringValueOf(row.getCell(
                accountCodeIndex, MissingCellPolicy.CREATE_NULL_AS_BLANK)));
        bAC.setCountryCode(getStringValueOf(row.getCell(
                countryCodeIndex, MissingCellPolicy.CREATE_NULL_AS_BLANK)));
        employee.setBankAccount(bAC);
        /* Name, First Surname, Second Surname*/
        employee.setName(getStringValueOf(row.getCell(nameIndex,
                 MissingCellPolicy.CREATE_NULL_AS_BLANK)));
        employee.setSurname1(getStringValueOf(row.getCell(surname1Index,
                 MissingCellPolicy.CREATE_NULL_AS_BLANK)));
        employee.setSurname2(getStringValueOf(row.getCell(surname2Index,
                 MissingCellPolicy.CREATE_NULL_AS_BLANK)));
        /* Category and company Name */
        employee.setCategory(getStringValueOf(row.getCell(categoryIndex,
                 MissingCellPolicy.CREATE_NULL_AS_BLANK)));
        employee.setCompanyName(getStringValueOf(row.getCell(companyNameIndex,
                 MissingCellPolicy.CREATE_NULL_AS_BLANK)));

        return employee;
    }

    /**
     * Returns the string value of cell. If cell is blank or null an empty
     * string is returned.
     *
     * @param cell
     * @return the string value of the cell.
     */
    public String getStringValueOf(Cell cell) {
        String cellValue = "";

        if (cell.getCellTypeEnum() == CellType.STRING) {
            cellValue = cell.getStringCellValue();
        }

        return cellValue;
    }

    /**
     * Sets value as the value of the cell located in row, col in the worksheet.
     *
     * @param row
     * @param col
     * @param value
     */
    public void updateCellValue(int row, int col, String value) {
        Sheet datatypeSheet = workbook.getSheetAt(SHEET_NUMBER);

        Cell cell = datatypeSheet.getRow(row).getCell(col,
                MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellValue(value);
    }

    /**
     * Method created to help with junit testing.
     *
     * @param row
     * @param col
     * @return the value of cell row,col
     */
    public String getCell(int row, int col) {
        Sheet datatypeSheet = workbook.getSheetAt(SHEET_NUMBER);
        Cell cell = datatypeSheet.getRow(row).getCell(col,
                MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return cell.getStringCellValue();
    }

}
