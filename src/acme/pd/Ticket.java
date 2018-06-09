package acme.pd;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

public class Ticket {
    private UUID id;
    private Company company;
    private Customer deliveryCustomer;
    private Customer pickupCustomer;
    private LocalDateTime creationDateTime;
    private User clerk;
    private Courier courier;
    private boolean billToSender;
    private String packageID;
    private BigDecimal quotedPrice;
    private LocalDateTime departureTime;
    private LocalDateTime estimatedDepartureTime;
    private LocalDateTime pickupTime;
    private LocalDateTime estimatedPickupTime;
    private LocalDateTime deliveryTime;
    private LocalDateTime estimatedDeliveryTime;
    private BigDecimal bonus;
    private Path path;
    private String note;

    public Ticket(Company company) {
        this.setCreationDateTime(LocalDateTime.now());
        this.setCompany(company);
        this.generatePackageId();
        this.setClerk(this.getCompany().getCurrentUser());
    }

    public ArrayList<String> getDeliveryInstructions() {
        return path.getDeliveryInstructions();
    }

    public UUID getId() {
        // TODO fix with database
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        return id;
    }

    public BigDecimal calcQuote() {
        // TODO calc quote
        return new BigDecimal(new Random().nextDouble());
    }

    private void calcEstimatedTimes() {
        // TODO estimate times
        this.estimatedDepartureTime = LocalDateTime.now().plusHours(2);
        this.estimatedPickupTime = this.estimatedDepartureTime.plusMinutes(16);
        this.estimatedDeliveryTime = this.pickupTime.plusMinutes(27);
    }

    public Company getCompany() {
        return company;
    }

    private void setCompany(Company company) {
        this.company = company;
    }

    public Customer getDeliveryCustomer() {
        return deliveryCustomer;
    }

    public void setDeliveryCustomer(Customer deliveryCustomer) {
        this.deliveryCustomer = deliveryCustomer;
    }

    public Customer getPickupCustomer() {
        return pickupCustomer;
    }

    public void setPickupCustomer(Customer pickupCustomer) {
        this.pickupCustomer = pickupCustomer;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    private void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public User getClerk() {
        return clerk;
    }

    private void setClerk(User clerk) {
        this.clerk = clerk;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public boolean isBillToSender() {
        return billToSender;
    }

    public void setBillToSender(boolean billToSender) {
        this.billToSender = billToSender;
    }

    public String getPackageID() {
        return packageID;
    }

    private void generatePackageId() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyMMdd-hhmmss");
        this.packageID = df.format(this.getCreationDateTime());
        // TODO check database for duplicate (Not likely but better safe than sorry)
        boolean overlap = (new Random().nextInt() % 10 == 0);
        if (overlap) {
            this.setCreationDateTime(this.getCreationDateTime().plusSeconds(1));
            this.generatePackageId();
        }
    }

    public BigDecimal getQuotedPrice() {
        return quotedPrice;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getEstimatedDepartureTime() {
        return estimatedDepartureTime;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public LocalDateTime getEstimatedPickupTime() {
        return estimatedPickupTime;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        if (new Random().nextInt() % 3 == 0)
            this.bonus = company.getBonus();
        else
            this.bonus = new BigDecimal(0);
        this.deliveryTime = deliveryTime;
    }

    public LocalDateTime getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(LocalDateTime estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public static void main(String [] args) {
        for (int i = 0; i < 100; i++) {
            Ticket ticket = new Ticket(new Company());
        }
    }
}
