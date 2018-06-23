package acme.pd;

import java.util.ArrayList;

import acme.ui.AcmeUI;

public class Path {
    private ArrayList<MapIntersection> path;
    private int blocksBetweenHomeandDropoff;
    private int blocksBetweenHomeandPickup;
    private int blocksBetweenPickupandDropoff;
    private int blocksBetweenDropoffandHome;
    private int blocks;

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
        int step;
        if (this.path != null && this.path.size() > 2) {
            Direction lastDir = company.getMap().getTravelDirection(this.path.get(0), this.path.get(1));
            Direction dir = null;
            step = 1;
            for (int i = 1; i < this.path.size() - 1; i++) {
                blocks = 1;
                dir = company.getMap().getTravelDirection(this.path.get(i), this.path.get(i+1));
                step = checkForInteraction(i, step, instructions);
                while (dir.toString().equalsIgnoreCase(lastDir.toString()) && i < this.path.size() - 2) {
                    i++;
                    blocks++;
                    dir = company.getMap().getTravelDirection(this.path.get(i), this.path.get(i+1));
                    step = checkForInteraction(i, step, instructions);
                }

                instructions.add("Step " + step + ": Go " + lastDir.toString() + " on " + getRoadName(i, lastDir) + " for "
                        + blocks + (blocks > 1 ? " Blocks" : " Block"));

                lastDir = dir;
                step++;
            }

            instructions.add("Step " + step + ": Confirm ticket completion with " + company.getName());
        }
        return instructions;
    }

    private String getRoadName(int block, Direction dir) {
        Road road = null;
        String name;
        if (dir == Direction.EAST || dir == Direction.WEST) {
            road = this.path.get(block).getEWroad();
            name = "Street " + road.getName();
        } else if (dir == Direction.NORTH || dir == Direction.SOUTH){
            road = this.path.get(block).getNSroad();
            name = "Avenue " + road.getName();
        }
        else {
            name = "";
        }
        return name;
    }

    private final int checkForInteraction(int block, int step, ArrayList<String> instructions) {
        String action = null;
        if (block == this.getBlocksBetweenHomeandPickup()) {
            action = "Pickup ";
        } else if (block == this.getBlocksBetweenHomeandPickup() + this.getBlocksBetweenPickupandDropoff()) {
            action = "Deliver ";
        } else {
            return step;
        }
        instructions.add("Step " + step + ": " + action + "Package at the intersection of "
                + getRoadName(block, Direction.NORTH) + " and " + getRoadName(block, Direction.EAST));
        return step + 1;
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
