package acme.pd;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import acme.data.PersistableEntity;
@Entity(name = "CUSTOMER")
public class Customer implements PersistableEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID")
    private UUID id;
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
	
    public UUID getId() {
        return id;
    }

    public int getCustomerNumber() {
        return this.customerNumber;
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

    public MapIntersection getIntersection(Map map) {
    	Road a = map.getRoadByName(this.streetName);
    	Road b =  map.getRoadByName(this.avenueName);
        return map.getIntersection(a,b);
    }

    public static int getNextCustomerNumber() {
		/*try {
			//FIXME
			Customer highest = PersistableEntity.querySingle(Customer.class, "select c from CUSTOMER c ORDER BY NUMBER DESC", Collections.EMPTY_MAP);
			return highest.getCustomerNumber() + 1;
		} catch (NoResultException e) {
			return 1;
		}*/
    	return 1;
    }

    public String toString() {
        return this.name;
    }
}
