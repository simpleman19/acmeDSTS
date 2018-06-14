package acme.pd;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import acme.data.PersistableEntity;
@Entity
@Table(name = "CUSTOMER")
public class Customer implements PersistableEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID")
    private UUID id;
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "NUMBER")
    private int customerNumber;
	@Column(name = "NAME")
    private String name;
	@Column(name = "IS_ACTIVE")
    private boolean isActive;
	@Column(name = "STREET_NAME")
    private String streetName;
	@Column(name = "AVENUE_NAME")
    private String avenueName;
	@Transient
	private MapIntersection intersection;

    public UUID getId() {
        // TODO fix with database
        /*if (this.id == null) {
            this.id = UUID.randomUUID();
        }*/
        return id;
    }

    public int getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getAvenueName() {
        return avenueName;
    }

    public void setAvenueName(String avenueName) {
        this.avenueName = avenueName;
    }

    public MapIntersection getIntersection() {
        return intersection;
    }

    public void setIntersection(MapIntersection intersection) {
        this.intersection = intersection;
    }
}
