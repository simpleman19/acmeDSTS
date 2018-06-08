package acme.pd;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import acme.data.PersistableEntity;

@Entity
@Table(name = "COMPANY")
public class Company implements PersistableEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "COMPANY_ID")
    private UUID id;
	@Column(name = "COMPANY_NAME")
    private String name = "ACME";
	@Transient
    private Map map;
	//@OneToMany(mappedBy = "COURIER")
	//@MapKey(name = "NUMBER")
    private HashMap<UUID, Courier> couriers;
	@Transient
    private HashMap<UUID, Ticket> tickets;
	@Transient
    private HashMap<UUID, Customer> customer;
    @Transient
    private User currentUser;
    private BigDecimal bonus = new BigDecimal(1.25);
    private BigDecimal flatBillingRate = new BigDecimal(25);
    private BigDecimal blockBillingRate = new BigDecimal(5.36);
    private int latenessMarginMinutes = 5;
    private double blocksPerMile = 5.2;
    private double courierMilesPerHour = 5.8;

    public Company() {
        // TODO initialize company
        File file = new File("null");
        this.map = new Map(file);
    }

    public UUID getId() {
        // TODO fix with database
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public HashMap<UUID, Courier> getCouriers() {
        return couriers;
    }

    public void setCouriers(HashMap<UUID, Courier> couriers) {
        this.couriers = couriers;
    }

    public HashMap<UUID, Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(HashMap<UUID, Ticket> tickets) {
        this.tickets = tickets;
    }

    public HashMap<UUID, Customer> getCustomer() {
        return customer;
    }

    public void setCustomer(HashMap<UUID, Customer> customer) {
        this.customer = customer;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public BigDecimal getFlatBillingRate() {
        return flatBillingRate;
    }

    public void setFlatBillingRate(BigDecimal flatBillingRate) {
        this.flatBillingRate = flatBillingRate;
    }

    public BigDecimal getBlockBillingRate() {
        return blockBillingRate;
    }

    public void setBlockBillingRate(BigDecimal blockBillingRate) {
        this.blockBillingRate = blockBillingRate;
    }

    public int getLatenessMarginMinutes() {
        return latenessMarginMinutes;
    }

    public void setLatenessMarginMinutes(int latenessMarginMinutes) {
        this.latenessMarginMinutes = latenessMarginMinutes;
    }

    public double getBlocksPerMile() {
        return blocksPerMile;
    }

    public void setBlocksPerMile(double blocksPerMile) {
        this.blocksPerMile = blocksPerMile;
    }

    public double getCourierMilesPerHour() {
        return courierMilesPerHour;
    }

    public void setCourierMilesPerHour(double courierMilesPerHour) {
        this.courierMilesPerHour = courierMilesPerHour;
    }
}
