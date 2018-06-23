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
    private static String PICKUP = "Pickup", DELIVER = "Deliver", NONE = "";
    private static boolean change = false;
    private static Direction lastDir;
    private static Direction dir;
    private MapIntersection pickUp;
    private MapIntersection dropOff;

    public ArrayList<MapIntersection> getPath() {
        return path;
    }

    public void setPath(ArrayList<MapIntersection> path) {
        this.path = path;
    }

    //Total blocks from Home Office -> Pick Up -> Drop Off (delivery)
    public int getBlocksBetweenHomeandDropoff() {
        return blocksBetweenHomeandDropoff;
    }

    public void setBlocksBetweenHomeandDropoff(int blocksBetweenHomeandDropoff) {
        this.blocksBetweenHomeandDropoff = blocksBetweenHomeandDropoff;
    }

    //Total blocks from Home Office -> Pick Up
    public int getBlocksBetweenHomeandPickup() {
        return blocksBetweenHomeandPickup;
    }

    public void setBlocksBetweenHomeandPickup(int blocksBetweenHomeandPickup) {
        this.blocksBetweenHomeandPickup = blocksBetweenHomeandPickup;
    }

    //Total blocks from Pick Up -> Drop Off (delivery)
    public int getBlocksBetweenPickupandDropoff() {
        return blocksBetweenPickupandDropoff;
    }

    public void setBlocksBetweenPickupandDropoff(int blocksBetweenPickupandDropoff) {
        this.blocksBetweenPickupandDropoff = blocksBetweenPickupandDropoff;
    }

    //Total blocks from Drop Off (delivery) -> Home Office
    public int getBlocksBetweenDropoffandHome() {
        return blocksBetweenDropoffandHome;
    }

    public void setBlocksBetweenDropoffandHome(int blocksBetweenDropoffandHome) {
        this.blocksBetweenDropoffandHome = blocksBetweenDropoffandHome;
    }

   

    public ArrayList<String> getDeliveryInstructions(Company company) {
        ArrayList<String> instructions = new ArrayList<>();
        if (this.path != null && this.path.size() > 0) {
            lastDir = company.getMap().getTravelDirection(this.path.get(0), this.path.get(1));
            dir = null;
            int step = 0, blocks = 0;
            String interaction = NONE;
            for (int i = 1; i < this.path.size() - 1; i++) {
                pickUp = this.path.get(i);
                blocks = 1;
                step++;
                step = checkForInteraction(i, step, instructions);
                dir = company.getMap().getTravelDirection(this.path.get(i), this.path.get(i + 1));
                
                while (dir.toString().equalsIgnoreCase(lastDir.toString()) && i < this.path.size() - 2) {
                    i++;
                    step = checkForInteraction(i, step,  instructions);
                    dir = company.getMap().getTravelDirection(this.path.get(i), this.path.get(i + 1));
                    blocks++;
                }
                dropOff = this.path.get(i);
                Road road = null;
                String roadName = null;
                if (lastDir == Direction.EAST || lastDir == Direction.WEST) {
                    road = this.path.get(i).getEWroad();
                    roadName = road.getName() + " Street";
                } else {
                    road = this.path.get(i).getNSroad();
                    roadName = "Avenue " + road.getName();
                }

                Path p = company.getMap().getPath(pickUp, dropOff);
                blocks =p.getBlocksBetweenPickupandDropoff();
                if (!change) instructions.add("Step " + step + ": Go " + lastDir.toString() + " on " + roadName + " for "
                        + blocks + (blocks > 1 ? " Blocks" : " Block")+" to " + this.path.get(i).getIntersectionName());

                lastDir = dir;
                pickUp = this.path.get(i + 1);
            }
            Road road = null;
            String roadName = null;
            if (lastDir == Direction.EAST || lastDir == Direction.WEST) {
                road = this.path.get(this.path.size() - 1).getEWroad();
                roadName = "Street " + road.getName();
            } else {
                road = this.path.get(this.path.size() - 1).getNSroad();
                roadName = "Avenue " + road.getName();
            }
            dropOff = this.path.get(this.path.size() - 1);
            Path p = company.getMap().getPath(pickUp, dropOff);
            blocks = p.getBlocksBetweenPickupandDropoff();
            step++;
            instructions.add("Step " + step + ": Continue " +lastDir.toString() + " to " + company.getName());
        }
        return instructions;
    }

    private int checkForInteraction(int count, int step, ArrayList<String> instructions) {
        if (count == this.getBlocksBetweenHomeandPickup()) {
            instructions.add("Step "+ step + ": "+ PICKUP + " Package at the intersection of "
                    + this.path.get(count).getIntersectionName());
            pickUp = this.path.get(count+1);
            change = true;
        } else if (count == this.getBlocksBetweenPickupandDropoff()+this.getBlocksBetweenHomeandPickup()) {
            instructions.add("Step " + step + ": Go " + lastDir.toString() + " to " + DELIVER + " Package at the intersection of "
                    + this.path.get(count).getIntersectionName());
            pickUp = this.path.get(count+1);
            change = true;
        }else {
            change = false;
            return step;
        }
        return step+1;
    }

    public static void main(String[] args) {
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
