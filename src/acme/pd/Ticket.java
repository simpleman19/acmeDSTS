package acme.pd;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

public class Ticket {
    private UUID id;
    private Company company;
    private MapIntersection deliveryCustomerLocation;
    private MapIntersection pickupCustomerLocation;
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
      double quote = milesToTravel * company.getBlockBillingRate().doubleValue();
      quote = quote + company.getFlatBillingRate().doubleValue();
        return new BigDecimal(quote);
    }

    private void calcEstimatedTimes() {
      // TODO estimate times - needs refining
      
      double mphCouriers = company.getCourierMilesPerHour();
      double milesToTravel = path.getBlocksBetweenHomeandDropoff() * company.getBlocksPerMile();
      double timeToTravel = milesToTravel / mphCouriers;
      LocalDateTime resultOfCouriersAndMiles = deliveryTime.minusHours((long)timeToTravel);
      resultOfCouriersAndMiles.plusMinutes(5);
      
      if(resultOfCouriersAndMiles.isBefore(LocalDateTime.now()))
      {
        this.estimatedDepartureTime = resultOfCouriersAndMiles;
      }
      
      milesToTravel = path.getBlocksBetweenHomeandDropoff() * company.getBlocksPerMile();
      timeToTravel = milesToTravel / mphCouriers;
      this.estimatedPickupTime = this.estimatedDepartureTime.plusMinutes((long)timeToTravel);
      
      milesToTravel = path.getBlocksBetweenPickupandDropoff() * company.getBlocksPerMile();
      timeToTravel = milesToTravel / mphCouriers;
      this.estimatedDeliveryTime = this.pickupTime.plusMinutes((long)timeToTravel);
      
     
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
