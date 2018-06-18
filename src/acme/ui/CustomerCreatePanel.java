package acme.ui;

import java.util.Collections;

import javax.persistence.NoResultException;

import acme.data.PersistableEntity;
import acme.pd.Company;
import acme.pd.User;

public class CustomerCreatePanel extends AcmeBaseJPanel {
	Company c;

	public CustomerCreatePanel() {
        super();
    }
	
	@Override
    public void buildPanel() {
        c = this.getCompany();
        this.removeAll();
        
        this.revalidate();
        this.repaint();
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