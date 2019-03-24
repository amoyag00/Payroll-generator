/*
 * Class used to manage operations with company table of the database
 */
package sis2;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Alejandro Moya García
 * @version 02/06/2018
 */
public class CompanyDAO {

    /**
     * Saves a company into the database
     *
     * @param session
     * @param company
     */
    public void save(Session session, Company company) {
        Transaction t = session.beginTransaction();
        session.saveOrUpdate(company);
        t.commit();
    }

    /**
     * Updates a company of the database
     *
     * @param session
     * @param com
     * @return the company updated
     */
    public Company update(Session session, Company com) {
        Company bbddCom;
        Transaction t = session.beginTransaction();
        Query query = session.createQuery("from Company where"
                + " CIF = :cif ");
        query.setParameter("cif", com.getCIF());
        bbddCom = (Company) query.list().get(0);

        updateColumns(com, bbddCom);
        session.update(bbddCom);
        t.commit();
        return bbddCom;
    }

    /**
     * Checks if a company exists in the database
     *
     * @param session
     * @param com
     * @return true if exists, false otherwise
     */
    public boolean exists(Session session, Company com) {
        List<Category> list;
        Transaction t = session.beginTransaction();

        Query query = session.createQuery("from Company where"
                + " CIF = :cif ");
        query.setParameter("cif", com.getCIF());
        list = query.list();

        t.commit();

        return !list.isEmpty();
    }

    /**
     * Updates the company bbddCom by setting in it the values of the company
     * memoryCom
     *
     * @param memoryCom
     * @param bbddCom
     */
    public void updateColumns(Company memoryCom, Company bbddCom) {
        bbddCom.setCompanyName(memoryCom.getCompanyName());
        bbddCom.setTrabajadorbbdds(memoryCom.getTrabajadorbbdds());

    }
}
