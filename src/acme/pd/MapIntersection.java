package acme.pd;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

public class MapIntersection {
    private Road NSroad;
    private Road EWroad;
    private LocalDate closedTo;
    private LocalDate closedFrom;
    private boolean closedIndefinitely;

    MapIntersection() {
        this.closedIndefinitely = false;
        this.closedFrom = null;
        this.closedTo = null;
    }

    public String getIntersectionName() {
        // TODO get name
        return NSroad.getName() + " & " + EWroad.getName();
    }

    public boolean isClosed(LocalDateTime dateTime) {
        // TODO closed intersection
        return new Random().nextInt() % 2 == 0;
    }

    public boolean canTravelDirection(Direction dir) {
        // TODO can travel
        return this.NSroad.canTravelDirection(dir) || this.EWroad.canTravelDirection(dir);
    }

    public Road getNSroad() {
        return NSroad;
    }

    public void setNSroad(Road NSroad) {
        this.NSroad = NSroad;
    }

    public Road getEWroad() {
        return EWroad;
    }

    public void setEWroad(Road EWroad) {
        this.EWroad = EWroad;
    }

    public LocalDate getClosedTo() {
        return closedTo;
    }

    public void setClosedTo(LocalDate localDate) {
        this.closedTo = localDate;
    }

    public LocalDate getClosedFrom() {
        return closedFrom;
    }

    public void setClosedFrom(LocalDate closedFrom) {
        this.closedFrom = closedFrom;
    }

    public boolean isClosedIndefinitely() {
        return closedIndefinitely;
    }

    public void setClosedIndefinitely(boolean closedIndefinitely) {
        this.closedIndefinitely = closedIndefinitely;
    }
}
