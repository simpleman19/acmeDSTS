package acme.ui;

import acme.pd.User;

import javax.swing.*;
import java.awt.*;

public class ExampleJPanel extends AcmeBaseJPanel {

    JLabel companyName;

    int name;
    String [] names = {"Acme", "A Company", "Walmart", "Target"};

    public ExampleJPanel() {
        super();
        // Add any constructor logic here that doesn't need the parent jframe
        this.name = 0;
    }


    public void buildPanel() {
        // Initialize your panel here
        this.removeAll();
        this.setLayout(new FlowLayout());

        this.add(new JLabel("Company Name: "));
        companyName = new JLabel(this.getCompany().getName()); 
        this.add(companyName);
        JButton changeName = new JButton("Change Name");
        changeName.addActionListener(actionEvent -> newName());
        this.add(changeName);

        if (this.getCompany().getCurrentUser() == null) {
            JButton login = new JButton("Login");
            login.addActionListener(actionEvent -> {
                this.getCompany().setCurrentUser(new User());
                this.getCompany().getCurrentUser().setAdmin(false);
                this.getAcmeUI().ticketList(false);
            });
            this.add(login);

            JButton loginA = new JButton("Login Admin");
            loginA.addActionListener(actionEvent -> {
                this.getCompany().setCurrentUser(new User());
                this.getCompany().getCurrentUser().setAdmin(true);
                this.getAcmeUI().ticketList(false);
            });
            this.add(loginA);
        }
        this.revalidate();
        this.repaint();
    }

    private void newName() {
        if (name == this.names.length) name = 0;
        this.getCompany().setName(this.names[name++]);
        this.buildPanel();
    }

    public static void main(String [] args) {
        AcmeUI acme = new AcmeUI();
        acme.getCompany().setCurrentUser(new User());
        acme.setPanel(new ExampleJPanel());
    }

}
