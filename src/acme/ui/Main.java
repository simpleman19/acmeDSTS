package acme.ui;

import acme.pd.Company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.persistence.Persistence;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
 
import javax.persistence.PersistenceUnit;

public class Main {
    public static void main(String[] args) {
        Company company = new Company();
        SQLiteJDBCDriverConnection.connect();
        HibernateConnection.connect();
        System.out.println("Hello world!");
    }

    
    public static class HibernateConnection {
    	@PersistenceUnit
    	static EntityManagerFactory entityManagerFactory;
    	protected static void setUp() throws Exception {
		    entityManagerFactory = Persistence.createEntityManagerFactory("my-pu");
		}
    	
    	public static void connect() {
    		try {
        		setUp();	
    		} catch (Exception e) {
                System.out.println(e.getMessage());
    		}
    		if (entityManagerFactory != null) {
    			EntityManager entityManager = entityManagerFactory.createEntityManager();
        		entityManager.getTransaction().begin();
        		Company c = new Company();
        		c.setName("Test company");
        		entityManager.persist(c);
        		entityManager.getTransaction().commit();
        		entityManager.close();
    		}
    		
    	}
    }
 
    /**
     *
     * @author sqlitetutorial.net
     */
    public static class SQLiteJDBCDriverConnection {
         /**
         * Connect to a sample database
         */
        public static void connect() {
            Connection conn = null;
            try {
                // db parameters
                String url = "jdbc:sqlite:database/acmeDSTS.db";
                // create a connection to the database
                conn = DriverManager.getConnection(url);
                
                System.out.println("Connection to SQLite has been established.");
                
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
}
