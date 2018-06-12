package acme.ui;

import acme.pd.Company;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import java.awt.*;

public class AcmeJPanelBase extends JPanel {

    public AcmeJPanelBase() {
        super();

    }


    public void buildPanel() {
        throw new NotImplementedException();
    }

    public Company getCompany() {
        Object acme = this.getTopLevelAncestor();
        if (acme instanceof AcmeUI) {
            return ((AcmeUI) acme).getCompany();
        } else {
            return null;
        }
    }
}
