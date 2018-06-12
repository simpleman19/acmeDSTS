package acme.ui;

import acme.data.HibernateAdapter;
import acme.data.PersistableEntity;
import acme.pd.Company;
import acme.pd.Courier;

import acme.pd.Direction;
import acme.pd.MapIntersection;
import acme.pd.Road;

public class Main {
    public static void main(String[] args) {
    	HibernateAdapter.startUp();
        Company company = new Company();
        company.setName("The coolest delivery company");
        Courier courier = new Courier();
        courier.setName("Bill");
        company.save();
        Company company2 = PersistableEntity.get(Company.class, company.getId());
        System.out.println(company2.getId() == company.getId());
        courier.save();
        HibernateAdapter.shutDown();
    	new UserUI();
    	new CompanyUI();
    }
    
}
