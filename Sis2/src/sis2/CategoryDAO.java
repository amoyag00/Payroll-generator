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
public class CategoryDAO {

    /**
     * Saves a category into the database
     *
     * @param session
     * @param cat
     */
    public void save(Session session, Category cat) {
        Transaction t = session.beginTransaction();
        session.saveOrUpdate(cat);
        t.commit();
    }

    /**
     * Updates a category of the database
     *
     * @param session
     * @param cat
     * @return the category updated
     */
    public Category update(Session session, Category cat) {
        Category bbddCat;
        Transaction t = session.beginTransaction();
        Query query = session.createQuery("from Category where"
                + " categoryName = :nombreCat ");
        query.setParameter("nombreCat", cat.getCategoryName());
        bbddCat = (Category) query.list().get(0);

        updateColumns(cat, bbddCat);
        session.update(bbddCat);
        t.commit();
        return bbddCat;

        /*Query query = session.createQuery("update Category set basePayroll =
        :basePayroll, complement= :complement" +
         " where categoryName = :categoryName");
         query.setParameter("basePayroll", cat.getBasePayroll());
         query.setParameter("complement", cat.getComplement());
         query.setParameter("categoryName", cat.getCategoryName());
         int result = query.executeUpdate();*/
    }

    /**
     * Checks if a category exists
     *
     * @param session
     * @param cat
     * @return true if exists, flase otherwise
     */
    public boolean exists(Session session, Category cat) {
        List<Category> list;
        Transaction t = session.beginTransaction();

        Query query = session.createQuery("from Category where"
                + " categoryName = :nombreCat ");
        query.setParameter("nombreCat", cat.getCategoryName());
        list = query.list();

        t.commit();

        return !list.isEmpty();

    }

    /**
     * Updates the category bbddCat by setting in it the values of the category
     * memoryCat
     *
     * @param memoryCat
     * @param bbddCat
     */
    public void updateColumns(Category memoryCat, Category bbddCat) {
        bbddCat.setBasePayroll(memoryCat.getBasePayroll());
        bbddCat.setComplement(memoryCat.getComplement());

    }
}
