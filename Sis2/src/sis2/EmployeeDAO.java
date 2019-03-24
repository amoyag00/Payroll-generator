/*
 * Class used to manage operations with employee table of the database
 */
package sis2;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Alejandro Moya García
 * @version 4.0 02/06/2018
 */
public class EmployeeDAO {

    /**
     * Saves an employee into the database
     *
     * @param session
     * @param emp
     */
    public void save(Session session, Employee emp) {
        Transaction t = session.beginTransaction();
        session.saveOrUpdate(emp);
        t.commit();
    }

    /**
     * Updates an employee of the database
     *
     * @param session
     * @param emp
     * @return the updated employee
     */
    public Employee update(Session session, Employee emp) {
        Employee bbddEmp;
        Transaction t = session.beginTransaction();
        Query query = session.createQuery("from Employee where"
                + " name = :name and nifnie = :nifnie and companyEntryDate=:"
                + "companyEntryDate ");
        query.setParameter("name", emp.getName());
        query.setParameter("nifnie", emp.getNifnie());
        query.setDate("companyEntryDate", emp.getCompanyEntryDate());
        bbddEmp = (Employee) query.list().get(0);

        updateColumns(emp, bbddEmp);
        session.update(bbddEmp);
        t.commit();
        return bbddEmp;
    }

    /**
     * Checks if an employee exists in the database
     *
     * @param session
     * @param emp
     * @return true if exists, false otherwise
     */
    public boolean exists(Session session, Employee emp) {
        List<Category> list;
        Transaction t = session.beginTransaction();

        Query query = session.createQuery("from Employee where"
                + " name = :name and nifnie = :nifnie and companyEntryDate=:"
                + "companyEntryDate ");
        query.setParameter("name", emp.getName());
        query.setParameter("nifnie", emp.getNifnie());
        query.setDate("companyEntryDate", emp.getCompanyEntryDate());
        list = query.list();

        t.commit();

        return !list.isEmpty();
    }

    /**
     * Updates the employee bbddEmp by setting in it the values of the employee
     * memoryEmp
     *
     * @param memoryEmp
     * @param bbddEmp
     */
    public void updateColumns(Employee memoryEmp, Employee bbddEmp) {
        bbddEmp.setBankAccount(memoryEmp.getbAC());
        bbddEmp.setCategory(memoryEmp.getCategory());
        bbddEmp.setCodigoCuenta(memoryEmp.getCodigoCuenta());
        bbddEmp.setCompany(memoryEmp.getCompany());
        bbddEmp.setCompanyEntryDate(memoryEmp.getCompanyEntryDate());
        bbddEmp.setCompanyEntryLocalDate(memoryEmp.getCompanyEntryLocalDate());
        bbddEmp.setCompanyName(memoryEmp.getCompanyName());
        bbddEmp.setEmail(memoryEmp.getEmail());
        bbddEmp.setExtraPayroll(memoryEmp.getExtraPayroll());
        bbddEmp.setExtraProRate(memoryEmp.getExtraProRate());
        bbddEmp.setID(memoryEmp.getID());
        bbddEmp.setIban(memoryEmp.getIban());
        bbddEmp.setNominas(memoryEmp.getNominas());
        bbddEmp.setPayroll(memoryEmp.getPayroll());
        bbddEmp.setSurname1(memoryEmp.getSurname1());
        bbddEmp.setSurname2(memoryEmp.getSurname2());

    }
}
