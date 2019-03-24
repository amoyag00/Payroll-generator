/**
 * This class manages the operations with Excel files. Preconditions: the IDs
 * must be located in the specified sheet and column of the excel file. IDs must
 * be correct, blank or containing a wrong control digit. For example, IDs of
 * length 12 are not expected, or words that are not IDs.
 */
package sis2;

import com.itextpdf.text.DocumentException;
import hibernate.NewHibernateUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * @version 4.0 02/06/2018
 * @author Alejandro Moya García
 */
public class ExcelController {

    private int count = 0;
    public static ExcelController instance;
    private String filePath;
    private Workbook workbook;
    private Document idErrors, accountErrors;
    private Element duplicatedBlankID, wrongAccounts;
    private PayrollInformation info;
    Set<String> ids;
    private ArrayList<Employee> employees;
    private final int EMPLOYEE_SHEET_NUMBER = 0;
    private final int PAYROLL_DATA_SHEET_NUMBER = 1;
    private final Map<String, Integer> COLUMN_INDEXES;
    private final Map<String, Integer> CATEGORY_INDEXES;
    private final Map<String, Integer> IRPF_INDEXES;
    private final Map<String, Integer> PERCENT_INDEXES;
    private final Map<String, Integer> THREEYEAR_INDEXES;
    private final String[] ID_ATTRIBUTES = {"Nombre", "PrimerApellido",
        "SegundoApellido", "Categoria", "Empresa"};
    private final String[] ACCOUNT_ATTRIBUTES = {"Nombre", "PrimerApellido",
        "SegundoApellido", "Empresa", "CodigoCuentaErroneo", "IBAN"};
    private final String ID_ERRORS_PATH = System.getProperty("user.dir")
            + "/resources/Errores.xml";
    private final String ACCOUNT_ERRORS_PATH = System.getProperty("user.dir")
            + "/resources/cuentasErroneas.xml";
    private final String PDF_PATH = System.getProperty("user.dir")
            + "/resources/";
    private static final Logger LOGGER = MyLogger.getLogger();
    private int month;
    private int year;

    public ExcelController(String filePath) {
        this.filePath = filePath;
        this.duplicatedBlankID = new Element("Trabajadores");
        this.wrongAccounts = new Element("Trabajadores");
        this.idErrors = new Document(duplicatedBlankID);
        this.accountErrors = new Document(wrongAccounts);
        this.ids = new HashSet();
        this.COLUMN_INDEXES = new HashMap();
        this.CATEGORY_INDEXES = new HashMap();
        this.IRPF_INDEXES = new HashMap();
        this.PERCENT_INDEXES = new HashMap();
        this.THREEYEAR_INDEXES = new HashMap();
        this.info = PayrollInformation.getInstance();
        this.employees = new ArrayList();
        setColumnIndexes();
    }

    /**
     * Creates a Map which has the association of HeaderName + ColumnIndex
     */
    public void setColumnIndexes() {
        String headers[] = {"ID", "Surname1", "Surname2", "Name", "Email",
            "Category", "CompanyName", "CIFCompany", "CompanyEntryDate",
            "EntryDate", "LeavingDate", "ForcedExtraHours",
            "VolunteerExtraHours", "ProrateExtra", "AccountCode",
            "CountryCode", "IBAN"};
        String categoryHeaders[] = {"CategoryName", "BasePayroll", "Complements"
                , "QuotationCode"};
        String IRPFHeaders[] = {"YearlyGross", "Retention"};
        String percentHeaders[] = {"Name", "Percent"};
        String ThreeYearsHeaders[] = {"numYear", "GrossAmount"};
        int irpfOffset = 5;
        int threeYearsOffset = 3;

        for (int i = 0; i < headers.length; i++) {
            this.COLUMN_INDEXES.put(headers[i], i);
            if (i < categoryHeaders.length) {
                this.CATEGORY_INDEXES.put(categoryHeaders[i], i);
            }
            if (i < IRPFHeaders.length) {
                this.IRPF_INDEXES.put(IRPFHeaders[i], i + irpfOffset);
            }
            if (i < percentHeaders.length) {
                this.PERCENT_INDEXES.put(percentHeaders[i], i);
            }
            if (i < ThreeYearsHeaders.length) {
                this.THREEYEAR_INDEXES.put(ThreeYearsHeaders[i],
                        i + threeYearsOffset);
            }
        }
    }

    /**
     * Returns the index associated to the column of name columnName
     *
     * @param columnName
     * @param mapName
     * @return the index associated to columnName.
     */
    public int getColumnIndexOf(String columnName, String mapName) {
        if (mapName.equals("column")) {
            return this.COLUMN_INDEXES.get(columnName);
        } else if (mapName.equals("category")) {
            return this.CATEGORY_INDEXES.get(columnName);
        } else if (mapName.equals("irpf")) {
            return this.IRPF_INDEXES.get(columnName);
        } else if (mapName.equals("percent")) {
            return this.PERCENT_INDEXES.get(columnName);
        } else if (mapName.equals("threeyear")) {
            return this.THREEYEAR_INDEXES.get(columnName);
        }
        return -1;

    }

    /**
     * Reads the information about the payroll such as discount percents and the
     * salaries and complements associated to the categories and stores it.
     */
    public void fillPayrollInfo() {
        Sheet datatypeSheet = workbook.getSheetAt(PAYROLL_DATA_SHEET_NUMBER);
        Iterator<Row> iterator = datatypeSheet.iterator();
        Row currentRow;
        int numCategories = 14;
        int numIRPF = 49;
        int numThreeYears = 18;
        int startThreeYears = 18;
        int genQuotEmp = 17;
        int disempQuotEmp = 18;
        int formationEmp = 19;
        int commonContingencies = 20;
        int fogasa = 21;
        int disempEntr = 22;
        int formationEntr = 23;
        int accidentsEntr = 24;

        /**
         * Categories *
         */
        if (iterator.hasNext()) {
            /* skip headers row */
            iterator.next();
        }

        while (iterator.hasNext()) {
            currentRow = iterator.next();

            /* Categories */
            if (currentRow.getRowNum() <= numCategories) {
                Category cat = new Category(
                        getStringValueOf(currentRow.getCell(
                                getColumnIndexOf("CategoryName", "category"))),
                        getDoubleValueOf(currentRow.getCell(
                                getColumnIndexOf("BasePayroll", "category"))),
                        getDoubleValueOf(currentRow.getCell(
                                getColumnIndexOf("Complements", "category")))
                );
                this.info.addCategory(cat);
            }

            /* IRPF */
            if (currentRow.getRowNum() <= numIRPF) {
                this.info.addIRPF(
                        getDoubleValueOf(currentRow.getCell(
                                    getColumnIndexOf("YearlyGross", "irpf"))),
                        getDoubleValueOf(currentRow.getCell(
                                    getColumnIndexOf("Retention", "irpf")))
                );
            }

            /* ThreeYears */
            if (currentRow.getRowNum() >= startThreeYears
                    && currentRow.getRowNum() < numThreeYears
                    + startThreeYears) {
                this.info.addThreeYear(getDoubleValueOf(
                        currentRow.getCell(
                                getColumnIndexOf("GrossAmount", "threeyear"))));
            }

            /* Other data */
            if (currentRow.getRowNum() == genQuotEmp) {
                this.info.setGenQuotEmp(getDoubleValueOf(
                        currentRow.getCell(getColumnIndexOf("Percent",
                                        "percent"))));
            }

            if (currentRow.getRowNum() == disempQuotEmp) {
                this.info.setDisempQuotEmp(getDoubleValueOf(
                        currentRow.getCell(getColumnIndexOf("Percent",
                                        "percent"))));
            }

            if (currentRow.getRowNum() == formationEmp) {
                this.info.setFormationEmp(getDoubleValueOf(
                        currentRow.getCell(getColumnIndexOf("Percent",
                                        "percent"))));
            }

            if (currentRow.getRowNum() == commonContingencies) {
                this.info.setCommonContingencies(getDoubleValueOf(
                        currentRow.getCell(getColumnIndexOf("Percent",
                                        "percent"))));
            }

            if (currentRow.getRowNum() == fogasa) {
                this.info.setFogasa(getDoubleValueOf(
                        currentRow.getCell(getColumnIndexOf("Percent",
                                        "percent"))));
            }

            if (currentRow.getRowNum() == disempEntr) {
                this.info.setDisempEntr(getDoubleValueOf(
                        currentRow.getCell(getColumnIndexOf("Percent",
                                        "percent"))));
            }

            if (currentRow.getRowNum() == formationEntr) {
                this.info.setFormationEntr(getDoubleValueOf(
                        currentRow.getCell(getColumnIndexOf("Percent",
                                        "percent"))));
            }

            if (currentRow.getRowNum() == accidentsEntr) {
                this.info.setAccidentsEntr(getDoubleValueOf(
                        currentRow.getCell(getColumnIndexOf("Percent",
                                        "percent"))));
            }
        }
    }

    /**
     * Reads the file specified in the in the filePath attribute.
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void readFile() throws FileNotFoundException, IOException {
        FileInputStream excFile = new FileInputStream(new File(this.filePath));
        this.workbook = new XSSFWorkbook(excFile);
        excFile.close();
    }

    /**
     * Iterates over the rows and cells of the document and materializes
     * employees.
     *
     */
    public void iterateDocument() {
        Sheet datatypeSheet = workbook.getSheetAt(EMPLOYEE_SHEET_NUMBER);
        Iterator<Row> iterator = datatypeSheet.iterator();

        if (iterator.hasNext()) {
            /* skip headers row */
            iterator.next();
        }

        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            Employee employee = initEmployee(currentRow);
            this.employees.add(employee);

        }
    }

    /**
     * Performs the correction of the ID, the correction of the bank account
     * code, the calculation of the IBAN, the generation of the email and the
     * generation of the payroll.
     *
     * @param month
     * @param year
     */
    public void performOperations(int month, int year) {
        for (Employee employee : this.employees) {
            /* validate ID */
            validateID(employee);

            /* validate bank account and calculate IBAN */
            calculateIBAN(employee);

            /* Generate email */
            generateEmail(employee);

            /* Generate payroll */
            generatePayroll(employee, month, year);

            this.month = month;
            this.year = year;
            /*Used for decide if save extra Payrolls or if an employee was in 
             the company this year*/
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
                        getColumnIndexOf("ID", "column"), employee.getID()
                        .toString());

            }
            employee.setNifnie(employee.getID().toString());
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
                    getColumnIndexOf("IBAN", "column"), emp.getIBAN());
            if (corrected) {
                /* Account code was wrong */
                updateCellValue(emp.getRowID(),
                        getColumnIndexOf("AccountCode", "column"),
                        emp.getCorrectedAccountCode());
                addEmployeeXML(emp, "account");
                LOGGER.info("Row number: "
                        + String.valueOf(emp.getRowID() + 1)
                        + " contains a wrong account code ");
            }
            emp.setCodigoCuenta(emp.getbAC().getCode());
            emp.setIban(emp.getIBAN());

        }

    }

    /**
     * Generates an email for the employee and stores it in the excel file.
     *
     * @param employee
     */
    public void generateEmail(Employee employee) {
        int emailIndex = getColumnIndexOf("Email", "column");

        String email = employee.generateEmail();
        updateCellValue(employee.getRowID(), emailIndex, email);
    }

    /**
     * Performs the needed calculus to generate the payroll of the employee for
     * the given month and year.
     *
     * @param employee
     * @param month
     * @param year
     */
    public void generatePayroll(Employee employee, int month, int year) {
        PDF pdf = null, pdfExtra;
        ID id = employee.getID();
        int entryYear = employee.getCompanyEntryLocalDate().getYear();
        int entryMonth = employee.getCompanyEntryLocalDate().getMonth().
                getValue();
        if (!id.isEmpty() && (year > entryYear
                || (year == entryYear && month >= entryMonth))) {

            try {
                employee.calculatePayroll(month, year);
                pdf = new PDF(employee, month, year, false);
                pdf.print(PDF_PATH + id.toString() + "_" + employee.getName()
                        + employee.getSurname1() + employee.getSurname2() + "_"
                        + pdf.getMonthName(month) + year + ".pdf");

                if ((month == 6 || month == 12)
                        && employee.getExtraProRate().equals("NO")) {
                    employee.calculateExtra(month, year);
                    pdfExtra = new PDF(employee, month, year, true);
                    pdfExtra.print(PDF_PATH + id.toString() + "_"
                            + employee.getName()
                            + employee.getSurname1() + employee.getSurname2()
                            + "_"
                            + pdf.getMonthName(month) + year + "Extra.pdf");
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ExcelController.class.getName()).log(
                        Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                Logger.getLogger(ExcelController.class.getName()).log(
                        Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ExcelController.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Saves the changes made in the excel file.
     *
     * @throws IOException
     */
    public void updateFile() throws IOException {

        FileOutputStream outputStream = new FileOutputStream(this.filePath);
        this.workbook.write(outputStream);
        outputStream.close();
    }

    /**
     * Closes the workbook.
     *
     * @throws IOException
     */
    public void closeWorkbook() throws IOException {
        this.workbook.close();
    }

    /**
     * Adds an employee and its data such as name, surname, company name... to
     * the specified XML.
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
            values[3] = employee.getCategory().getCategoryName();
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
        outputAccount.output(accountErrors, new FileWriter(
                ACCOUNT_ERRORS_PATH));
    }

    /**
     * Materializes an employee from the excel File.
     *
     * @param row
     * @return the emloyee object initialized
     */
    public Employee initEmployee(Row row) {

        int IDIndex = getColumnIndexOf("ID", "column");
        int accountCodeIndex = getColumnIndexOf("AccountCode", "column");
        int countryCodeIndex = getColumnIndexOf("CountryCode", "column");
        int ibanIndex = getColumnIndexOf("IBAN", "column");
        int nameIndex = getColumnIndexOf("Name", "column");
        int surname1Index = getColumnIndexOf("Surname1", "column");
        int surname2Index = getColumnIndexOf("Surname2", "column");
        int categoryIndex = getColumnIndexOf("Category", "column");
        int companyNameIndex = getColumnIndexOf("CompanyName", "column");
        int cifIndex = getColumnIndexOf("CIFCompany", "column");
        int companyEntryDateIndex = getColumnIndexOf("CompanyEntryDate",
                "column");
        int entryDateIndex = getColumnIndexOf("EntryDate", "column");
        int leavingDateIndex = getColumnIndexOf("LeavingDate", "column");
        int proRateIndex = getColumnIndexOf("ProrateExtra", "column");
        int forcedEHoursIndex = getColumnIndexOf("ForcedExtraHours", "column");
        int volEHoursIndex = getColumnIndexOf("VolunteerExtraHours", "column");

        Company comp;
        LocalDate companyEntryDate;
        LocalDate entryDate;
        LocalDate leavingDate;
        DateTimeFormatter formatter;
        String proRate;
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
        /* Category */
        employee.setCategory(this.info.getCategory(getStringValueOf(row.getCell(
                categoryIndex,
                MissingCellPolicy.CREATE_NULL_AS_BLANK))));
        /* Company */
        comp = this.info.getCompany(getStringValueOf(row.getCell(
                companyNameIndex, MissingCellPolicy.CREATE_NULL_AS_BLANK)),
                getStringValueOf(row.getCell(cifIndex,
                                MissingCellPolicy.CREATE_NULL_AS_BLANK)));

        employee.setCompany(comp);
        /* Dates */
        formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        companyEntryDate = LocalDate.parse(
                getDateValueOf(row.getCell(companyEntryDateIndex,
                                MissingCellPolicy.CREATE_NULL_AS_BLANK)),
                formatter);
        /*entryDate = dateFormat.parse(getStringValueOf(row.getCell(
         entryDateIndex, MissingCellPolicy.CREATE_NULL_AS_BLANK)));
         leavingDate = dateFormat.parse(getStringValueOf(row.getCell(
         leavingDateIndex, MissingCellPolicy.CREATE_NULL_AS_BLANK)));
         */
        employee.setCompanyEntryLocalDate(companyEntryDate);
        employee.setCompanyEntryDate(Date.from(companyEntryDate.atStartOfDay(
                ZoneId.systemDefault()).toInstant()));/*
         employee.setEntryDate(entryDate);
         employee.setLeavingDate(leavingDate);*/
        /* Extra Hours */

        int hours = getIntValueOf(row.getCell(forcedEHoursIndex,
                MissingCellPolicy.CREATE_NULL_AS_BLANK));
        employee.setForcedEHours(hours);

        hours = getIntValueOf(row.getCell(volEHoursIndex,
                MissingCellPolicy.CREATE_NULL_AS_BLANK));
        employee.setVolEHours(hours);

        /* Extra Pro Rate */
        proRate = getStringValueOf(row.getCell(
                proRateIndex, MissingCellPolicy.CREATE_NULL_AS_BLANK));
        employee.setExtraProRate(proRate);

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
     * Returns the string value of a date cell in the format dd/MM/yyyy
     *
     * @param cell
     * @return the date as String
     */
    public String getDateValueOf(Cell cell) {
        String cellValue = "";
        Date cellDate;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        cellDate = cell.getDateCellValue();
        cellValue = df.format(cellDate);
        return cellValue;
    }

    /**
     * Returns the int value of a cell.
     *
     * @param cell
     * @return the numeric value.
     */
    public int getIntValueOf(Cell cell) {
        int cellValue = 0;

        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            cellValue = (int) cell.getNumericCellValue();
        }

        return cellValue;
    }

    /**
     * Returns the double value of a cell.
     *
     * @param cell
     * @return the numeric value.
     */
    public double getDoubleValueOf(Cell cell) {
        double cellValue = 0.0;

        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            cellValue = (double) cell.getNumericCellValue();
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
        Sheet datatypeSheet = workbook.getSheetAt(EMPLOYEE_SHEET_NUMBER);

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
        Sheet datatypeSheet = workbook.getSheetAt(EMPLOYEE_SHEET_NUMBER);
        Cell cell = datatypeSheet.getRow(row).getCell(col,
                MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return cell.getStringCellValue();
    }

    /**
     * Saves the employees, categories, payrolls and companies into the database
     */
    public void saveDatabase() {
        SessionFactory sF = null;
        Session session = null;

        try {
            sF = NewHibernateUtil.getSessionFactory();
            session = sF.openSession();

            saveCategories(session);

            saveEmployees(session);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
            if (!sF.isClosed()) {
                sF.close();
            }
            NewHibernateUtil.closeServiceRegistry();
        }

    }

    /**
     * Saves or updates the categories in the database
     *
     * @param session
     */
    public void saveCategories(Session session) {
        CategoryDAO catDAO = new CategoryDAO();
        Category updatedCat;
        ArrayList<Category> categories = this.info.getCategories();
        for (Category cat : categories) {
            if (catDAO.exists(session, cat)) {
                updatedCat = catDAO.update(session, cat);
                this.info.updateCategory(updatedCat);

            } else {
                catDAO.save(session, cat);
            }

        }

    }

    /**
     * Saves or updates the employees in the database
     *
     * @param session
     */
    public void saveEmployees(Session session) {
        EmployeeDAO empDAO = new EmployeeDAO();
        Employee updatedEmp;
        int entryYear, entryMonth;
        for (Employee emp : employees) {
            entryYear = emp.getCompanyEntryLocalDate().getYear();
            entryMonth = emp.getCompanyEntryLocalDate().getMonth().getValue();
            emp.updateCategory();

            if (!emp.getID().isEmpty()) {
                saveCompany(session, emp);
                if (!empDAO.exists(session, emp)) {
                    empDAO.save(session, emp);
                } else {
                    //System.out.println("dup"+emp.getName());
                    updatedEmp = empDAO.update(session, emp);
                    emp.getPayroll().setEmployee(updatedEmp);
                    emp.getExtraPayroll().setEmployee(updatedEmp);
                }
                if ((year > entryYear
                        || (year == entryYear && month >= entryMonth))) {
                    savePayroll(session, emp.getPayroll());
                    if ((this.month == 6 || this.month == 12) && 
                            emp.getExtraProRate().equals("NO")) {
                        savePayroll(session, emp.getExtraPayroll());
                    }

                }
            }

        }

    }

    /**
     * Saves or updates the company of emp in the database
     *
     * @param session
     * @param emp
     */
    public void saveCompany(Session session, Employee emp) {
        CompanyDAO comDAO = new CompanyDAO();
        Company updatedCom;
        Company com = emp.getCompany();

        if (!comDAO.exists(session, com)) {
            comDAO.save(session, com);
        } else {
            updatedCom = comDAO.update(session, com);
            emp.setCompany(updatedCom);
        }

    }

    /**
     * Saves or updates a payroll in the database
     *
     * @param session
     * @param payroll
     */
    public void savePayroll(Session session, Payroll payroll) {
        PayrollDAO pDAO = new PayrollDAO();
        Payroll updatedPayroll;

        if (!pDAO.exists(session, payroll)) {
            pDAO.save(session, payroll);
        } else {
            updatedPayroll = pDAO.update(session, payroll);
            payroll.getEmployee().setPayroll(updatedPayroll);
        }
    }
}
