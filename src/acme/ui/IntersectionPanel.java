package acme.ui;

import acme.pd.MapIntersection;
import acme.ui.AcmeBaseJPanel;

public class IntersectionPanel extends AcmeBaseJPanel {

	public IntersectionPanel(MapIntersection intersection) {
		System.out.println("Intersection page for " + intersection.getIntersectionName());
	}
}
