package acme.ui;

import acme.pd.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExampleJPanel extends AcmeJPanelBase {

    JLabel companyName;

    int name = 0;
    String [] names = {"Acme", "A Company", "Walmart", "Target"};

    public ExampleJPanel() {
        super();
        // Add any constructor logic here that doesn't need the parent jframe
    }


    public void buildPanel() {
        // Initialize your panel here
        this.setLayout(new FlowLayout());

        this.add(new JLabel("Company Name: "));
        companyName = new JLabel(this.getCompany().getName());
        this.add(companyName);
        JButton changeName = new JButton("Change Name");
        changeName.addActionListener(actionEvent -> newName());
        this.add(changeName);
    }

    private void newName() {
        if (name == this.names.length) name = 0;
        this.getCompany().setName(this.names[name++]);
        this.companyName.setText(this.getCompany().getName());
    }

    public static void main(String [] args) {
        AcmeUI acme = new AcmeUI();
        acme.setPanel(new ExampleJPanel());
        acme.getCompany().setCurrentUser(new User());
    }

}
