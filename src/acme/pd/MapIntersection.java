package acme.pd;

import java.time.LocalDateTime;
import java.util.Random;

public class MapIntersection {
    private Road NSroad;
    private Road EWroad;
    private LocalDateTime closedTo;
    private LocalDateTime closedFrom;
    private boolean closedIndefinitely;

    public MapIntersection() {
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
        //return new Random().nextInt() % 2 == 0;
      return false;
    }

    public boolean canTravelDirection(Direction dir) {
        // TODO can travel
      if(dir == Direction.NORTH || dir == Direction.SOUTH)
      {
        return this.NSroad.canTravelDirection(dir);
      }
      else
      {
        return this.EWroad.canTravelDirection(dir);
      }
         
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

    public LocalDateTime getClosedTo() {
        return closedTo;
    }

    public void setClosedTo(LocalDateTime closedTo) {
        this.closedTo = closedTo;
    }

    public LocalDateTime getClosedFrom() {
        return closedFrom;
    }

    public void setClosedFrom(LocalDateTime closedFrom) {
        this.closedFrom = closedFrom;
    }

    public boolean isClosedIndefinitely() {
        return closedIndefinitely;
    }

    public void setClosedIndefinitely(boolean closedIndefinitely) {
        this.closedIndefinitely = closedIndefinitely;
    }
}
