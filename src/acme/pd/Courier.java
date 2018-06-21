package acme.pd;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.NoResultException;

import acme.data.HibernateAdapter;
import acme.data.PersistableEntity;

@Entity(name = "COURIER")
public class Courier extends Person implements PersistableEntity {
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "NUMBER")
    private int courierNumber;

	public Courier() {
	    super();
    }
    public int getCourierNumber() {
        return courierNumber;
    }

    public void setCourierNumber(int courierNumber) {
        this.courierNumber = courierNumber;
    }

    public static int getNextCourierNumber() {
		try {		    
		    EntityManager em = HibernateAdapter.getEntityManager();
		    List<Courier> cour = em.createQuery(
		            "select c " +
		            "from COURIER c " +
		            "order by NUMBER DESC", Courier.class
		        ).getResultList();
		    
			return cour.get(0).getCourierNumber()+1;
		} catch (NoResultException e) {
			return 1;
		}
    }

    public String toString() {
        return this.getName();
    }
}
