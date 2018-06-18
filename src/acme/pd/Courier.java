package acme.pd;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

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

    public String toString() {
        return this.getName();
    }
}
