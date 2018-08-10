package other;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import entity.User;

/**
 * Application Lifecycle Listener implementation class EMInitalizer
 *
 */
@WebListener
public class EMInitalizer implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public EMInitalizer() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
         EntityManagerFactory emf = Persistence.createEntityManagerFactory("forum");
         arg0.getServletContext().setAttribute("emf", emf);
         EntityManager em = emf.createEntityManager();
         EntityTransaction et = em.getTransaction();
         et.begin();
         User u = new User();
         u.setLogin("jan");
         u.setPassword("haslo");
         em.persist(u);
         et.commit();
    }
	
}
