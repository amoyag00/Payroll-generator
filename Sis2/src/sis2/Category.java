package sis2;

import java.util.HashSet;
import java.util.Set;

/*
 * Class representing the category of an employee. It contains the base payroll,
 * the complement and the quotation code.
 */
/**
 *
 * @author Alejandro Moya García
 * @version 4.0 02/06/2018
 */
public class Category {

    private int idCategory;
    private String categoryName;
    private double basePayroll;
    private double complement;
    private Set trabajadorbbdds = new HashSet(0);

    public Category() {

    }

    public Category(String categoryName, double basePayroll, double complement) {
        this.categoryName = categoryName;
        this.basePayroll = basePayroll;
        this.complement = complement;
    }

    public Category(String categoryName, double basePayroll, double complement, Set trabajadorbbdds) {
        this.categoryName = categoryName;
        this.basePayroll = basePayroll;
        this.complement = complement;
        this.trabajadorbbdds = trabajadorbbdds;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getBasePayroll() {
        return basePayroll;
    }

    public void setBasePayroll(double basePayroll) {
        this.basePayroll = basePayroll;
    }

    public double getComplement() {
        return complement;
    }

    public void setComplement(double complement) {
        this.complement = complement;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public Set getTrabajadorbbdds() {
        return this.trabajadorbbdds;
    }

    public void setTrabajadorbbdds(Set trabajadorbbdds) {
        this.trabajadorbbdds = trabajadorbbdds;
    }

    @Override
    public String toString() {
        return "Category{" + "categoryName=" + categoryName + ", basePayroll="
                + basePayroll + ", complement=" + complement + '}';
    }

}
