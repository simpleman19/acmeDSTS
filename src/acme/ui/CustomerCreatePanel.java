package acme.ui;

import java.util.Collections;
import java.util.Set;

import javax.persistence.NoResultException;

import acme.data.PersistableEntity;
import acme.pd.Company;
import acme.pd.Customer;
import acme.pd.Road;
import acme.pd.User;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;

public class CustomerCreatePanel extends AcmeBaseJPanel {
	Company company;
	Customer customer;
	private JTextField nameField;
	private JLabel numberField;
	private JComboBox<Road> avenueComboField;
	private JComboBox<Road> streetComboField;

	public CustomerCreatePanel() {
        super();
        customer = new Customer();
        customer.create();
    }
	
	@Override
    public void buildPanel() {
        company = this.getCompany();
        this.removeAll();
        
        setLayout(null);
        
        JLabel titleLabel = new JLabel("Customer");
        titleLabel.setBounds(42, 30, 75, 22);
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        add(titleLabel);
        
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(57, 88, 46, 14);
        add(nameLabel);
        
        nameField = new JTextField();
        nameField.setBounds(113, 85, 86, 20);
        add(nameField);
        nameField.setColumns(10);
        
        JLabel numberLabel = new JLabel("Number");
        numberLabel.setBounds(57, 126, 46, 14);
        add(numberLabel);
        
        
        numberField = new JLabel(Integer.toString(customer.getCustomerNumber()));
        numberField.setBounds(113, 126, 46, 14);
        add(numberField);
        
        JLabel lblIntersection = new JLabel("Intersection");
        lblIntersection.setFont(new Font("Tahoma", Font.PLAIN, 14));
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
        avenueComboField.setBounds(113, 196, 86, 20);
        add(avenueComboField);
        
        Set<Road> streets = company.getMap().getStreets();
        streetComboField = new JComboBox<Road>(streets.toArray(new Road[0]));
        streetComboField.setBounds(113, 237, 86, 20);
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
		this.getAcmeUI().courierList();
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
		System.out.println("Saved customer with id: " + customer.getId());
		navigateToPreviousPage();
	}	
	
	public static void main(String [] args) {
        AcmeUI acme = new AcmeUI();
        
        User currentUser;
        try {
        	currentUser = PersistableEntity.querySingle(User.class, "select u from APP_USER u", Collections.EMPTY_MAP);
        } catch(NoResultException e) {
        	currentUser = new User();
        	currentUser.create();
        }
        acme.getCompany().addUser(currentUser);
        acme.getCompany().setCurrentUser(currentUser);
        
        acme.getCompany().setCurrentUser((User) acme.getCompany().getUsers().values().toArray()[0]);
        acme.setPanel(new CustomerCreatePanel());
    }
}