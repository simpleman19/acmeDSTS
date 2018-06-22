package acme.pd;

import acme.data.PersistableEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity(name = "TICKET")
public class Ticket implements PersistableEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID")
    private UUID id;
	@ManyToOne
    private Company company;
	@ManyToOne
    private Customer deliveryCustomer;
	@ManyToOne
    private Customer pickupCustomer;
	@Column(name = "CREATED_TIME")
    private LocalDateTime creationDateTime;
    @ManyToOne
    private User clerk;
    @ManyToOne
    private Courier courier;
    @Column(name = "IS_BILLED_TO_SENDER")
    private boolean billToSender;
    @Column(name = "PACKAGE_ID")
    private String packageID;
    @Column(name = "QUOTE")
    private BigDecimal quotedPrice;
    @Column(name = "DEPARTURE_TIME")
    private LocalDateTime departureTime;
    @Column(name = "EST_DEPARTURE_TIME")
    private LocalDateTime estimatedDepartureTime;
    @Column(name = "PICKUP_TIME")
    private LocalDateTime pickupTime;
    @Column(name = "EST_PICKUP_TIME")
    private LocalDateTime estimatedPickupTime;
    @Column(name = "DELIVERY_TIME")
    private LocalDateTime deliveryTime;
    @Column(name = "EST_DELIVERY_TIME")
    private LocalDateTime estimatedDeliveryTime;
    @Column(name = "RETURN_TIME")
    private LocalDateTime returnTime;
    @Column(name = "EST_RETURN_TIME")
    private LocalDateTime estimateReturnTime;
    @Column(name = "COURIER_BONUS")
    private BigDecimal bonus;
    @Transient
    private Path path;
    @Column(name = "NOTE")
    private String note;

    public Ticket() {
    }

    public Ticket(Company company) {
        this.setCreationDateTime(LocalDateTime.now());
        this.setCompany(company);
        this.generatePackageId();
        this.setClerk(this.getCompany().getCurrentUser());

        this.setCompany(company);
        HashMap<UUID, Customer> customers = new HashMap<>(company.getCustomers());
        // Set the pickup customer to a customer (Useful for UI)
        Customer tmpCust = (Customer) customers.values().toArray()[0];
        this.setPickupCustomer(tmpCust);
        // Remove customer from map to be sure that pickup and destination are different
        customers.remove(tmpCust.getId());
        // Set destination customer to a customer (Useful for UI)
        this.setDeliveryCustomer((Customer) customers.values().toArray()[0]);
        this.setDeliveryTime(LocalDateTime.now().plusHours(6));

        this.note = "";
        updatePath();

    }

    public ArrayList<String> getDeliveryInstructions() {
        return path.getDeliveryInstructions();
    }

    public UUID getId() {
        return id;
    }

    private void updatePath() {
        // Only update if pickup and delivery are set
        if (this.getPickupCustomer() != null
                && this.getDeliveryCustomer() != null
                && this.getDeliveryTime() != null) {
            this.path = company.getMap().getPath(
                    this.getPickupCustomerLocation(),
                    this.getDeliveryCustomerLocation()
            );
            calcEstimatedTimes();
            calculateQuote();
        } else {
            this.path = null;
        }
    }

    private void calculateQuote() {
        double quote = path.getBlocksBetweenHomeandDropoff() * company.getBlockBillingRate().doubleValue();
        this.quotedPrice = new BigDecimal(quote + company.getFlatBillingRate().doubleValue());
    }

    private void calcEstimatedTimes() {
      //Lets get down to bussiness.... to defeat the huns!
      
      double mphCouriers = company.getCourierMilesPerHour();
      System.out.println("Miles Per Hour: "+ mphCouriers);
      System.out.println("Blocks per Mile: " + company.getBlocksPerMile());
      double bphCouriers = mphCouriers * company.getBlocksPerMile();
      System.out.println("Blocks per Hour: " + bphCouriers);
      double timeToTravel = path.getBlocksBetweenHomeandDropoff() / bphCouriers;
      timeToTravel = timeToTravel * 60;
      System.out.println("Time to Travel before 10 mins added: " + timeToTravel);
      timeToTravel = timeToTravel + 10;
      System.out.println("Time to Travel after 10 mins added: " + timeToTravel);
      Long time = (long)timeToTravel;
      System.out.println(time);
      LocalDateTime result = deliveryTime.minus((long)(timeToTravel), ChronoUnit.MINUTES);
      System.out.println(result);
      

      if(result.isAfter(LocalDateTime.now()))
      {
          this.estimatedDepartureTime = result;
      } else {
          this.estimatedDepartureTime = LocalDateTime.now();
      }

      timeToTravel = path.getBlocksBetweenHomeandPickup() / bphCouriers;
      this.estimatedPickupTime = this.estimatedDepartureTime.plus((long)(60*timeToTravel), ChronoUnit.MINUTES);
      
      timeToTravel = path.getBlocksBetweenPickupandDropoff()/ bphCouriers;
      this.estimatedDeliveryTime = this.estimatedPickupTime.plus((long)(60*(timeToTravel+5)), ChronoUnit.MINUTES);
      
      timeToTravel = path.getBlocksBetweenDropoffandHome() / bphCouriers;
      this.estimateReturnTime = this.estimatedDeliveryTime.plus((long)(60*(timeToTravel + 5)), ChronoUnit.MINUTES);
      
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Customer getDeliveryCustomer() {
        return deliveryCustomer;
    }

    public void setDeliveryCustomer(Customer deliveryCustomer) {
        this.deliveryCustomer = deliveryCustomer;
        this.updatePath();
    }

    public Customer getPickupCustomer() {
        return pickupCustomer;
    }

    public void setPickupCustomer(Customer pickupCustomer) {
        this.pickupCustomer = pickupCustomer;
        this.updatePath();
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
        // TODO Bonus
        this.deliveryTime = deliveryTime;
        this.updatePath();
    }

    public LocalDateTime getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    private void setEstimatedDeliveryTime(LocalDateTime estimatedDeliveryTime) {
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

    public MapIntersection getDeliveryCustomerLocation()
    {
      return this.deliveryCustomer.getIntersection();
    }

    public MapIntersection getPickupCustomerLocation()
    {
      return this.pickupCustomer.getIntersection();
    }
}
