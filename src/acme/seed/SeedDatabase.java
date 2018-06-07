package acme.seed;

import acme.pd.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
        Customer customer2 = new Customer();
        customer.setName("Company Two");
        customer.setAvenueName("F");
        customer.setStreetName("5th");
        // TODO save

        Ticket ticket = new Ticket();
        ticket.setCompany(company);
        ticket.setDeliveryCustomer(customer);
        ticket.setPickupCustomer(customer2);
        ticket.setDeliveryTime(LocalDateTime.now().plusHours(4).plusMinutes(45));
        ticket.setCreationDateTime(LocalDateTime.now());
        ticket.setBillToSender(false);
        ticket.setNote("This is a note");
        ticket.setClerk(user);
        // TODO save

        Ticket ticket2 = new Ticket();
        ticket.setCompany(company);
        ticket.setDeliveryCustomer(customer2);
        ticket.setPickupCustomer(customer);
        ticket.setDeliveryTime(LocalDateTime.now().plusDays(2));
        ticket.setCreationDateTime(LocalDateTime.now());
        ticket.setBillToSender(true);
        ticket.setNote("This is a note");
        ticket.setClerk(user);
        // TODO save

        Ticket ticket3 = new Ticket();
        ticket.setCompany(company);
        ticket.setDeliveryCustomer(customer2);
        ticket.setPickupCustomer(customer);
        ticket.setDeliveryTime(LocalDateTime.now().minusHours(2));
        ticket.setCreationDateTime(LocalDateTime.now());
        ticket.setBillToSender(true);
        ticket.setNote("This is a note");
        ticket.setClerk(user);
        ticket.setCourier(courier);
        ticket.setDeliveryTime(LocalDateTime.now().minusHours(2));
        ticket.setPickupTime(LocalDateTime.now().minusHours(3));
        ticket.setDepartureTime(LocalDateTime.now().minusHours(4));
        // TODO save


    }


}
