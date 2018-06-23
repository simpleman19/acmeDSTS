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
        MapIntersection pickUp = null;
        MapIntersection dropOff = null;
        if (this.path != null && this.path.size() > 0) {
            Direction lastDir = company.getMap().getTravelDirection(this.path.get(0), this.path.get(1));
            Direction dir = null;
            int step = 0, blocks = 0;
            String interaction = NONE;
            for (int i = 1; i < this.path.size() - 1; i++) {
                pickUp = this.path.get(i);
                step++;
                dir = company.getMap().getTravelDirection(this.path.get(i), this.path.get(i + 1));
                step = checkForInteraction(i, step, instructions);
                while (dir.toString().equalsIgnoreCase(lastDir.toString()) && i < this.path.size() - 2) {
                    i++;
                    step = checkForInteraction(i, step,  instructions);
                    dir = company.getMap().getTravelDirection(this.path.get(i), this.path.get(i + 1));
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
                blocks = p.getBlocksBetweenPickupandDropoff();
                instructions.add("Step " + step + ": Go " + lastDir.toString() + " on " + roadName + " for "
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
            instructions.add("Step " + step + ": Go " + lastDir.toString() + " on " + road.getName() + " for "
                    + blocks + (blocks > 1 ? " Blocks" : " Block")+" to " + this.path.get(this.path.size() -1).getIntersectionName());
            step++;
            instructions.add("Step " + step + ": Confirm ticket completion with " + company.getName());
        }
        return instructions;
    }

    private int checkForInteraction(int count, int step, ArrayList<String> instructions) {
        if (count == this.getBlocksBetweenHomeandPickup()) {
            instructions.add("Step " + step + ": " + PICKUP + " Package at the intersection of "
                    + this.path.get(count).getIntersectionName());
        } else if (count == this.getBlocksBetweenPickupandDropoff()+this.getBlocksBetweenHomeandPickup()) {
            instructions.add("Step " + step + ": " + DELIVER + " Package at the intersection of "
                    + this.path.get(count).getIntersectionName());
        }else {
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
