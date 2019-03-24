/**
 * Class used to materialize an employee from the excel file.
 */
package sis2;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Transient;

/**
 *
 * @version 4.0 02/06/2018
 * @author Alejandro Moya García
 */
public class Employee {

    private Integer idEmployee;
    private Category category;
    private Company company;
    private String name;
    private String surname1;
    private String surname2;
    @Transient
    private ID id;
    private String nifnie;
    private String email;
    private Date companyEntryDate;
    @Transient
    private LocalDate companyEntryLocalDate;
    private BankAccountCode bAC;
    private String codigoCuenta;
    private String iban;
    private Set nominas = new HashSet(0);

    private int forcedEHours;
    private int volEHours;
    private String extraProRate;
    private int rowID;
    private PayrollInformation info;
    private Payroll payroll;
    private Payroll extraPayroll;

    public Employee() {

    }

    public Employee(int rowID) {
        this.rowID = rowID;
        this.company = new Company();
        this.payroll = new Payroll();
        this.payroll.setEmployee(this);
        this.extraPayroll = new Payroll();
        this.extraPayroll.setEmployee(this);
        this.info = PayrollInformation.getInstance();
    }

    public Employee(Category category, Company company, String name,
            String surname1, String nifnie) {
        this.category = category;
        this.company = company;
        this.name = name;
        this.surname1 = surname1;
        this.nifnie = nifnie;
    }

    public Employee(Category category, Company company, String name,
            String surname1, String surname2, String nifnie, String email,
            Date companyEntryDate, String codigoCuenta, String iban, 
            Set nominas) {
        this.category = category;
        this.company = company;
        this.name = name;
        this.surname1 = surname1;
        this.surname2 = surname2;
        this.nifnie = nifnie;
        this.email = email;
        this.companyEntryDate = companyEntryDate;
        this.codigoCuenta = codigoCuenta;
        this.iban = iban;
        this.nominas = nominas;
    }

    public Integer getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Integer idEmployee) {
        this.idEmployee = idEmployee;
    }

    public Set getNominas() {
        return nominas;
    }

    public void setNominas(Set nominas) {
        this.nominas = nominas;
    }

    @Transient
    public void setID(ID id) {
        this.id = id;
    }

    public void setBankAccount(BankAccountCode bAC) {
        this.bAC = bAC;
    }

    @Transient
    public ID getID() {
        return id;
    }

    public String getNifnie() {
        return this.nifnie;
    }

    public BankAccountCode getbAC() {
        return bAC;
    }

    public String getCodigoCuenta() {
        return codigoCuenta;
    }

    public Company getCompany() {
        return company;
    }

    public void setNifnie(String nifnie) {
        this.nifnie = nifnie;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCodigoCuenta(String codigoCuenta) {
        this.codigoCuenta = codigoCuenta;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setPayroll(Payroll payroll) {
        this.payroll = payroll;
    }

    public String getIban() {
        return this.iban;
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

    public Payroll getExtraPayroll() {
        return extraPayroll;
    }

    public void setExtraPayroll(Payroll extraPayroll) {
        this.extraPayroll = extraPayroll;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getCompanyName() {
        return this.company.getCompanyName();
    }

    public void setCompanyName(String companyName) {
        this.company.setCompanyName(companyName);
    }

    public String getEmail() {
        return this.email;
    }

    public Payroll getPayroll() {
        return this.payroll;
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

    public void setCompanyEntryLocalDate(LocalDate companyEntryLocalDate) {
        this.companyEntryLocalDate = companyEntryLocalDate;
    }

    public void setCompanyEntryDate(Date companyEntryDate) {
        this.companyEntryDate = companyEntryDate;
    }

    public String getCompanyEntryDateString() {
        return this.companyEntryDate.toString();
    }

    public LocalDate getCompanyEntryLocalDate() {
        return this.companyEntryLocalDate;
    }

    public Date getCompanyEntryDate() {
        return this.companyEntryDate;

    }

    public void setExtraProRate(String extraProRate) {
        this.extraProRate = extraProRate;
    }

    public String getExtraProRate() {
        return extraProRate;
    }

    public int getForcedEHours() {
        return forcedEHours;
    }

    public void setForcedEHours(int forcedEHours) {
        this.forcedEHours = forcedEHours;
    }

    public int getVolEHours() {
        return volEHours;
    }

    public void setVolEHours(int volEHours) {
        this.volEHours = volEHours;
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
     * @return the email
     */
    public String generateEmail() {
        this.email = EmailGenerator.getInstance().generateEmail(this.name,
                this.surname1, this.surname2, this.company.getCompanyName());
        return this.email;
    }

    public String getCompanyCIF() {
        return this.company.getCIF();
    }

    /**
     * Calculates the payroll for the given month and year.
     *
     * @param month
     * @param year
     */
    public void calculatePayroll(int month, int year) {

        this.payroll.setMonth(month);
        this.payroll.setYear(year);
        this.payroll.setNumThreeYears(this.calculateThreeYears(month, year));

        calculateYearlyGross(month, year);
        calculateMonthly(month, year);
    }

    /**
     * Calculates the gross earned money by an employee for the given month and
     * year.
     *
     * @param month
     * @param year
     */
    public void calculateYearlyGross(int month, int year) {
        double seniorityMoney = 0;
        /*double basePayrol = this.info.getPayrollOf(this.category);
         double complements = this.info.getComplementOf(this.category);*/
        double basePayrol = this.category.getBasePayroll();
        double complements = this.category.getComplement();

        if (this.companyEntryLocalDate.getYear() - year == 0) {
            //payroll of the first year
            this.payroll.setYearlyGross(calculateFirstYear());
        } else if ((this.companyEntryLocalDate.getYear() - year) % 3 == 0) {
            //this year seniority increases
            seniorityMoney = seniorityUpYear(year);
            this.payroll.setYearlyGross(basePayrol + complements
                    + seniorityMoney);
        } else {//this year seniority remains the same all the year
            seniorityMoney = this.info.getMoneyThreeYear(
                    calculateThreeYears(month, year)) * 14;
            this.payroll.setYearlyGross(basePayrol + complements
                    + seniorityMoney);
        }
        this.payroll.setIrpf(this.info.getIRPFFor(
                this.payroll.getYearlyGross()));
    }

    /**
     * Calculates the num of three year periods. The three year period is
     * achieved the month after the entry month. for 01/01/2001 the three year
     * period is achieved in 01/02/2004
     *
     * @param payrollMonth
     * @param payrollYear
     * @return the number of three year periods
     */
    public int calculateThreeYears(int payrollMonth, int payrollYear) {
        int entryYear = this.companyEntryLocalDate.getYear();
        int entryMonth = this.companyEntryLocalDate.getMonth().getValue();
        int diff = payrollYear - entryYear;
        if (entryMonth > payrollMonth || entryMonth == payrollMonth) {
            diff--;
            if (diff == -1) {
                diff = 0;
            }

        }

        return (int) Math.floor(diff / 3.0);

    }

    /**
     * Calculates the yearlyGross for the first year of the employee.
     *
     * @return the yearly gross for the first year.
     */
    public double calculateFirstYear() {
        int entryMonth;
        double totalMoney = 0.0;
        double basePayroll = this.category.getBasePayroll();
        double complements = this.category.getComplement();

        entryMonth = this.companyEntryLocalDate.getMonth().getValue();
        if (this.extraProRate.equals("SI")) {
            totalMoney = (basePayroll + complements)
                    * ((12.0 - entryMonth + 1) / 12.0);
        } else if (this.extraProRate.equals("NO")) {
            totalMoney = ((basePayroll + complements) / 14.0)
                    * (12 - entryMonth + 1);
            //June extra
            if (entryMonth < 6) {
                //Not -1 because june extra includes december
                totalMoney += ((basePayroll + complements) / 14.0)
                        * ((6.0 - entryMonth) / 6.0);

                /* December extra is full because months from june to november 
                 are fully worked */
                totalMoney += (basePayroll + complements) / 14.0;
            } else {
                //Employee has entered after June so there is no extra June
                totalMoney += ((basePayroll + complements) / 14.0)
                        * ((12.0 - entryMonth) / 6.0);
            }
        }
        return totalMoney;
    }

    /**
     * Calculates the seniority for the year that seniority increases.
     *
     * @param year
     * @return the seniority for the year seniority increases.
     */
    public double seniorityUpYear(int year) {
        double seniorityBefore;
        double seniorityAfter;
        double moneyBefore, moneyAfter;
        double seniorityMoney;
        int entryMonth = 0;
        entryMonth = this.companyEntryLocalDate.getMonth().getValue();
        moneyBefore = this.info.getMoneyThreeYear(
                calculateThreeYears(entryMonth, year));
        moneyAfter = this.info.getMoneyThreeYear(
                calculateThreeYears(entryMonth + 1, year));
        // It Does not matter to be month 13

        seniorityBefore = entryMonth * moneyBefore;
        seniorityAfter = (12 - entryMonth) * moneyAfter;
        seniorityMoney = seniorityBefore + seniorityAfter;
        if (entryMonth >= 6) {//June extra
            seniorityMoney += moneyBefore;
        } else {
            seniorityMoney += moneyAfter;
        }

        if (entryMonth == 12) {//December extra 
            seniorityMoney += moneyBefore;
        } else {
            seniorityMoney += moneyAfter;
        }
        return seniorityMoney;
    }

    /**
     * Calculates the money earned for the specified month and year.
     *
     * @param month
     * @param year
     */
    public void calculateMonthly(int month, int year) {
        double monthlyBase;
        double monthlyComplement;
        double seniority;
        double yearlyBase = this.category.getBasePayroll();
        double yearlyComplement = this.category.getComplement();
        double proRateValue = 0.0;
        double irpfRetention;
        double regulatoryBase;
        double monthlyGross;
        double liquidMoney;
        double seniorityExtra;

        monthlyBase = yearlyBase / 14.0;
        monthlyComplement = yearlyComplement / 14.0;
        if (calculateThreeYears(month, year) == -1) {
            System.out.println("Error en " + this.name + this.surname1);
        }
        seniority = this.info.getMoneyThreeYear(
                calculateThreeYears(month, year));
        this.payroll.setMoneyThreeYears(seniority);
        this.payroll.setMonthlySalary(monthlyBase);
        this.payroll.setMonthlyComplement(monthlyComplement);
        
        if (month < 6 || month == 12) {
                seniorityExtra = this.info.getMoneyThreeYear(
                        calculateThreeYears(6, year));
            } else {
                seniorityExtra = this.info.getMoneyThreeYear(
                        calculateThreeYears(12, year));
            }

        if (this.extraProRate.equals("SI")) {
            proRateValue = (monthlyBase + monthlyComplement + seniorityExtra) / 6.0;
        } else if (this.extraProRate.equals("NO")) {
            proRateValue = 0.0;
        }
        this.payroll.setProRateValue(proRateValue);
        monthlyGross = monthlyBase + monthlyComplement + seniority
                + proRateValue;
        this.payroll.setMonthlyGross(monthlyGross);

        irpfRetention = this.payroll.getIrpf();
        this.payroll.setIrpfMoney(
                (monthlyBase + monthlyComplement + seniority + proRateValue)
                * (irpfRetention / 100.0));

        regulatoryBase = monthlyBase + monthlyComplement + seniority
                + proRateValue;
        if (this.extraProRate.equals("NO")) {
            regulatoryBase += (monthlyBase + monthlyComplement + seniorityExtra)
                    / 6.0;
        }
        this.payroll.setRegulatoryBase(regulatoryBase);
        calculateDiscountsEntr(regulatoryBase);
        calculateDiscountsEmp(regulatoryBase);
        liquidMoney = monthlyGross
                - this.payroll.getGeneralContingenciesMoneyEmp()
                - this.payroll.getDisempMoneyEmp()
                - this.payroll.getFormationMoneyEmp()
                - this.payroll.getIrpfMoney();
        this.payroll.setLiquid(liquidMoney);

    }

    /**
     * Calculates the money earned in the given month and year in the extra
     *
     * @param month
     * @param year
     */
    public void calculateMonthlyExtra(int month, int year) {
        double monthlyBase;
        double monthlyComplement;
        double seniority;
        double yearlyBase = this.category.getBasePayroll();
        double yearlyComplement = this.category.getComplement();
        double proRateValue = 0.0;
        double irpfRetention;
        double regulatoryBase;
        double monthlyGross;
        double liquidMoney;

        double generalContingencies;
        double disempQuotEmp;
        double formationEmp;

        double commonContingencies;
        double disempEntr;
        double formationEntr;
        double accidentsEntr;
        double fogasaEntr;

        int entryMonth = this.companyEntryLocalDate.getMonth().getValue();
        int entryYear = this.companyEntryLocalDate.getYear();
        monthlyBase = yearlyBase / 14.0;
        monthlyComplement = yearlyComplement / 14.0;
        seniority = this.info.getMoneyThreeYear(
                calculateThreeYears(month, year));
        if (entryYear == year) {
            if (month == 6 && entryMonth <= 6) {
                monthlyBase = monthlyBase * (6 - entryMonth) / 6.0;
                monthlyComplement = monthlyComplement * (6 - entryMonth) / 6.0;
            } else if (month == 12 && entryMonth > 6 && entryMonth <= 12) {
                monthlyBase = monthlyBase * (12 - entryMonth) / 6.0;
                monthlyComplement = monthlyComplement * (12 - entryMonth) / 6.0;
            }
        }
        this.extraPayroll.setMoneyThreeYears(seniority);
        this.extraPayroll.setMonthlySalary(monthlyBase);
        this.extraPayroll.setMonthlyComplement(monthlyComplement);

        monthlyGross = monthlyBase + monthlyComplement + seniority;
        this.extraPayroll.setMonthlyGross(monthlyGross);

        irpfRetention = this.extraPayroll.getIrpf();
        this.extraPayroll.setIrpfMoney(
                (monthlyBase + monthlyComplement + seniority + proRateValue)
                * (irpfRetention / 100.0));

        regulatoryBase = 0;

        this.extraPayroll.setRegulatoryBase(regulatoryBase);
        //calculateDiscountsEntr(regulatoryBase);
        //calculateDiscountsEmp(regulatoryBase);
        liquidMoney = monthlyGross
                - this.extraPayroll.getGeneralContingenciesMoneyEmp()
                - this.extraPayroll.getDisempMoneyEmp()
                - this.extraPayroll.getFormationMoneyEmp()
                - this.extraPayroll.getIrpfMoney();
        this.extraPayroll.setLiquid(liquidMoney);

        this.extraPayroll.setTotalCostEntr(
                this.extraPayroll.getFogasaMoneyEntr()
                + this.extraPayroll.getAccidentsMoneyEntr()
                + this.extraPayroll.getFormationMoneyEntr()
                + this.extraPayroll.getDisempMoneyEntr()
                + this.extraPayroll.getCommonContingenciesMoneyEntr()
                + this.extraPayroll.getMonthlyGross());

        generalContingencies = this.info.getGenQuotEmp();
        this.extraPayroll.setGeneralContingenciesEmp(generalContingencies);

        disempQuotEmp = this.info.getDisempQuotEmp();
        this.extraPayroll.setDisempEmp(disempQuotEmp);

        formationEmp = this.info.getFormationEmp();
        this.extraPayroll.setFormationEmp(formationEmp);

        commonContingencies = this.info.getCommonContingencies();
        this.extraPayroll.setCommonContingenciesEntr(commonContingencies);

        disempEntr = this.info.getDisempEntr();
        this.extraPayroll.setDisempEntr(disempEntr);

        formationEntr = this.info.getFormationEntr();
        this.extraPayroll.setFormationEntr(formationEntr);

        accidentsEntr = this.info.getAccidentsEntr();
        this.extraPayroll.setAccidentsEntr(accidentsEntr);

        fogasaEntr = this.info.getFogasa();
        this.extraPayroll.setFogasaEntr(fogasaEntr);

    }

    /**
     * Calculates the value of the extra prorate for the given month and year.
     *
     * @param month
     * @param year
     * @return
     */
    public double calculateExtraProrate(int month, int year) {
        double extra = 0.0;
        int entryMonth = this.companyEntryLocalDate.getMonth().getValue();
        int entryYear = this.companyEntryLocalDate.getYear();
        double basePayroll = this.category.getBasePayroll();
        double complements = this.category.getComplement();
        double seniority = 0.0;

        if (month == 6) {
            if (entryMonth < 6 && entryYear == year) {
                //Not -1 because june extra includes december
                extra = ((basePayroll + complements) / 14.0)
                        * (6.0 - entryMonth) / 6.0;
            } else {
                extra = ((basePayroll + complements) / 14.0);
            }
        } else if (month == 12) {
            if (entryYear == year && entryMonth > 6) {
                extra = ((basePayroll + complements) / 14.0)
                        * (12.0 - entryMonth) / 6.0;
            } else {
                extra = ((basePayroll + complements) / 14.0);
            }
        }

        //Seniority
        seniority = this.info.getMoneyThreeYear(
                calculateThreeYears(month, year));
        //this.payroll.setMoneyThreeYears(seniority);
        extra += seniority;
        return extra;
    }

    /**
     * Calculates the additional costs the entrepreneur has to pay.
     *
     * @param regulatoryBase
     */
    public void calculateDiscountsEntr(double regulatoryBase) {
        double commonContingencies;
        double disempEntr;
        double formationEntr;
        double accidentsEntr;
        double fogasaEntr;

        commonContingencies = this.info.getCommonContingencies();
        this.payroll.setCommonContingenciesEntr(commonContingencies);
        this.payroll.setCommonContingenciesMoneyEntr(regulatoryBase
                * (commonContingencies / 100.0));

        disempEntr = this.info.getDisempEntr();
        this.payroll.setDisempEntr(disempEntr);
        this.payroll.setDisempMoneyEntr(regulatoryBase * (disempEntr / 100.0));

        formationEntr = this.info.getFormationEntr();
        this.payroll.setFormationEntr(formationEntr);
        this.payroll.setFormationMoneyEntr(
                regulatoryBase * (formationEntr / 100.0));

        accidentsEntr = this.info.getAccidentsEntr();
        this.payroll.setAccidentsEntr(accidentsEntr);
        this.payroll.setAccidentsMoneyEntr(
                regulatoryBase * (accidentsEntr / 100.0));

        fogasaEntr = this.info.getFogasa();
        this.payroll.setFogasaEntr(fogasaEntr);
        this.payroll.setFogasaMoneyEntr(regulatoryBase * (fogasaEntr / 100.0));

        this.payroll.setTotalCostEntr(this.payroll.getFogasaMoneyEntr()
                + this.payroll.getAccidentsMoneyEntr()
                + this.payroll.getFormationMoneyEntr()
                + this.payroll.getDisempMoneyEntr()
                + this.payroll.getCommonContingenciesMoneyEntr()
                + this.payroll.getMonthlyGross());

    }

    /**
     * Calculates the additional costs retrieved from the monthly gross.
     *
     * @param regulatoryBase
     */
    public void calculateDiscountsEmp(double regulatoryBase) {
        double generalContingencies;
        double disempQuotEmp;
        double formationEmp;

        generalContingencies = this.info.getGenQuotEmp();
        this.payroll.setGeneralContingenciesEmp(generalContingencies);
        this.payroll.setGeneralContingenciesMoneyEmp(regulatoryBase
                * (generalContingencies / 100.0));

        disempQuotEmp = this.info.getDisempQuotEmp();
        this.payroll.setDisempEmp(disempQuotEmp);
        this.payroll.setDisempMoneyEmp(regulatoryBase
                * (disempQuotEmp / 100.0));

        formationEmp = this.info.getFormationEmp();
        this.payroll.setFormationEmp(formationEmp);
        this.payroll.setFormationMoneyEmp(regulatoryBase
                * (formationEmp / 100.0));

    }

    /**
     * Calculates the money earned in the extra.
     *
     * @param month
     * @param year
     */
    public void calculateExtra(int month, int year) {
        this.extraPayroll.setMonth(month);
        this.extraPayroll.setYear(year);
        this.extraPayroll.setNumThreeYears(
                this.calculateThreeYears(month, year));

        this.extraPayroll.setYearlyGross(this.payroll.getYearlyGross());
        this.extraPayroll.setIrpf(this.payroll.getIrpf());
        calculateMonthlyExtra(month, year);
    }

    /**
     * Updates the category associated to an employee
     */
    public void updateCategory() {
        this.category = this.info.getCategory(this.category.getCategoryName());
    }

}
