package acme.ui;

import acme.pd.Company;

import javax.swing.JPanel;

public class AcmeBaseJPanel extends JPanel {

    public AcmeBaseJPanel() {
        super();
    }


    public void buildPanel() {
        // Initialize your panel here
        //throw new UnsupportedOperationException("Not Implemented");
    }

    public Company getCompany() {
        Object acme = this.getTopLevelAncestor();
        if (acme instanceof AcmeUI) {
            return ((AcmeUI) acme).getCompany();
        } else {
            return null;
        }
    }

    public AcmeUI getAcmeUI() {
        Object ui = this.getTopLevelAncestor();
        if (ui instanceof AcmeUI) {
            return (AcmeUI) ui;
        } else {
            return null;
        }
    }
}
