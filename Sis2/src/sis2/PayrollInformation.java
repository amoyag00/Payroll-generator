/*
 * Class containing the information about payrolls read from the excel. This 
 * information includes base salaries, complements, seniority money, IRPF 
 * information and discounts information
 */
package sis2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Alejandro Moya García
 * @version 4.0 02/06/2018
 */
public class PayrollInformation {

    private ArrayList<Category> categories;
    private ArrayList<Company> companies;
    private Map<Double, Double> IRPF;
    private ArrayList<Double> threeYears;
    private double genQuotEmp;
    private double disempQuotEmp;
    private double formationEmp;
    private double commonContingencies;
    private double fogasa;
    private double disempEntr;
    private double formationEntr;
    private double accidentsEntr;
    private static PayrollInformation instance;

    public PayrollInformation() {
        this.categories = new ArrayList();
        this.companies = new ArrayList();
        this.IRPF = new HashMap();
        this.threeYears = new ArrayList();
        this.threeYears.add(0.0);
    }

    public static PayrollInformation getInstance() {
        if (instance == null) {
            instance = new PayrollInformation();
        }
        return instance;
    }

    /**
     * Returns the base salary for the specified category.
     *
     * @param category
     * @return the base salary.
     */
    public double getPayrollOf(String category) {
        for (Category cat : this.categories) {
            if (cat.getCategoryName().equals(category)) {
                return cat.getBasePayroll();
            }
        }
        return -1;
    }

    public Category getCategory(String categoryName) {
        for (Category cat : this.categories) {
            if (cat.getCategoryName().equals(categoryName)) {
                return cat;
            }
        }
        return null;
    }

    public void updateCategory(Category updatedCat) {
        for (int i = 0; i < this.categories.size(); i++) {
            if (this.categories.get(i).getCategoryName().equals(
                    updatedCat.getCategoryName())) {
                this.categories.set(i, updatedCat);
            }
        }
    }

    /**
     * Returns the complement for the specified category.
     *
     * @param category
     * @return the complement.
     */
    public double getComplementOf(String category) {
        for (Category cat : this.categories) {
            if (cat.getCategoryName().equals(category)) {
                return cat.getComplement();
            }
        }
        return -1;
    }

    public double getGenQuotEmp() {
        return genQuotEmp;
    }

    public void setGenQuotEmp(double genQuotEmp) {
        this.genQuotEmp = genQuotEmp;
    }

    public double getDisempQuotEmp() {
        return disempQuotEmp;
    }

    public void setDisempQuotEmp(double disempQuotEmp) {
        this.disempQuotEmp = disempQuotEmp;
    }

    public double getFormationEmp() {
        return formationEmp;
    }

    public void setFormationEmp(double formationEmp) {
        this.formationEmp = formationEmp;
    }

    public double getCommonContingencies() {
        return commonContingencies;
    }

    public void setCommonContingencies(double commonContingencies) {
        this.commonContingencies = commonContingencies;
    }

    public double getFogasa() {
        return fogasa;
    }

    public void setFogasa(double fogasa) {
        this.fogasa = fogasa;
    }

    public double getDisempEntr() {
        return disempEntr;
    }

    public void setDisempEntr(double disempEntr) {
        this.disempEntr = disempEntr;
    }

    public double getFormationEntr() {
        return formationEntr;
    }

    public void setFormationEntr(double formationEntr) {
        this.formationEntr = formationEntr;
    }

    public double getAccidentsEntr() {
        return accidentsEntr;
    }

    public void setAccidentsEntr(double accidentsEntr) {
        this.accidentsEntr = accidentsEntr;
    }

    public void addCategory(Category cat) {
        this.categories.add(cat);
    }

    public void addIRPF(double money, double retention) {
        this.IRPF.put(money, retention);
    }

    public void addThreeYear(double money) {
        this.threeYears.add(money);
    }

    public double getMoneyThreeYear(int numThreeYears) {
        return this.threeYears.get(numThreeYears);
    }

    /**
     * Returns the associated retention percent for the given money.
     *
     * @param money
     * @return the IPRF retention
     */
    public double getIRPFFor(double money) {
        Iterator it = this.IRPF.entrySet().iterator();
        double key, value = 0.0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            key = (double) pair.getKey();
            value = (double) pair.getValue();
            if (key >= money) {
                return value;
            }

        }
        return value;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public Company getCompany(String companyName, String CIF) {
        for (Company com : this.companies) {
            if (com.getCIF().equals(CIF)) {
                return com;
            }
        }
        Company com = new Company();
        com.setCompanyName(companyName);
        com.setCIF(CIF);
        this.companies.add(com);
        return com;
    }

    @Override
    public String toString() {
        return "PayrollInformation{" + "genQuotEmp=" + genQuotEmp
                + ", disempQuotEmp=" + disempQuotEmp + ", formationEmp="
                + formationEmp + ", commonContingencies=" + commonContingencies
                + ", fogasa=" + fogasa + ", disempEntr=" + disempEntr
                + ", formationEntr=" + formationEntr + ", accidentsEntr="
                + accidentsEntr + '}';
    }

}
