package acme.ui;

import java.awt.Font;
import java.util.Set;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import acme.pd.Company;
import acme.pd.Customer;
import acme.pd.Road;

public class CustomerCreatePanel extends AcmeBaseJPanel {
	Company company;
	Customer customer;
	private JTextField nameField;
	private JLabel numberField;
	private JComboBox<Road> avenueComboField;
	private JComboBox<Road> streetComboField;
	private Consumer<Customer> onSaveFunc;
	
	public CustomerCreatePanel(Consumer<Customer> onSave) {
		super();
        customer = new Customer();
        customer.create();
        this.onSaveFunc = onSave;
	}
	
	@Override
    public void buildPanel() {
        company = this.getCompany();
        this.removeAll();
        
        setLayout(null);
        
        JLabel titleLabel = new JLabel("Customer");
        titleLabel.setBounds(42, 30, 100, 22);
        titleLabel.setFont(new Font(titleLabel.getFont().toString(), Font.PLAIN, 18));
        add(titleLabel);
        
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(57, 88, 46, 14);
        add(nameLabel);
        
        nameField = new JTextField();
        nameField.setBounds(113, 85, 125, 20);
        add(nameField);
        nameField.setColumns(10);
        
        JLabel numberLabel = new JLabel("Number");
        numberLabel.setBounds(57, 126, 46, 14);
        add(numberLabel);
        
        
        numberField = new JLabel(Integer.toString(Customer.getNextCustomerNumber()));
        numberField.setBounds(113, 126, 46, 14);
        add(numberField);
        
        JLabel lblIntersection = new JLabel("Intersection");
        lblIntersection.setFont(new Font(lblIntersection.getFont().toString(), Font.PLAIN, 14));
        lblIntersection.setBounds(57, 166, 75, 22);
        add(lblIntersection);
        
        JLabel avenueLabel = new JLabel("Avenue");
        avenueLabel.setBounds(57, 199, 46, 14);
        add(avenueLabel);
        
        JLabel streetLabel = new JLabel("Street");
        streetLabel.setBounds(57, 240, 46, 14);
        add(streetLabel);
        
        Set<Road> avenues = company.getMap().getAvenues();
        avenueComboField = new JComboBox<Road>(avenues.toArray(new Road[0]));
        avenueComboField.setBounds(113, 196, 125, 20);
        add(avenueComboField);
        
        Set<Road> streets = company.getMap().getStreets();
        streetComboField = new JComboBox<Road>(streets.toArray(new Road[0]));
        streetComboField.setBounds(113, 237, 125, 20);
        add(streetComboField);
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveCustomer());
        saveButton.setBounds(330, 266, 89, 23);
        add(saveButton);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> cancelCreate());
        cancelButton.setBounds(231, 266, 89, 23);
        add(cancelButton);
        
        this.revalidate();
        this.repaint();
	}
	
	private void navigateToPreviousPage() {
		onSaveFunc.accept(customer);
	}
	
	private void cancelCreate() {
		customer.delete();
		navigateToPreviousPage();
	}
	
	private void updateCustomerFromFields() {
		customer.setName(nameField.getText());
		customer.setCustomerNumber(Integer.parseInt(numberField.getText()));
		Road avenue = (Road) avenueComboField.getSelectedItem();
		Road street = (Road) streetComboField.getSelectedItem();
		customer.setAvenueName(avenue.getName());
		customer.setStreetName(street.getName());
		customer.setActive(true);
	}
	
	private void saveCustomer() {
		updateCustomerFromFields();
		customer = customer.update();
		company.addCustomer(customer);
		company.update();
		navigateToPreviousPage();
	}	
	
	public static void main(String [] args) {
        AcmeUI acme = new AcmeUI();
        acme.setPanel(new CustomerCreatePanel(c -> System.out.println("Saved customer with id: " + c.getId())));
    }
}