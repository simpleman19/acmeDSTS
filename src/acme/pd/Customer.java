package acme.pd;

import java.util.UUID;

public class Customer {
    private UUID id;
    private int customerNumber;
    private String name;
    private boolean isActive;
    private String streetName;
    private String avenueName;

    public UUID getId() {
        // TODO fix with database
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
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

    private MapIntersection intersection;

    public String toString() {
        return this.name;
    }
}
