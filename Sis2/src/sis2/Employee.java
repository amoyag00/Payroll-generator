/**
 * Class used to materialize an employee from the excel file.
 */
package sis2;

/**
 *
 * @version 2.0 05/04/2018
 * @author Alejandro Moya García
 */
public class Employee {

    private ID id;
    private BankAccountCode bAC;
    private String name;
    private String surname1;
    private String surname2;
    private String category;
    private String companyName;
    private String email;
    int rowID;

    public Employee(int rowID) {
        this.rowID = rowID;
    }

    public void setID(ID id) {
        this.id = id;
    }

    public void setBankAccount(BankAccountCode bAC) {
        this.bAC = bAC;
    }

    public ID getID() {
        return id;
    }

    public BankAccountCode getbAC() {
        return bAC;
    }

    public int getRowID() {
        return rowID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname1() {
        return surname1;
    }

    public void setSurname1(String surname1) {
        this.surname1 = surname1;
    }

    public String getSurname2() {
        return surname2;
    }

    public void setSurname2(String surname2) {
        this.surname2 = surname2;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getCorrectedAccountCode() {
        return this.bAC.getCode();
    }

    public String getWrongCode() {
        return this.bAC.getWrongCode();
    }

    public void calculateIBAN() {
        this.bAC.calculateIBAN();
    }

    public String getIBAN() {
        return this.bAC.getIBAN();
    }

    /**
     * Tells if ID is empty or not.
     *
     * @return true if ID is empty, false otherwise.
     */
    public boolean hasEmptyID() {
        return this.id.isEmpty();
    }

    /**
     * Corrects an ID by calling an ID object.
     *
     * @return true if it was corrected, flase otherwise
     */
    public boolean correctID() {
        return this.id.correct();
    }

    /**
     * Corrects an account code by calling a BankAccountCode object
     *
     * @return true if it was corrected, flase otherwise
     */
    public boolean correctAccountCode() {
        return this.bAC.correct();
    }

    /**
     * Generates an email based on the employee's name, first surname, second
     * surname (if any) and company name by calling an Emailgenerator.
     *
     * @return
     */
    public String generateEmail() {
        this.email = EmailGenerator.getInstance().generateEmail(this.name,
                this.surname1, this.surname2, this.companyName);
        return this.email;
    }

}
