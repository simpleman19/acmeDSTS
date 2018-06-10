package acme.pd;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;
//////////GUI/////////
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
///////////////////////
public class Company 
{
    private UUID id;
    private String name = "ACME";
    private Map map;
    private HashMap<UUID, Courier> couriers;
    private HashMap<UUID, Ticket> tickets;
    private HashMap<UUID, Customer> customer;
    private User currentUser;
    private BigDecimal bonus = new BigDecimal(1.25);
    private BigDecimal flatBillingRate = new BigDecimal(25);
    private BigDecimal blockBillingRate = new BigDecimal(5.36);
    private int latenessMarginMinutes = 5;
    private double blocksPerMile = 5.2;
    private double courierMilesPerHour = 5.8;
    
    public Company()
    {      
        // TODO initialize company
        File file = new File("null");
        this.map = new Map(file);

        couriers = new HashMap<UUID, Courier>();
        tickets = new HashMap<UUID, Ticket>();
        customer = new HashMap<UUID, Customer>();
        currentUser = new User();
    }
    

    public UUID getId()
    {
        // TODO fix with database
        if (this.id == null)
        {
            this.id = UUID.randomUUID();
        }
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Map getMap()
    {
        return map;
    }

    public void setMap(Map map)
    {
        this.map = map;
    }

    public HashMap<UUID, Courier> getCouriers()
    {
        return couriers;
    }

    public void setCouriers(HashMap<UUID, Courier> couriers)
    {
        this.couriers = couriers;
    }

    public HashMap<UUID, Ticket> getTickets()
    {
        return tickets;
    }

    public void setTickets(HashMap<UUID, Ticket> tickets)
    {
        this.tickets = tickets;
    }

    public HashMap<UUID, Customer> getCustomer()
    {
        return customer;
    }

    public void setCustomer(HashMap<UUID, Customer> customer)
    {
        this.customer = customer;
    }

    public User getCurrentUser()
    {
        return currentUser;
    }

    public void setCurrentUser(User currentUser)
    {
        this.currentUser = currentUser;
    }

    public BigDecimal getBonus()
    {
        return bonus;
    }

    public void setBonus(BigDecimal bonus)
    {
        this.bonus = bonus;
    }

    public BigDecimal getFlatBillingRate()
    {
        return flatBillingRate;
    }

    public void setFlatBillingRate(BigDecimal flatBillingRate)
    {
        this.flatBillingRate = flatBillingRate;
    }

    public BigDecimal getBlockBillingRate()
    {
        return blockBillingRate;
    }

    public void setBlockBillingRate(BigDecimal blockBillingRate)
    {
        this.blockBillingRate = blockBillingRate;
    }

    public int getLatenessMarginMinutes()
    {
        return latenessMarginMinutes;
    }

    public void setLatenessMarginMinutes(int latenessMarginMinutes)
    {
        this.latenessMarginMinutes = latenessMarginMinutes;
    }

    public double getBlocksPerMile()
    {
        return blocksPerMile;
    }

    public void setBlocksPerMile(double blocksPerMile)
    {
        this.blocksPerMile = blocksPerMile;
    }

    public double getCourierMilesPerHour()
    {
        return courierMilesPerHour;
    }

    public void setCourierMilesPerHour(double courierMilesPerHour)
    {
        this.courierMilesPerHour = courierMilesPerHour;
    }
    
    public static Company getDefaultAcme() {
    	Company acme = new Company();
    	acme.setName("Acme");
    	acme.setBonus(acme.bonus);
    	acme.setCourierMilesPerHour(15);
    	acme.setBlocksPerMile(5.5);
    	acme.setLatenessMarginMinutes(2);
    	acme.setFlatBillingRate(acme.flatBillingRate);
    	acme.setBlockBillingRate(acme.blockBillingRate);
    	
    	return acme; 
    }
   
    
}
