/*
 * Class used for creating and destroying hibernate sessions.
 */
package hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author Alejandro Moya García
 * @version 02/06/2018
 */
public class NewHibernateUtil {

    private static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;
    
    /**
     * Creates the sessionFactory
     */
    public static void initialize() {
        try {
            /* Create the SessionFactory from standard (hibernate.cfg.xml) 
             config file.
             Original :sessionFactory = new AnnotationConfiguration().
             configure().buildSessionFactory();*/
            Configuration configuration = new Configuration();
            configuration.configure("/hibernate/hibernate.cfg.xml");
            serviceRegistry = new StandardServiceRegistryBuilder().applySettings
            (configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    /**
     * Singleton pattern for getting the sessionFactory
     * @return 
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            initialize();
        }
        return sessionFactory;
    }
    
    /**
     * Closes the service registry
     */
    public static void closeServiceRegistry() {
        StandardServiceRegistryBuilder.destroy(serviceRegistry);
    }
}
