package acme.ui;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import acme.pd.Company;
import acme.pd.Courier;
import acme.pd.Customer;
import acme.pd.Road;

public class CourierCreatePanel extends AcmeBaseJPanel {
    Company company;
    Courier courier;
    private JTextField nameField;
    private JLabel numberField;
    private JCheckBox activeCheck;
    private boolean newCourier = true;

    public CourierCreatePanel(Courier courier) {
        super();
        this.courier = courier;
    }

    @Override
    public void buildPanel() {
        company = this.getCompany();
        if (courier != null) {
            newCourier = false;
        } else {
            courier = new Courier();
            courier.create();
        }
        this.removeAll();

        setLayout(null);

        JLabel titleLabel = new JLabel("Courier");
        titleLabel.setBounds(42, 30, 75, 22);
        titleLabel.setFont(new Font(titleLabel.getFont().toString(), Font.PLAIN, 18));
        add(titleLabel);

        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(57, 88, 46, 14);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(113, 85, 125, 20);
        add(nameField);
        nameField.setColumns(10);

        activeCheck = new JCheckBox("Active");
        activeCheck.setBounds(250, 85, 100, 14);
        activeCheck.setSelected(true);
        add(activeCheck);

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

        if (!newCourier) {
            nameField.setText(courier.getName());
            numberField.setText(Integer.toString(courier.getCourierNumber()));
            activeCheck.setSelected(courier.isActive());
        }

        this.revalidate();
        this.repaint();
    }

    private void navigateToPreviousPage() {
        this.getAcmeUI().courierList();
    }

    private void cancelCreate() {
        if (newCourier) {
            courier.delete();
        }
        navigateToPreviousPage();
    }

    private void updateCourierFromFields() {
        courier.setName(nameField.getText());
        courier.setCourierNumber(Integer.parseInt(numberField.getText()));
        courier.setActive(activeCheck.isSelected());
    }

    private void saveCourier() {
        updateCourierFromFields();
        if (newCourier) {
            courier = courier.update();
            company.addCourier(courier);
        }
        company.update();
        navigateToPreviousPage();
    }

    public static void main(String[] args) {
        AcmeUI acme = new AcmeUI();
        acme.setPanel(new CourierCreatePanel(null));
    }
}