package sis2;

import java.util.HashSet;
import java.util.Set;

/*
 * Class representing a company
 */
/**
 *
 * @author Alejandro Moya García
 * @version 4.0 02/06/2018
 */
public class Company {

    private Integer idCompany;
    private String companyName;
    private String CIF;
    private Set trabajadorbbdds = new HashSet(0);

    public Company() {

    }

    public Company(Integer idCompany, String companyName, String CIF) {
        this.idCompany = idCompany;
        this.companyName = companyName;
        this.CIF = CIF;
    }

    public Company(Integer idCompany, String companyName, String CIF, Set trabajadorbbdds) {
        this.idCompany = idCompany;
        this.companyName = companyName;
        this.CIF = CIF;
        this.trabajadorbbdds = trabajadorbbdds;
    }

    public Integer getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(Integer idCompany) {
        this.idCompany = idCompany;
    }

    public Set getTrabajadorbbdds() {
        return trabajadorbbdds;
    }

    public void setTrabajadorbbdds(Set trabajadorbbdds) {
        this.trabajadorbbdds = trabajadorbbdds;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCIF() {
        return CIF;
    }

    public void setCIF(String CIF) {
        this.CIF = CIF;
    }

}
