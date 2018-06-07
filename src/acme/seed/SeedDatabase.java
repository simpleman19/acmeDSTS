package acme.seed;

import acme.pd.*;

import java.math.BigDecimal;

public class SeedDatabase {
    public static void main(String[] args) {
        Company company = new Company();
        company.setName("ACME Courier Service");
        company.setBonus(new BigDecimal(5.25));
        company.setBlockBillingRate(new BigDecimal(3.76));
        company.setBlocksPerMile(5.2);
        company.setCourierMilesPerHour(5.8);
        company.setFlatBillingRate(new BigDecimal(25.00));
        // TODO save

        User user = new User();
        user.setUsername("admin");
        user.setPassword("password");
        user.setAdmin(true);
        // TODO save
        user = new User();
        user.setUsername("clerk");
        user.setPassword("password");
        user.setAdmin(false);
        // TODO save

        Courier courier = new Courier();
        courier.setName("John Doe");
        courier.setCourierNumber(1000);
        // TODO save
        courier = new Courier();
        courier.setName("Jane Doe");
        courier.setCourierNumber(1001);
        // TODO save

        Customer customer = new Customer();
        customer.setName("Company One");
        customer.setAvenueName("A");
        customer.setStreetName("1st");
        customer.setCustomerNumber(5000);
        // TODO save
        customer = new Customer();
        customer.setName("Company Two");
        customer.setAvenueName("F");
        customer.setStreetName("5th");
        // TODO save

        Ticket ticket = new Ticket();
        ticket.setCompany(company);
        ticket.setDeliveryCustomer(customer);

    }


}
