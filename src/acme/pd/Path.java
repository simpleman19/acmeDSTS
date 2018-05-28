package acme.pd;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public class Path {
    private ArrayList<MapIntersection> path;
    private int blocksBetweenPickupandDropoff;

    public ArrayList<MapIntersection> getPath() {
        return path;
    }

    public void setPath(ArrayList<MapIntersection> path) {
        this.path = path;
    }

    public int getBlocksBetweenPickupandDropoff() {
        return blocksBetweenPickupandDropoff;
    }

    public void setBlocksBetweenPickupandDropoff(int blocksBetweenPickupandDropoff) {
        this.blocksBetweenPickupandDropoff = blocksBetweenPickupandDropoff;
    }

    public ArrayList<String> getDeliveryInstructions() {
        // TODO implement delivery instructions
        ArrayList<String> instructions = new ArrayList<>();
        Random rand = new Random();
        IntStream.range(0, 10).forEach(i -> instructions.add("Go " + Direction.values()[rand.nextInt() % 4] + " 1 block"));
        return instructions;
    }
}
