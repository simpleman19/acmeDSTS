package acme.ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import acme.pd.Company;
import acme.pd.User;

public class ClerksUpdatePanel extends AcmeBaseJPanel {
    private static final long serialVersionUID = 1L;
    /* Declarations */
    private Company company;
    private User user;
    private JLabel mainLbl = new JLabel("Clerk");
    private JLabel subLbl = new JLabel("sub");
    private JLabel nameLbl = new JLabel("Name");
    private JTextField nameFld = new JTextField();
    private JLabel uNameLbl = new JLabel("Username");
    private JTextField uNameFld = new JTextField();
    /* Reset Password segment */
    private JPanel resetPanel = new JPanel();
    private JButton rstBtn = new JButton("Reset Password");
    private JLabel newPassLbl = new JLabel("New Password");
    private JLabel confPassLbl = new JLabel("Confirm Password");
    private JTextField newPassFld = new JTextField();
    private JTextField confPassFld = new JTextField();
    /* User is active */
    private JCheckBox activeChk = new JCheckBox("Active");
    private JCheckBox adminChk = new JCheckBox("Admin");
    private JButton cnclBtn = new JButton("Cancel");
    private JButton saveBtn = new JButton("Save");
    final String NEW_USER = " - (New User)";

    /* Constructor */
    public ClerksUpdatePanel(User user) {

        // If the parameter is null, assume it is a new user
        if (user == null) {
            user = new User();
            subLbl.setText(NEW_USER);
        } else {
            subLbl.setText(" - (Update) " + user.getUsername());
        }
        this.user = user;
    }

    @Override
    public void buildPanel() {
        company = this.getCompany();
        initLayout();
        initDefaults();
        initListeners();
    }

    /* All listeners for the page */
    private void initListeners() {

        final String CANCEL = "c", SAVE = "s", RESET = "r", ACTIVE = "a";
        cnclBtn.setActionCommand(CANCEL);
        saveBtn.setActionCommand(SAVE);
        rstBtn.setActionCommand(RESET);
        activeChk.setActionCommand(ACTIVE);

        ActionListener btnListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                switch (e.getActionCommand()) {
                case CANCEL:
                    // go back to the user list
                    getAcmeUI().userList();
                    break;
                case SAVE:
                    // save the info
                    saveInfo();
                    break;
                case RESET:
                    // pop-up for the password reset
                    displayPassword(!newPassLbl.isVisible());
                    if (newPassLbl.isVisible()) {
                        rstBtn.setText("Hide Password");
                    } else {
                        rstBtn.setText("Show Password");
                    }
                    break;
                case ACTIVE:
                    // set the user active or inactive based on the selection
                    user.setActive(activeChk.isSelected());
                default:
                    break;
                }
            }

        };

        cnclBtn.addActionListener(btnListener);
        saveBtn.addActionListener(btnListener);
        rstBtn.addActionListener(btnListener);
        activeChk.addActionListener(btnListener);

    }

    /* Setup the panel component defaults */
    private void initDefaults() {

        if (subLbl.getText().equals(NEW_USER)) {
            nameFld.setText("");
            uNameFld.setText("");
            activeChk.setSelected(true);
            newPassLbl.setVisible(true);
            newPassFld.setVisible(true);
            confPassLbl.setVisible(true);
            confPassFld.setVisible(true);
            rstBtn.setText("Hide Password");
        } else {
            nameFld.setText(user.getName());
            uNameFld.setText(user.getUsername());
            activeChk.setSelected(user.isActive());
            adminChk.setSelected(user.isAdmin());
            newPassLbl.setVisible(false);
            newPassFld.setText(user.getPassword());
            newPassFld.setVisible(false);
            confPassLbl.setVisible(false);
            confPassFld.setText(user.getPassword());
            confPassFld.setVisible(false);
            rstBtn.setVisible(true);
        }

    }

    private void saveInfo() {
        // check for userName exists
        boolean saveUser = true;
        boolean overwrite = false;
        for (Map.Entry<UUID, User> users : company.getUsers().entrySet()) {
            if (users.getValue().getUsername().equals(uNameFld.getText())) {
                // The user is in the system, do we want to update?
                overwrite = true;
                String infoMessage = "Are you sure you want to update \"" + uNameFld.getText() + "\" ?";
                if (JOptionPane.showConfirmDialog(null, infoMessage, "Wait",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    // switch to the user we found so we can update that one
                    user = users.getValue();
                } else {
                    // We decided no to save overwrite the user
                    saveUser = false;
                }
            }
        }

        // If we are supposed to save the user, do some simple checks
        if (saveUser) {
            // check for fields
            if (nameFld.getText().isEmpty() || uNameFld.getText().isEmpty()) {
                //error
                JOptionPane.showMessageDialog(null, "Missing Name or Username");
            } else {

                // save and go back to user list
                user.setName(nameFld.getText());
                user.setUsername(uNameFld.getText());
                user.setActive(activeChk.isSelected());
                user.setAdmin(adminChk.isSelected());
                // If the password fields are good, update the user's password
                if (checkPassword()) {
                    user.setPassword(newPassFld.getText());
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Password");
                    return;
                }
                /* If this was a new user, we need to add it to the users map */
                if (subLbl.getText().equals(NEW_USER) && !overwrite) {
                    company.getUsers().put(UUID.randomUUID(), user);
                }
                // return to the Clerks list page
                getAcmeUI().userList();
            }
        }

    }

    // This will display all of the password fields
    private void displayPassword(boolean on) {
        newPassLbl.setVisible(on);
        newPassFld.setVisible(on);
        confPassLbl.setVisible(on);
        confPassFld.setVisible(on);
    }

    /* Returns true if the passwords match and fields aren't empty */
    private boolean checkPassword() {
        return (newPassFld.getText().equals(confPassFld.getText()) && !newPassFld.getText().isEmpty());
    }

    /* All of the layout for the panel */
    private void initLayout() {
        mainLbl.setFont(new Font(mainLbl.getFont().toString(), Font.BOLD, 16));
        subLbl.setFont(new Font(subLbl.getFont().toString(), Font.PLAIN, 16));
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 35, 76, 170, 65, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 29, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);
        GridBagConstraints gbc_mainLbl = new GridBagConstraints();
        gbc_mainLbl.anchor = GridBagConstraints.EAST;
        gbc_mainLbl.insets = new Insets(15, 0, 25, 5);
        gbc_mainLbl.gridx = 1;
        gbc_mainLbl.gridy = 0;
        add(mainLbl, gbc_mainLbl);
        GridBagConstraints gbc_subLbl = new GridBagConstraints();
        gbc_subLbl.gridx = 2;
        gbc_subLbl.gridy = 0;
        gbc_subLbl.insets = new Insets(15, 0, 25, 5);
        gbc_subLbl.anchor = GridBagConstraints.WEST;
        add(subLbl, gbc_subLbl);
        GridBagConstraints gbc_nameLbl = new GridBagConstraints();
        gbc_nameLbl.anchor = GridBagConstraints.SOUTHEAST;
        gbc_nameLbl.insets = new Insets(0, 0, 5, 5);
        gbc_nameLbl.gridx = 1;
        gbc_nameLbl.gridy = 1;
        add(nameLbl, gbc_nameLbl);
        GridBagConstraints gbc_nameFld = new GridBagConstraints();
        gbc_nameFld.anchor = GridBagConstraints.SOUTH;
        gbc_nameFld.fill = GridBagConstraints.HORIZONTAL;
        gbc_nameFld.insets = new Insets(0, 0, 5, 5);
        gbc_nameFld.gridx = 2;
        gbc_nameFld.gridy = 1;
        add(nameFld, gbc_nameFld);
        GridBagConstraints gbc_activeChk = new GridBagConstraints();
        gbc_activeChk.insets = new Insets(0, 0, 5, 5);
        gbc_activeChk.gridx = 3;
        gbc_activeChk.gridy = 1;
        add(activeChk, gbc_activeChk);
        GridBagConstraints gbc_uNameLbl = new GridBagConstraints();
        gbc_uNameLbl.anchor = GridBagConstraints.SOUTHEAST;
        gbc_uNameLbl.insets = new Insets(0, 0, 5, 5);
        gbc_uNameLbl.gridx = 1;
        gbc_uNameLbl.gridy = 2;
        add(uNameLbl, gbc_uNameLbl);
        GridBagConstraints gbc_uNameFld = new GridBagConstraints();
        gbc_uNameFld.anchor = GridBagConstraints.SOUTH;
        gbc_uNameFld.fill = GridBagConstraints.HORIZONTAL;
        gbc_uNameFld.insets = new Insets(0, 0, 5, 5);
        gbc_uNameFld.gridx = 2;
        gbc_uNameFld.gridy = 2;
        add(uNameFld, gbc_uNameFld);
        GridBagConstraints gbc_adminChk = new GridBagConstraints();
        gbc_adminChk.insets = new Insets(0, 0, 5, 5);
        gbc_adminChk.gridx = 3;
        gbc_adminChk.gridy = 2;
        add(adminChk, gbc_adminChk);
        GridBagConstraints gbc_resetPanel = new GridBagConstraints();
        gbc_resetPanel.gridwidth = 2;
        gbc_resetPanel.fill = GridBagConstraints.BOTH;
        gbc_resetPanel.insets = new Insets(0, 0, 5, 5);
        gbc_resetPanel.gridx = 2;
        gbc_resetPanel.gridy = 3;
        add(resetPanel, gbc_resetPanel);
        GridBagLayout gbl_resetPanel = new GridBagLayout();
        gbl_resetPanel.columnWidths = new int[] { 162, 0, 0 };
        gbl_resetPanel.rowHeights = new int[] { 23, 0, 0, 0 };
        gbl_resetPanel.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
        gbl_resetPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
        resetPanel.setLayout(gbl_resetPanel);
        GridBagConstraints gbc_rstBtn = new GridBagConstraints();
        gbc_rstBtn.anchor = GridBagConstraints.NORTHWEST;
        gbc_rstBtn.insets = new Insets(15, 0, 5, 5);
        gbc_rstBtn.gridx = 0;
        gbc_rstBtn.gridy = 0;
        resetPanel.add(rstBtn, gbc_rstBtn);
        GridBagConstraints gbc_newPass = new GridBagConstraints();
        gbc_newPass.fill = GridBagConstraints.HORIZONTAL;
        gbc_newPass.insets = new Insets(0, 0, 5, 5);
        gbc_newPass.gridx = 0;
        gbc_newPass.gridy = 1;
        resetPanel.add(newPassFld, gbc_newPass);
        GridBagConstraints gbc_newPassLbl = new GridBagConstraints();
        gbc_newPassLbl.anchor = GridBagConstraints.WEST;
        gbc_newPassLbl.insets = new Insets(0, 0, 5, 0);
        gbc_newPassLbl.gridx = 1;
        gbc_newPassLbl.gridy = 1;
        resetPanel.add(newPassLbl, gbc_newPassLbl);
        GridBagConstraints gbc_confPass = new GridBagConstraints();
        gbc_confPass.fill = GridBagConstraints.HORIZONTAL;
        gbc_confPass.insets = new Insets(0, 0, 0, 5);
        gbc_confPass.gridx = 0;
        gbc_confPass.gridy = 2;
        resetPanel.add(confPassFld, gbc_confPass);
        GridBagConstraints gbc_confPassLbl = new GridBagConstraints();
        gbc_confPassLbl.anchor = GridBagConstraints.WEST;
        gbc_confPassLbl.gridx = 1;
        gbc_confPassLbl.gridy = 2;
        resetPanel.add(confPassLbl, gbc_confPassLbl);
        GridBagConstraints gbc_cnclBtn = new GridBagConstraints();
        gbc_cnclBtn.insets = new Insets(0, 0, 15, 15);
        gbc_cnclBtn.gridx = 3;
        gbc_cnclBtn.gridy = 4;
        add(cnclBtn, gbc_cnclBtn);
        GridBagConstraints gbc_saveBtn = new GridBagConstraints();
        gbc_saveBtn.insets = new Insets(0, 0, 15, 15);
        gbc_saveBtn.gridx = 4;
        gbc_saveBtn.gridy = 4;
        add(saveBtn, gbc_saveBtn);
    }

}
