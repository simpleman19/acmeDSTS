package acme.pd;

import java.util.ArrayList;
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
   

    public ArrayList<String> getDeliveryInstructions() {
        // TODO implement delivery instructions
        ArrayList<String> instructions = new ArrayList<>();
        Random rand = new Random();
        IntStream.range(0, 10).forEach(i -> instructions.add("Go " + Direction.values()[rand.nextInt() % 4] + " 1 block"));
        return instructions;
    }
}
