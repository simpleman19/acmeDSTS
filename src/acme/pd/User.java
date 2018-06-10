package acme.pd;

// Creates the class and extends methods
// from the Person class for User to use.

public class User extends Person {
	
	// Create Class attributes to store
	// username, password and isAdmin
	// datatypes. 
	
    private String username;
    private String password;
    private boolean isAdmin;

    // Constructor for the User object.
    
    public User () 
    {
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
    
    public boolean authenticate(String u, String p) {
    	
    	 // Checking to see if username and password match 
    	
    	 if ( 
    		 (this.getUsername().equalsIgnoreCase(u)) && 
    	     (this.getPassword() == p)
    	     ) {
    		 
    		 System.out.println("Login Authenticated. Access Granted.");
    		 return true;
    		 
    	 	}
    	 else {
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









