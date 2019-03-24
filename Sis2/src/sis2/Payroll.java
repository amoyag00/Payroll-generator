/*
 * Class containing information about the payroll of an employee.
 */
package sis2;


/**
 *
 * @author Alejandro Moya García
 * @version 4.0 02/06/2018
 */
public class Payroll {
    
    private Integer idPayroll;
    private Employee employee;
    private int month;
    private int year;
    private int numThreeYears;
    private double moneyThreeYears;
    private double monthlySalary;
    private double monthlyComplement;
    private double proRateValue;
    private double yearlyGross;
    private double irpf;
    private double irpfMoney;
    private double regulatoryBase;
    
    private double commonContingenciesMoneyEntr;
    private double commonContingenciesEntr;
    private double disempEntr;
    private double disempMoneyEntr;
    private double formationEntr;
    private double formationMoneyEntr;
    private double accidentsEntr;
    private double accidentsMoneyEntr;
    private double fogasaEntr;
    private double fogasaMoneyEntr;
    
    private double generalContingenciesEmp;
    private double generalContingenciesMoneyEmp;
    private double disempEmp;
    private double disempMoneyEmp;
    private double formationEmp;
    private double formationMoneyEmp;
    private double monthlyGross;
    private double liquid;
    private double totalCostEntr;
    
    
    public Payroll(){
        
    }
    
    public Payroll(Employee employee, int month, int year, int numThreeYears, double moneyThreeYears,
            double monthlySalary, double monthlyComplement, double proRateValue, double yearlyGross,
            double irpf, double iprfMoney, double regulatoryBase, double commonContingenciesEntr,
            double commonContingenciesMoneyEntr, double disempEntr, double disempMoneyEntr,
            double formationEntr, double formationMoneyEntr, double accidentsEntr, 
            double accidentsMoneyEntr, double fogasaEntr, double fogasaMoneyEntr, 
            double generalContingenciesEmp, double generalContingenciesMoneyEmp, double disempEmp,
            double disempMoneyEmp, double formationEmp, double formationMoneyEmp, double monthlyGross,
            double liquid, double totalCostEntr) {
       this.employee = employee;
       this.month = month;
       this.year = year;
       this.numThreeYears = numThreeYears;
       this.moneyThreeYears = moneyThreeYears;
       this.monthlySalary = monthlySalary;
       this.monthlyComplement= monthlyComplement;
       this.proRateValue = proRateValue;
       this.yearlyGross = yearlyGross;
       this.irpf = irpf;
       this.irpfMoney = iprfMoney;
       this.regulatoryBase = regulatoryBase;
       this.commonContingenciesEntr = commonContingenciesEntr;
       this.commonContingenciesMoneyEntr = commonContingenciesMoneyEntr;
       this.disempEntr = disempEntr;
       this.disempMoneyEntr = disempMoneyEntr;
       this.formationEntr = formationEntr;
       this.formationMoneyEntr = formationMoneyEntr;
       this.accidentsEntr = accidentsEntr;
       this.accidentsMoneyEntr = accidentsMoneyEntr;
       this.fogasaEntr= fogasaEntr;
       this.fogasaMoneyEntr = fogasaMoneyEntr;
       this.generalContingenciesEmp = generalContingenciesEmp;
       this.generalContingenciesMoneyEmp = generalContingenciesMoneyEmp;
       this.disempEmp = disempEmp;
       this.disempMoneyEmp = disempMoneyEmp;
       this.formationEmp = formationEmp;
       this.formationMoneyEmp = formationMoneyEmp;
       this.monthlyGross = monthlyGross;
       this.liquid = liquid;
       this.totalCostEntr = totalCostEntr;
    }
    
    public double getLiquid() {
        return liquid;
    }

    public void setLiquid(double liquid) {
        this.liquid = liquid;
    }

    public double getCommonContingenciesMoneyEntr() {
        return commonContingenciesMoneyEntr;
    }

    public void setCommonContingenciesMoneyEntr(
            double commonContingenciesMoneyEntr) {
        this.commonContingenciesMoneyEntr = commonContingenciesMoneyEntr;
    }

    public double getCommonContingenciesEntr() {
        return commonContingenciesEntr;
    }

    public void setCommonContingenciesEntr(double commonContingenciesEntr) {
        this.commonContingenciesEntr = commonContingenciesEntr;
    }

    public double getDisempMoneyEmp() {
        return disempMoneyEmp;
    }

    public void setDisempMoneyEmp(double disempMoneyEmp) {
        this.disempMoneyEmp = disempMoneyEmp;
    }

    public double getFormationMoneyEmp() {
        return formationMoneyEmp;
    }

    public void setFormationMoneyEmp(double formationMoneyEmp) {
        this.formationMoneyEmp = formationMoneyEmp;
      
    }

    public double getGeneralContingenciesEmp() {
        return generalContingenciesEmp;
    }

    public void setGeneralContingenciesEmp(
            double generalContingenciesEmp) {
        this.generalContingenciesEmp = generalContingenciesEmp;
    }

    public double getGeneralContingenciesMoneyEmp() {
        return generalContingenciesMoneyEmp;
    }

    public void setGeneralContingenciesMoneyEmp(
            double generalContingenciesMoneyEmp) {
        this.generalContingenciesMoneyEmp = generalContingenciesMoneyEmp;
    }

    public double getDisempEmp() {
        return disempEmp;
    }

    public void setDisempEmp(double disempEmp) {
        this.disempEmp = disempEmp;
    }

    public double getFormationEmp() {
        return formationEmp;
    }

    public void setFormationEmp(double formationEmp) {
        this.formationEmp = formationEmp;
    }

    public double getMonthlyGross() {
        return monthlyGross;
    }

    public void setMonthlyGross(double monthlyGross) {
        this.monthlyGross = monthlyGross;
    }

    public double getDisempEntr() {
        return disempEntr;
    }

    public void setDisempEntr(double disempEntr) {
        this.disempEntr = disempEntr;
    }

    public double getDisempMoneyEntr() {
        return disempMoneyEntr;
    }

    public void setDisempMoneyEntr(double disempMoneyEntr) {
        this.disempMoneyEntr = disempMoneyEntr;
    }

    public double getFormationEntr() {
        return formationEntr;
    }

    public void setFormationEntr(double formationEntr) {
        this.formationEntr = formationEntr;
    }

    public double getFormationMoneyEntr() {
        return formationMoneyEntr;
    }

    public void setFormationMoneyEntr(double formationMoneyEntr) {
        this.formationMoneyEntr = formationMoneyEntr;
    }

    public double getAccidentsEntr() {
        return accidentsEntr;
    }

    public void setAccidentsEntr(double accidentsEntr) {
        this.accidentsEntr = accidentsEntr;
    }

    public double getAccidentsMoneyEntr() {
        return accidentsMoneyEntr;
    }

    public void setAccidentsMoneyEntr(double accidentsMoneyEntr) {
        this.accidentsMoneyEntr = accidentsMoneyEntr;
    }

    public double getFogasaEntr() {
        return fogasaEntr;
    }

    public void setFogasaEntr(double fogasaEntr) {
        this.fogasaEntr = fogasaEntr;
    }

    public double getFogasaMoneyEntr() {
        return fogasaMoneyEntr;
    }

    public void setFogasaMoneyEntr(double fogasaMoneyEntr) {
        this.fogasaMoneyEntr = fogasaMoneyEntr;
    }

    public double getRegulatoryBase() {
        return regulatoryBase;
    }

    public void setRegulatoryBase(double regulatoryBase) {
        this.regulatoryBase = regulatoryBase;
    }

    public double getIrpfMoney() {
        return irpfMoney;
    }

    public void setIrpfMoney(double irpfMoney) {
        this.irpfMoney = irpfMoney;
    }

    public double getIrpf() {
        return irpf;
    }

    public void setIrpf(double irpf) {
        this.irpf = irpf;
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(double monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    public double getMonthlyComplement() {
        return monthlyComplement;
    }

    public void setMonthlyComplement(double monthlyComplement) {
        this.monthlyComplement = monthlyComplement;
    }

    public double getProRateValue() {
        return proRateValue;
    }

    public void setProRateValue(double proRateValue) {
        this.proRateValue = proRateValue;
    }

    public double getMoneyThreeYears() {
        return moneyThreeYears;
    }

    public void setMoneyThreeYears(double moneyThreeYears) {
        this.moneyThreeYears = moneyThreeYears;
    }

    public double getYearlyGross() {
        return this.yearlyGross;
    }

    public void setYearlyGross(double yearlyGross) {
        this.yearlyGross = yearlyGross;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getNumThreeYears() {
        return numThreeYears;
    }

    public void setNumThreeYears(int numThreeYears) {
        this.numThreeYears = numThreeYears;
    }

    public Integer getIdPayroll() {
        return idPayroll;
    }

    public void setIdPayroll(Integer idPayroll) {
        this.idPayroll = idPayroll;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    

    public double getTotalCostEntr() {
        return totalCostEntr;
    }

    public void setTotalCostEntr(double totalCostEntr) {
        this.totalCostEntr = totalCostEntr;
    }  
}
