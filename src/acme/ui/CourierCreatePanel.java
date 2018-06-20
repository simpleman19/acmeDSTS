package acme.ui;

import java.util.Collections;
import java.util.Set;

import javax.persistence.NoResultException;

import acme.data.PersistableEntity;
import acme.pd.Company;
import acme.pd.Courier;
import acme.pd.Road;
import acme.pd.User;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;

public class CourierCreatePanel extends AcmeBaseJPanel {
	Company company;
	Courier courier;
	private JTextField nameField;
	private JLabel numberField;

	public CourierCreatePanel() {
        super();
        courier = new Courier();
        courier.create();
    }
	
	@Override
    public void buildPanel() {
        company = this.getCompany();
        this.removeAll();
        
        setLayout(null);
        
        JLabel titleLabel = new JLabel("Courier");
        titleLabel.setBounds(42, 30, 75, 22);
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
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
        
        
        numberField = new JLabel(Integer.toString(Courier.getNextCourierNumber()));
        numberField.setBounds(113, 126, 46, 14);
        add(numberField);
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveCourier());
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
		courier.delete();
		navigateToPreviousPage();
	}
	
	private void updateCourierFromFields() {
		courier.setName(nameField.getText());
		courier.setCourierNumber(Integer.parseInt(numberField.getText()));
		courier.setActive(true);
	}
	
	private void saveCourier() {
		updateCourierFromFields();
		courier = courier.update();
		company.addCourier(courier);
		company.update();
		System.out.println("Saved courier with id: " + courier.getId());
		navigateToPreviousPage();
	}	
	
	public static void main(String [] args) {
        AcmeUI acme = new AcmeUI();
        acme.setPanel(new CourierCreatePanel());
    }
}