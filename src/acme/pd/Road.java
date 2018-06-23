package acme.pd;

public class Road implements Comparable {
    private Direction direction;
    private String name;
    private boolean bidirectional;

    public boolean canTravelDirection(Direction dir) {
        return (this.getDirection() == dir
                || (this.isBidirectional()
                && (dir.ordinal() % 2 == this.getDirection().ordinal() % 2)))
                && dir != Direction.NONE;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBidirectional() {
        return bidirectional;
    }

    public void setBidirectional(boolean bidirectional) {
        this.bidirectional = bidirectional;
    }
    
    public int compareTo(Object o) {
    	return this.getName().compareTo(((Road) o).getName());
    }
    
    public boolean equals(Object o) {
    	return this.compareTo(o) == 0;
    }
    
    public String toString() {
    	return this.getName();
    }
}
