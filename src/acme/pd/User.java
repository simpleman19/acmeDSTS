package acme.pd;

import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NoResultException;

import acme.data.PersistableEntity;

// Creates the class and extends methods
// from the Person class for User to use.
@Entity(name = "APP_USER")
public class User extends Person implements PersistableEntity {
	
	// Create Class attributes to store
	// username, password and isAdmin
	// datatypes. 
	@Column(name = "USERNAME")
    private String username;
	@Column(name = "PASSWORD")
    private String password;
	@Column(name = "IS_ADMIN")
    private boolean isAdmin;

    // Constructor for the User object.
    
    public User () 
    {
        super();
    	// IMPLEMENT
    }
    
    // Returns the username.
    public String getUsername() {
        if(username == null) {
        	System.out.println("No username has been set for " + this.getName() + ".");
        }
        
    	return username;
    } 
    
    // Sets the username.
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
    
    public static boolean isAuthenticated(String u, String p) {
    	 // Checking to see if username and password match 
    	HashMap<String,String> parameters = new HashMap<String, String>();
    	parameters.put("username", u);
    	parameters.put("password", p);
    	try {
    		PersistableEntity.querySingle(User.class,
       			 "select u from APP_USER u where USERNAME = :username and PASSWORD = :password",
       			 parameters);
    		System.out.println("Login Authenticated. Access Granted.");
   		 	return true;
    	} catch(NoResultException e) {
    		System.out.println("Login Failed Authentication. Access Denied.");
    		return false;
    	}
    }
    
    

	public static void main(String []args) {   

    	//Creating the new user called Wolfgang
    	//and setting login attributes for Wolfgang.

    	User wolfgang = new User();
    	wolfgang.setAdmin(true);
    	wolfgang.setUsername("wolf");
    	wolfgang.setPassword("password");
    	wolfgang.setName("Wolfgang Morton");
    	wolfgang.setActive(true);

    } 
}









