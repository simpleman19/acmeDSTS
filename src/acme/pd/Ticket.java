package acme.pd;

import acme.data.PersistableEntity;
import java.lang.reflect.Array;
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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "TICKET")
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

        this.note = "";
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
        double milesToTravel = path.getBlocksBetweenHomeandDropoff() * company.getBlocksPerMile();
        double quote = path.getBlocksBetweenHomeandDropoff() * company.getBlockBillingRate().doubleValue();
        this.quotedPrice = new BigDecimal(quote + company.getFlatBillingRate().doubleValue());
    }

    private void calcEstimatedTimes() {
      // TODO estimate times - needs refining
        // Altered this slightly

      double mphCouriers = company.getCourierMilesPerHour();
      double milesToTravel = company.getBlocksPerMile() / path.getBlocksBetweenHomeandDropoff();
      double timeToTravel = milesToTravel / mphCouriers;
      LocalDateTime resultOfCouriersAndMiles = deliveryTime.minus((long)(60*timeToTravel), ChronoUnit.MINUTES);
      resultOfCouriersAndMiles.plusMinutes(5);

      if(resultOfCouriersAndMiles.isAfter(LocalDateTime.now()))
      {
          this.estimatedDepartureTime = resultOfCouriersAndMiles;
      } else {
          this.estimatedDepartureTime = LocalDateTime.now();
      }

      milesToTravel = company.getBlocksPerMile() / path.getBlocksBetweenHomeandDropoff();
      timeToTravel = milesToTravel / mphCouriers;
      this.estimatedPickupTime = this.estimatedDepartureTime.plus((long)(60*timeToTravel), ChronoUnit.MINUTES);

      milesToTravel = company.getBlocksPerMile() / path.getBlocksBetweenPickupandDropoff();
      timeToTravel = milesToTravel / mphCouriers;
      this.estimatedDeliveryTime = this.estimatedPickupTime.plus((long)(60*timeToTravel), ChronoUnit.MINUTES);
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
