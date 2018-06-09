package acme.pd;

import java.time.LocalDate;

public class MapIntersection {
	private Road NSroad;
	private Road EWroad;
	private LocalDate closedTo;
	private LocalDate closedFrom;
	private boolean closedIndefinitely;

	MapIntersection() {
		this.closedIndefinitely = false;
		//prevent these from returning null when we check the date closures
		this.closedFrom = LocalDate.MAX;
		this.closedTo = LocalDate.MAX;
	}

	public String getIntersectionName() {
		// TODO get name
		return NSroad.getName() + " & " + EWroad.getName();
	}

	public boolean isClosed(LocalDate date) {
		/*
		 * if the intersection is closed now, during the date range, or closed
		 * indefinitely
		 */
		return (date.isAfter(this.getClosedFrom()) && date.isBefore(this.getClosedTo()))
				|| (date.isEqual(this.getClosedFrom()) || date.isEqual(this.getClosedTo()))
				|| (this.isClosedIndefinitely());
		
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
