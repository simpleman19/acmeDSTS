package acme.pd;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import acme.data.PersistableEntity;

@Entity
@Table(name = "COURIER")
public class Courier extends Person {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "NUMBER")
    private int courierNumber;

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
