package acme.seed;

import acme.data.HibernateAdapter;
import acme.pd.*;
import org.hibernate.Session;

import javax.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;

public class SeedDatabase {
    public static void main(String[] args) {
        HibernateAdapter.startUp();

        Scanner sc = new Scanner(System.in);
        String input = "";
        do {
            System.out.print("Do you wish to clear the current tables? (Y/N): ");
            input = sc.nextLine().toUpperCase();
            if (input.equals("Y")) {
                // TODO drop tables
                EntityManager em = HibernateAdapter.getEntityManager();
                Session sess = em.unwrap(Session.class);
                sess.createQuery("DROP SCHEMA public CASCADE; CREATE SCHEMA public; GRANT ALL ON SCHEMA public TO postgres; GRANT ALL ON SCHEMA public TO public;").executeUpdate();
                em.close();
                System.out.println("Dropped tables");
            } else {
                System.out.println("[WARN]: Not Dropping Tables");
            }
        } while (!input.equals("Y") && !input.equals("N"));


        Company company = new Company();
        company.setName("ACME Courier Service");
        company.setBonus(new BigDecimal(5.25));
        company.setBlockBillingRate(new BigDecimal(3.76));
        company.setBlocksPerMile(5.2);
        company.setCourierMilesPerHour(5.8);
        company.setFlatBillingRate(new BigDecimal(25.00));
        company.create();
        System.out.println("Added company");

        // admin account
        User user = new User();
        user.setUsername("admin");
        user.setPassword("password");
        user.setAdmin(true);
        // TODO save

        // clerk account
        user = new User();
        user.setUsername("clerk");
        user.setPassword("password");
        user.setAdmin(false);
        // TODO save
        System.out.println("Added admin and clerk accounts");

        // Two couriers
        Courier courier = new Courier();
        courier.setName("John Doe");
        //courier.setCourierNumber(1000);
        //courier.create();
        Courier courier2 = new Courier();
        courier2.setName("Jane Doe");
        //courier2.setCourierNumber(1001);
        //courier2.create();
        System.out.println("Added 2 couriers");

        // A couple customers
        Customer customer = new Customer();
        customer.setName("Company One");
        customer.setAvenueName("A");
        customer.setStreetName("1st");
        customer.setCustomerNumber(5000);

        customer.create();
        Customer customer2 = new Customer();
        customer2.setName("Company Two");
        customer2.setAvenueName("F");
        customer2.setStreetName("5th");
        customer2.create();
        System.out.println("Added 2 customers");

        // TODO path generation for tickets??
        // Open ticket
        Ticket ticket = new Ticket(company);
        ticket.setDeliveryCustomer(customer);
        ticket.setPickupCustomer(customer2);
        ticket.setDeliveryTime(LocalDateTime.now().plusHours(4).plusMinutes(45));
        ticket.setBillToSender(false);
        ticket.setNote("This is a note");
        ticket.create();

        // Open ticket with courier

        Ticket ticket2 = new Ticket(company);
        ticket.setDeliveryCustomer(customer2);
        ticket.setPickupCustomer(customer);
        ticket.setDeliveryTime(LocalDateTime.now().plusDays(2));
        ticket.setBillToSender(true);
        ticket.setNote("This is a note");
        ticket.setCourier(courier);
        ticket2.create();

        // Closed ticket
        Ticket ticket3 = new Ticket(company);
        ticket.setDeliveryCustomer(customer2);
        ticket.setPickupCustomer(customer);
        ticket.setDeliveryTime(LocalDateTime.now().minusHours(2));
        ticket.setBillToSender(true);
        ticket.setNote("This is a note");
        ticket.setCourier(courier);
        ticket.setDeliveryTime(LocalDateTime.now().minusHours(2));
        ticket.setPickupTime(LocalDateTime.now().minusHours(3));
        ticket.setDepartureTime(LocalDateTime.now().minusHours(4));
        ticket3.create();
        System.out.println("Added an open ticket, open with courier, and closed ticket");
        HibernateAdapter.shutDown();
    }


}
