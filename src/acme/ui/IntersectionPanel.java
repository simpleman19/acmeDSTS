package acme.ui;

import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;

import acme.pd.MapIntersection;
import acme.ui.AcmeBaseJPanel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class IntersectionPanel extends AcmeBaseJPanel {

	protected MapIntersection intersection;
	final LocalDate NULL_DATE = LocalDate.MAX;
	
	private DatePicker fromDate = new DatePicker();
    private DatePicker toDate = new DatePicker();
    private DatePickerSettings toSettings = new DatePickerSettings();
    private DatePickerSettings fromSettings = new DatePickerSettings();
    private JCheckBox isClosedIndefinitely;
	
	public IntersectionPanel(MapIntersection intersection) {
		this.intersection = intersection;
	}
	
	public void buildPanel() {
		setLayout(null);
		
		JLabel titleLbl = new JLabel("Intersection " + intersection.getIntersectionName());
		titleLbl.setFont(new Font(titleLbl.getFont().toString(), Font.PLAIN, 18));
		titleLbl.setBounds(12, 27, 174, 15);
		add(titleLbl);
		
		JLabel fromLbl = new JLabel("Closed From");
		fromLbl.setFont(new Font(fromLbl.getFont().toString(), Font.PLAIN, 14));
		fromLbl.setBounds(12, 69, 144, 15);
		add(fromLbl);
		
		JLabel toLbl = new JLabel("Closed To");
		toLbl.setFont(new Font(toLbl.getFont().toString(), Font.PLAIN, 14));
		toLbl.setBounds(12, 107, 144, 15);
		add(toLbl);
		
		if (!intersection.getClosedFrom().equals(NULL_DATE)) {
	    	fromDate.setDate(intersection.getClosedFrom());
	    } else {
	    	fromDate.setDateToToday();
	    }
		fromDate.setBounds(150, 67, 174, 19);
		add(fromDate);
		
		toDate.setBounds(150, 105, 174, 19);
		add(toDate);
		
		isClosedIndefinitely = new JCheckBox("Closed Indefinitely");
		isClosedIndefinitely.setFont(new Font(isClosedIndefinitely.getFont().toString(), Font.PLAIN, 14));
		isClosedIndefinitely.setBounds(12, 157, 195, 23);
		add(isClosedIndefinitely);

		if (!intersection.getClosedTo().equals(NULL_DATE)) {
	    	toDate.setDate(intersection.getClosedTo());
	    } else {
	    	isClosedIndefinitely.setSelected(true);
	    }
		
		JButton saveBtn = new JButton("Save");
		saveBtn.addActionListener(e -> save());
		saveBtn.setBounds(309, 263, 117, 25);
		add(saveBtn);
		
		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(e -> navigateToPreviousPage());
		cancelBtn.setBounds(180, 263, 117, 25);
		add(cancelBtn);
		
	    initDefaults();
	    initListeners();
	}
	
	private void navigateToPreviousPage() {
		this.getAcmeUI().mapView();
	}
	
	private void save() {
		intersection.setClosedFrom(fromDate.getDate() == null ? NULL_DATE : fromDate.getDate());
		intersection.setClosedTo(toDate.getDate() == null ? NULL_DATE : toDate.getDate());
		intersection.setClosedIndefinitely(isClosedIndefinitely.isSelected());
		navigateToPreviousPage();
	}

	private void initDefaults() {
		toSettings.setVisibleTodayButton(true);
        toSettings.setFormatForDatesCommonEra("MM/dd/yy");
        toSettings.setFontInvalidDate(new Font(toDate.getFont().toString(), Font.PLAIN, 16));
        toSettings.setVisibleClearButton(false);
        
        fromSettings.setVisibleTodayButton(true);
        fromSettings.setFormatForDatesCommonEra("MM/dd/yy");
        fromSettings.setVisibleClearButton(false);
        fromDate.setSettings(fromSettings);
        toDate.setSettings(toSettings);
        fromSettings.setDateRangeLimits(null, null);
        toSettings.setDateRangeLimits(fromDate.getDate().plusDays(1), null);
	}
	
    private void initListeners() {

        // date range
        DateChangeListener dateListen = new DateChangeListener() {
            LocalDate from = fromDate.getDate();

            @Override
            public void dateChanged(DateChangeEvent e) {
                toSettings.setDateRangeLimits(from.plusDays(1), null);
                if (toDate.getDate() != null && toDate.getDate().isBefore(from)) {
                    toDate.setDate(from.plusDays(1));
                }
            }
        };
        toDate.addDateChangeListener(dateListen);
        fromDate.addDateChangeListener(dateListen);
        
        // closed indefinitely
        toDate.addDateChangeListener(e -> {
        	if (toDate.getDate() != null)
        		isClosedIndefinitely.setSelected(false);
        });
        
        isClosedIndefinitely.addActionListener(e -> {
        	if (isClosedIndefinitely.isSelected())
        		toDate.setDate(null);
        });
    }
}	

