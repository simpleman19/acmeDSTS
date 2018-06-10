package acme.pd;

import java.util.Random;

public class Road {
	private Direction direction;
	private String name;
	private boolean bidirectional;

	public boolean canTravelDirection(Direction dir) {
		// TODO can travel direction
		return this.getDirection() == dir
				|| (this.isBidirectional() 
				&& (dir.ordinal() % 2 == this.getDirection().ordinal() % 2));
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
}
