package acme.ui;

import acme.data.PersistableEntity;
import acme.pd.Company;
import acme.pd.Courier;


public class Main {
    public static void main(String[] args) {
        Company company = new Company();
        company.setName("The coolest delivery company");
        Courier courier = new Courier();
        courier.setName("Bill");
        company.save();
        Company company2 = PersistableEntity.get(Company.class, company.getId());
        System.out.println(company2.getId() == company.getId());
        courier.save();
    }
}
