package acme.pd;
import java.util.Collections;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.NoResultException;

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
		/*try {
			//FIXME
			Courier highest = PersistableEntity.querySingle(Courier.class, "select c from COURIER c ORDER BY NUMBER DESC", Collections.EMPTY_MAP);
			return highest.getCourierNumber() + 1;
		} catch (NoResultException e) {
			return 1;
		}*/
    	return 1;
    }

    public String toString() {
        return this.getName();
    }
}
