package acme.pd;

import acme.data.PersistableEntity;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "TICKET")
public class Ticket implements PersistableEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID")
    private UUID id;
	@Transient
    private Company company;
	@Transient
    private MapIntersection deliveryCustomerLocation;
	@Transient
    private MapIntersection pickupCustomerLocation;
	@Transient
    private Customer deliveryCustomer;
	@Transient
    private Customer pickupCustomer;
	@Column(name = "CREATED_TIME")
    private LocalDateTime creationDateTime;
    @Transient
    private User clerk;
    @Transient
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
      double milesToTravel = path.getBlocksBetweenHomeandDropoff() * company.getBlocksPerMile();
      double quote = path.getBlocksBetweenHomeandDropoff() * company.getBlockBillingRate().doubleValue();
      quote = quote + company.getFlatBillingRate().doubleValue();
      
      this.calcEstimatedTimes();
        return new BigDecimal(quote);
    }

    private void calcEstimatedTimes() {
      // TODO estimate times - needs refining
      
      double mphCouriers = company.getCourierMilesPerHour();
      double milesToTravel = company.getBlocksPerMile() / path.getBlocksBetweenHomeandDropoff();
      double timeToTravel = milesToTravel / mphCouriers;
      LocalDateTime resultOfCouriersAndMiles = deliveryTime.minus((long)(60*timeToTravel), ChronoUnit.MINUTES);
      resultOfCouriersAndMiles.plusMinutes(5);
      
     
      try {
        if(resultOfCouriersAndMiles.isAfter(LocalDateTime.now()))
        {
          this.estimatedDepartureTime = resultOfCouriersAndMiles;
        }
      
      milesToTravel = company.getBlocksPerMile() / path.getBlocksBetweenHomeandDropoff();
      timeToTravel = milesToTravel / mphCouriers;
      this.estimatedPickupTime = this.estimatedDepartureTime.plus((long)(60*timeToTravel), ChronoUnit.MINUTES);
      
      milesToTravel = company.getBlocksPerMile() / path.getBlocksBetweenPickupandDropoff();
      timeToTravel = milesToTravel / mphCouriers;
      this.estimatedDeliveryTime = this.estimatedPickupTime.plus((long)(60*timeToTravel), ChronoUnit.MINUTES);
      } 
      catch(Exception error)
      {
        System.out.println("There has been a problem. Your delivery is not possible.");
      }
     
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

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public User getClerk() {
        return clerk;
    }

    public void setClerk(User clerk) {
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
//        if (new Random().nextInt() % 3 == 0)
//            //this.bonus = company.getBonus();
//        else
//            this.bonus = new BigDecimal(0);
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
    
    public MapIntersection getDeliveryCustomerLocation()
    {
      return this.deliveryCustomerLocation;
    }
    
    public void setDeliveryCustomerLocation(MapIntersection deliveryCustomerLocation)
    {
      this.deliveryCustomerLocation = deliveryCustomerLocation;
    }
    
    public MapIntersection getPickupCustomerLocation()
    {
      return this.pickupCustomerLocation;
    }
    
    public void setPickupCustomerLocation(MapIntersection pickupCustomerLocation)
    {
      this.pickupCustomerLocation = pickupCustomerLocation;
    }
}
