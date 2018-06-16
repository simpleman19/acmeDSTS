package acme.pd;

import java.io.File;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import acme.data.PersistableEntity;

///////////////////////
@Entity
@Table(name = "COMPANY")
public class Company implements PersistableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private UUID id;
    @Column(name = "NAME")
    private String name = "ACME";
    @Transient
    private Map map;
    @Transient
    private HashMap<UUID, Courier> couriers;
    @Transient
    private HashMap<UUID, Ticket> tickets;
    @Transient
    private HashMap<UUID, Customer> customer;
    @Transient
    private HashMap<UUID, User> users;
    @Transient
    private User currentUser = null;
    @Column(name = "BONUS")
    private BigDecimal bonus = new BigDecimal(1.25);
    @Column(name = "FLAT_BILLING_RATE")
    private BigDecimal flatBillingRate = new BigDecimal(25);
    @Column(name = "BLOCK_BILLING_RATE")
    private BigDecimal blockBillingRate = new BigDecimal(5.36);
    @Column(name = "LATENESS_MARGIN_MINUTES")
    private int latenessMarginMinutes = 5;
    @Column(name = "BLOCKS_PER_MILE")
    private double blocksPerMile = 5.2;
    @Column(name = "COURIER_MILES_PER_HOUR")
    private double courierMilesPerHour = 5.8;
    @Transient
    private String mapFile = "map/map.csv";

    public final DateTimeFormatter acmeDF = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");

    public Company() {
        // TODO initialize company
        File file = new File(mapFile);
        this.map = new Map(file);

        couriers = new HashMap<UUID, Courier>();
        tickets = new HashMap<UUID, Ticket>();
        customer = new HashMap<UUID, Customer>();
        users = new HashMap<UUID, User>();

        Random rand = new Random();
        // TODO remove test customers and couriers
        for (int i = 0; i < 10; i++) {
            Courier c1 = new Courier();
            c1.setName("That Guy " + i);
            couriers.put(c1.getId(), c1);
            Customer c2 = new Customer();
            c2.setName("That Customer " + (1000 + i));
            MapIntersection [][] mapI = map.getMap();
            // Put company on map
            c2.setIntersection(mapI[rand.nextInt(mapI.length)][rand.nextInt(mapI[0].length)]);
            customer.put(c2.getId(), c2);

            User u1 = new User();
            u1.setName("First " + "Last" + i);
            u1.setUsername("uname " + i);
            if (i % 2 == 0) {
                u1.setActive(true);
            } else {
                u1.setActive(false);
            }
            u1.setAdmin(false);
            u1.setPassword("pass"+i);
            users.put(UUID.randomUUID(), u1);
        }
    }

    public UUID getId() {
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

    public HashMap<UUID, User> getUsers() {
        return users;
    }

    public void setUsers(HashMap<UUID, User> users) {
        this.users = users;
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

    public static Company getDefaultAcme() {
        Company acme = new Company();

        acme.setName("Acme");
        acme.setCourierMilesPerHour(15);
        acme.setBlocksPerMile(5.5);
        acme.setLatenessMarginMinutes(2);

        return acme;
    }

}
