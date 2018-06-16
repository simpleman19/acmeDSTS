package acme.pd;

public class Courier extends Person {
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
