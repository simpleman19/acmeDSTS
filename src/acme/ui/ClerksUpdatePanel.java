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
import javax.swing.JTextField;

import acme.pd.Company;
import acme.pd.User;

public class ClerksUpdatePanel extends AcmeBaseJPanel {
    private static final long serialVersionUID = 1L;
    private Company company;
    private User user;
    private JLabel mainLbl = new JLabel("Clerk");
    private JLabel subLbl = new JLabel("sub");
    private JLabel nameLbl = new JLabel("Name");
    private JTextField nameFld = new JTextField();
    private JLabel uNameLbl = new JLabel("Username");
    private JTextField uNameFld = new JTextField();
    private JButton rstBtn = new JButton("Reset Password");
    private JCheckBox activeChk = new JCheckBox("Active");
    private JButton cnclBtn = new JButton("Cancel");
    private JButton saveBtn = new JButton("Save");
    final String NEW_USER = " - (New User)";

    /* Constructor */
    public ClerksUpdatePanel(User user) {

        if (user == null) {
            user = new User();
            subLbl.setText(NEW_USER);
        } else {
            subLbl.setText(" - (Update) " + user.getName());
        }
        this.user = user;

    }

    public void buildPanel() {
        company = this.getCompany();
        initLayout();
        initDefaults();
        initListeners();
    }

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
                    resetPassword();
                    break;
                case ACTIVE:
                    // set the user active or inactive based on the selection
                    user.setActive(activeChk.isSelected());
                    System.out.println("User set to " + user.isActive());
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

    private void initDefaults() {
        if (subLbl.getText().equals(NEW_USER)) {
            nameFld.setText("First Last");
            uNameFld.setText("username");
            activeChk.setSelected(true);
        } else {
            nameFld.setText(user.getName());
            uNameFld.setText(user.getUsername());
            activeChk.setSelected(user.isActive());
        }

    }

    private void saveInfo() {
        // TODO save info
        // check for userName exists
        boolean saveUser = true;
        boolean overwrite = false;
        for (Map.Entry<UUID, User> users : company.getUsers().entrySet()) {
            if (users.getValue().getUsername().equals(uNameFld.getText())) {
                overwrite = true;
                String infoMessage = "Are you sure you want to update \"" + uNameFld.getText()
                        + "\" ?";
                if (JOptionPane.showConfirmDialog(null, infoMessage, "Wait",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    // switch to the user we found so we can update it
                    user = users.getValue();
                } else {
                    saveUser = false;
                }
            }
        }

        if (saveUser) {
            // check for fields
            if (nameFld.getText().isEmpty() || uNameFld.getText().isEmpty()) {
                // error
            } else {

                // save and go back to user list
                user.setName(nameFld.getText());
                user.setUsername(uNameFld.getText());
                user.setActive(activeChk.isSelected());
                // set password for new users
                if (subLbl.getText().equals(NEW_USER) && !overwrite) {
                    String pass = resetPassword();
                    user.setPassword(pass);
                    company.getUsers().put(UUID.randomUUID(), user);
                }
                System.out.println(user.getPassword());
                getAcmeUI().userList();
            }
        }

    }

    private String resetPassword() {
        
        String pass = JOptionPane.showInputDialog("New Password");
        if (pass == JOptionPane.showInputDialog("Confirm Password")) {
            return pass;
        }
        else {
            return user.getPassword();
        }
    }

    private void initLayout() {
        mainLbl.setFont(new Font(mainLbl.getFont().toString(), Font.BOLD, 16));
        subLbl.setFont(new Font(subLbl.getFont().toString(), Font.PLAIN, 16));
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 35, 76, 170, 65, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 29, 0, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);
        GridBagConstraints gbc_mainLbl = new GridBagConstraints();
        gbc_mainLbl.anchor = GridBagConstraints.EAST;
        gbc_mainLbl.insets = new Insets(15, 0, 25, 5);
        gbc_mainLbl.gridx = 1;
        gbc_mainLbl.gridy = 0;
        add(mainLbl, gbc_mainLbl);
        GridBagConstraints gbc_subLbl = new GridBagConstraints();
        gbc_subLbl.insets = new Insets(15, 0, 25, 0);
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
        GridBagConstraints gbc_rstBtn = new GridBagConstraints();
        gbc_rstBtn.insets = new Insets(0, 0, 5, 5);
        gbc_rstBtn.anchor = GridBagConstraints.NORTHWEST;
        gbc_rstBtn.gridx = 2;
        gbc_rstBtn.gridy = 3;
        add(rstBtn, gbc_rstBtn);
        GridBagConstraints gbc_cnclBtn = new GridBagConstraints();
        gbc_cnclBtn.insets = new Insets(0, 0, 15, 15);
        gbc_cnclBtn.gridx = 3;
        gbc_cnclBtn.gridy = 5;
        add(cnclBtn, gbc_cnclBtn);
        GridBagConstraints gbc_saveBtn = new GridBagConstraints();
        gbc_saveBtn.insets = new Insets(0, 0, 15, 15);
        gbc_saveBtn.gridx = 4;
        gbc_saveBtn.gridy = 5;
        add(saveBtn, gbc_saveBtn);
    }

}
