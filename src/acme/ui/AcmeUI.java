package acme.ui;

import acme.pd.*;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class AcmeUI extends JFrame {

    private Company company;

    public AcmeUI() {
        super("Acme Delivery Software");

        this.company = new Company();
        this.buildMenu();

        setVisible(true);
        setSize(550, 550);
        setLocationRelativeTo(null);
        addWindowListener(new ShutdownListener());

        loginScreen();
    }

    public void setPanel(AcmeJPanelBase panel) {
        this.getContentPane().removeAll();
        this.getContentPane().add(panel);
        panel.buildPanel();
        this.revalidate();
        this.repaint();
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void loginScreen() {
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void ticketList() {
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void ticketCreate() {
        // This will be called with null to create a ticket
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void ticketComplete(Ticket ticket) {
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void importCustomers() {
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void customerList() {
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void customerAddUpdate(Customer customer) {
        // This will be called with null to create
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void userList() {
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void userAddUpdate(User customer) {
        // This will be called with null to create
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void importCouriers() {
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void courierList() {
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void courierAddUpdate(Courier courier) {
        // This will be called with null to create
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void mapView() {
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    // Everyone will tie in their panel like this.  Replace my example with your code
    public void reports() {
        ExampleJPanel exampleJPanel = new ExampleJPanel();
        this.setPanel(exampleJPanel);
    }

    private void buildMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu ticketMenu = new JMenu("Tickets");
        menuBar.add(ticketMenu);

        // TODO actually implement.  This is temporary to get a base for everyone
        ticketMenu.add(new JMenuItem("Create"));
        ticketMenu.add(new JMenuItem("List"));

        this.setJMenuBar(menuBar);
    }

    public Company getCompany() {
        return company;
    }
}

class ShutdownListener implements WindowListener {
    public void windowClosing(WindowEvent event) {

    }
    public void windowOpened(WindowEvent event) {}
    public void windowClosed(WindowEvent event) {}
    public void windowIconified(WindowEvent event) {}
    public void windowDeiconified(WindowEvent event) {}
    public void windowActivated(WindowEvent event) {}
    public void windowDeactivated(WindowEvent event) {}
}