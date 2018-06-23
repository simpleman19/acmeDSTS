package acme.pd;

import acme.ui.AcmeUI;
import org.hibernate.cache.spi.DirectAccessRegion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.IntStream;

public class Path {
    private ArrayList<MapIntersection> path;
    private int blocksBetweenHomeandDropoff;
    private int blocksBetweenHomeandPickup;
    private int blocksBetweenPickupandDropoff;
    private int blocksBetweenDropoffandHome;


    public ArrayList<MapIntersection> getPath() {
        return path;
    }

    public void setPath(ArrayList<MapIntersection> path) {
        this.path = path;
    }
    
    public int getBlocksBetweenHomeandDropoff() {
      return blocksBetweenHomeandDropoff;
    }

    public void setBlocksBetweenHomeandDropoff(int blocksBetweenHomeandDropoff) {
      this.blocksBetweenHomeandDropoff = blocksBetweenHomeandDropoff;
    }
    
    public int getBlocksBetweenHomeandPickup() {
      return blocksBetweenHomeandPickup;
    }

    public void setBlocksBetweenHomeandPickup(int blocksBetweenHomeandPickup) {
      this.blocksBetweenHomeandPickup = blocksBetweenHomeandPickup;
    }

    public int getBlocksBetweenPickupandDropoff() {
        return blocksBetweenPickupandDropoff;
    }

    public void setBlocksBetweenPickupandDropoff(int blocksBetweenPickupandDropoff) {
        this.blocksBetweenPickupandDropoff = blocksBetweenPickupandDropoff;
    }
    
    public int getBlocksBetweenDropoffandHome() {
      return blocksBetweenDropoffandHome;
    }

  public void setBlocksBetweenDropoffandHome(int blocksBetweenDropoffandHome) {
      this.blocksBetweenDropoffandHome = blocksBetweenDropoffandHome;
  }
   

    public ArrayList<String> getDeliveryInstructions(Company company) {
        ArrayList<String> instructions = new ArrayList<>();
        if (this.path != null && this.path.size() > 0) {
            Direction lastDir = company.getMap().getTravelDirection(this.path.get(0), this.path.get(1));
            Direction dir = null;
            for (int i = 1; i < this.path.size() - 1; i++) {
                dir = company.getMap().getTravelDirection(this.path.get(i), this.path.get(i+1));
                while (dir.toString().equalsIgnoreCase(lastDir.toString()) && i < this.path.size() - 2) {
                    i++;
                    dir = company.getMap().getTravelDirection(this.path.get(i), this.path.get(i+1));
                }
                Road road = null;
                if (lastDir == Direction.EAST || lastDir == Direction.WEST) {
                    road = this.path.get(i).getEWroad();
                } else {
                    road = this.path.get(i).getNSroad();
                }
                instructions.add("Go " + lastDir.toString() + " on " + road.getName() + " to "
                        + this.path.get(i).getIntersectionName());
                lastDir = dir;
            }
            Road road = null;
            if (lastDir == Direction.EAST || lastDir == Direction.WEST) {
                road = this.path.get(this.path.size() - 1).getEWroad();
            } else {
                road = this.path.get(this.path.size() - 1).getNSroad();
            }
            instructions.add("Go " + dir.toString() + " on " + road.getName() + " to "
                    + this.path.get(this.path.size() - 1).getIntersectionName());
            instructions.add("You have arrived at " + this.path.get(this.path.size() - 1).getIntersectionName());
        }
        return instructions;
    }

    public static void main(String [] args) {
        AcmeUI acme = new AcmeUI();
        for (Ticket t : acme.getCompany().getTickets().values()) {
            if (t.getDeliveryTime() != null) {
                ArrayList<String> instructions = t.getPath().getDeliveryInstructions(acme.getCompany());
                for (String s : instructions) {
                    System.out.println(s);
                }
            }
        }

    }
}
