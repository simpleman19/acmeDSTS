package acme.ui;

import acme.pd.User;

import javax.swing.*;
import java.awt.*;

public class ExampleJPanel extends AcmeJPanelBase {

    public ExampleJPanel() {
        super();
    }


    public void buildPanel() {
        this.setLayout(new FlowLayout());

        this.add(new JLabel("Company Name: "));
        this.add(new JLabel(this.getCompany().getName()));
    }

    public static void main(String [] args) {
        AcmeUI acme = new AcmeUI();
        acme.setPanel(new ExampleJPanel());
        acme.getCompany().setCurrentUser(new User());
    }

}
