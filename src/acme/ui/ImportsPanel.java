package acme.ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

import acme.pd.Company;

public class ImportsPanel extends AcmeBaseJPanel {

    private static final long serialVersionUID = 1L;
    /* Declarations */
    Company company;
    JLabel mainLbl = new JLabel("Import");
    static final String COURIER = "Courier", CUSTOMER = "Customer";
    JComboBox<String> typeSel = new JComboBox<String>();
    JFileChooser chooser = new JFileChooser();
    JButton selectBtn = new JButton("Select File");
    JButton saveBtn = new JButton("Save");

    File importFile;

    /* Constructor */
    public ImportsPanel() {
        // buildPanel();
    }

    public void buildPanel() {
        company = this.getCompany();
        initLayout();
        initDefaults();
        initListeners();
    }

    private void initListeners() {
        final String SAVE = "save", SELECT = "select";
        selectBtn.setActionCommand(SELECT);
        saveBtn.setActionCommand(SAVE);

        ActionListener listener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                switch (e.getActionCommand()) {
                case SAVE:
                    // TODO save list to HashMap
                    // check for existing

                    // save to hashmap

                    break;
                case SELECT:
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Comma Separated Value Files", "csv");
                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showOpenDialog(null);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
                        importFile = chooser.getSelectedFile();
                        if (typeSel.getSelectedItem().toString().equals(COURIER)) {
                            parseCourier();
                        } else {
                            parseCustomer();
                        }
                        saveBtn.setVisible(true);
                        saveBtn.setEnabled(true);
                    }
                    break;
                default:
                    break;
                }
            }

        };

        selectBtn.addActionListener(listener);
        saveBtn.addActionListener(listener);

    }

    private void initDefaults() {
        saveBtn.setVisible(false);
        typeSel.addItem(COURIER);
        typeSel.addItem(CUSTOMER);

    }

    private void parseCourier() {
        Scanner inputStream = null;
        List<List<String>> lines = new ArrayList<>();
        try {
            inputStream = new Scanner(importFile);
            while (inputStream.hasNext()) {
                String line = inputStream.next();
                String[] values = line.split(",");
                // this adds the currently parsed line to the 2-dimensional string array
                lines.add(Arrays.asList(values));
                System.out.println(lines);
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            // TODO add a popup for when there is no file
            e.printStackTrace();
        }
    }

    private void parseCustomer() {

    }

    private void initLayout() {
        mainLbl.setFont(new Font(mainLbl.getFont().toString(), Font.BOLD, 16));
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 62, 168, 28, 81, 57, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 23, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);
        GridBagConstraints gbc_mainLbl = new GridBagConstraints();
        gbc_mainLbl.anchor = GridBagConstraints.WEST;
        gbc_mainLbl.insets = new Insets(25, 15, 10, 10);
        gbc_mainLbl.gridx = 0;
        gbc_mainLbl.gridy = 0;
        add(mainLbl, gbc_mainLbl);
        GridBagConstraints gbc_typeSel = new GridBagConstraints();
        gbc_typeSel.fill = GridBagConstraints.HORIZONTAL;
        gbc_typeSel.insets = new Insets(0, 0, 5, 5);
        gbc_typeSel.gridx = 1;
        gbc_typeSel.gridy = 1;
        add(typeSel, gbc_typeSel);
        GridBagConstraints gbc_selectBtn = new GridBagConstraints();
        gbc_selectBtn.anchor = GridBagConstraints.NORTHWEST;
        gbc_selectBtn.insets = new Insets(0, 0, 5, 5);
        gbc_selectBtn.gridx = 3;
        gbc_selectBtn.gridy = 1;
        add(selectBtn, gbc_selectBtn);
        GridBagConstraints gbc_saveBtn = new GridBagConstraints();
        gbc_saveBtn.insets = new Insets(0, 0, 5, 5);
        gbc_saveBtn.anchor = GridBagConstraints.NORTHWEST;
        gbc_saveBtn.gridx = 3;
        gbc_saveBtn.gridy = 2;
        add(saveBtn, gbc_saveBtn);

    }

}
