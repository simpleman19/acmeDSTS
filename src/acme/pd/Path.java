package acme.pd;

import acme.ui.AcmeUI;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.IntStream;

public class Path {
    private ArrayList<MapIntersection> path;
    private int blocksBetweenHomeandDropoff;
    private int blocksBetweenHomeandPickup;
    private int blocksBetweenPickupandDropoff;


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
   

    public ArrayList<String> getDeliveryInstructions() {
        ArrayList<String> instructions = new ArrayList<>();
        if (this.path != null && this.path.size() > 0) {
            for (int i = 0; i < this.path.size() - 1; i++) {
                instructions.add("Go from " + this.path.get(i).getIntersectionName() + " to "
                        + this.path.get(i + 1).getIntersectionName());
            }
            instructions.add("You have arrived at " + this.path.get(this.path.size() - 1).getIntersectionName()
                    + " which is your destination");
        }
        return instructions;
    }

    public static void main(String [] args) {
        AcmeUI acme = new AcmeUI();
        for (Ticket t : acme.getCompany().getTickets().values()) {
            if (t.getDeliveryTime() != null) {
                ArrayList<String> instructions = t.getPath().getDeliveryInstructions();
                for (String s : instructions) {
                    System.out.println(s);
                }
            }
        }

    }
}
