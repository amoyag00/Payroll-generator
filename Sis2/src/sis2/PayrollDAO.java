/*
 * Class used to manage operations with category table of the database
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
public class PayrollDAO {

    /**
     * Saves a payroll in the database
     *
     * @param session
     * @param proll
     */
    public void save(Session session, Payroll proll) {
        Transaction t = session.beginTransaction();
        session.saveOrUpdate(proll);
        t.commit();
    }

    /**
     * Updates a payroll of the database
     *
     * @param session
     * @param proll
     * @return the updated payroll.
     */
    public Payroll update(Session session, Payroll proll) {
        Payroll bbddProll;
        Transaction t = session.beginTransaction();

        Query query = session.createQuery("from Payroll where"
                + " month = :month and year = :year and employee=:employee and "
                + "monthlyGross =:monthlyGross and liquid=:liquid");
        query.setParameter("month", proll.getMonth());
        query.setParameter("year", proll.getYear());
        query.setParameter("employee", proll.getEmployee());
        query.setParameter("monthlyGross", proll.getMonthlyGross());
        query.setParameter("liquid", proll.getLiquid());
        bbddProll = (Payroll) query.list().get(0);
        updateColumns(proll, bbddProll);
        session.update(bbddProll);
        t.commit();
        return bbddProll;

    }

    /**
     * Checks if a payroll exists in the database
     *
     * @param session
     * @param pay
     * @return true if exists, false otherwise
     */
    public boolean exists(Session session, Payroll pay) {
        List<Payroll> list;
        Transaction t = session.beginTransaction();

        Query query = session.createQuery("from Payroll where"
                + " month = :month and year = :year and employee=:employee and"
                + " monthlyGross =:monthlyGross and liquid=:liquid");
        query.setParameter("month", pay.getMonth());
        query.setParameter("year", pay.getYear());
        query.setParameter("employee", pay.getEmployee());
        query.setParameter("monthlyGross", pay.getMonthlyGross());
        query.setParameter("liquid", pay.getLiquid());
        list = query.list();

        t.commit();

        return !list.isEmpty();
    }

    /**
     * Updates the payroll bbddProll by setting in it the values of the payroll
     * memProll
     *
     * @param memProll
     * @param bbddProll
     * @return
     */
    public Payroll updateColumns(Payroll memProll, Payroll bbddProll) {
        bbddProll.setAccidentsEntr(memProll.getAccidentsEntr());
        bbddProll.setAccidentsMoneyEntr(memProll.getAccidentsMoneyEntr());
        bbddProll.setCommonContingenciesEntr(
                memProll.getCommonContingenciesEntr());
        bbddProll.setCommonContingenciesMoneyEntr(
                memProll.getCommonContingenciesMoneyEntr());
        bbddProll.setDisempEmp(memProll.getDisempEmp());
        bbddProll.setDisempEntr(memProll.getDisempEntr());
        bbddProll.setDisempMoneyEmp(memProll.getDisempMoneyEmp());
        bbddProll.setDisempMoneyEntr(memProll.getDisempMoneyEntr());
        bbddProll.setFogasaEntr(memProll.getFogasaEntr());
        bbddProll.setFogasaMoneyEntr(memProll.getFogasaMoneyEntr());
        bbddProll.setFormationEmp(memProll.getFormationEmp());
        bbddProll.setFormationEntr(memProll.getFormationEntr());
        bbddProll.setFormationMoneyEmp(memProll.getFormationMoneyEmp());
        bbddProll.setFormationMoneyEntr(memProll.getFormationMoneyEntr());
        bbddProll.setGeneralContingenciesEmp(
                memProll.getGeneralContingenciesEmp());
        bbddProll.setGeneralContingenciesMoneyEmp(
                memProll.getGeneralContingenciesMoneyEmp());
        bbddProll.setIrpf(memProll.getIrpf());
        bbddProll.setIrpfMoney(memProll.getIrpfMoney());
        bbddProll.setMoneyThreeYears(memProll.getMoneyThreeYears());
        bbddProll.setMonthlyComplement(memProll.getMonthlyComplement());
        bbddProll.setMonthlyGross(memProll.getMonthlyGross());
        bbddProll.setMonthlySalary(memProll.getMonthlySalary());
        bbddProll.setNumThreeYears(memProll.getNumThreeYears());
        bbddProll.setProRateValue(memProll.getProRateValue());
        bbddProll.setRegulatoryBase(memProll.getRegulatoryBase());
        bbddProll.setTotalCostEntr(memProll.getTotalCostEntr());
        bbddProll.setYearlyGross(memProll.getYearlyGross());

        return bbddProll;
    }
}
