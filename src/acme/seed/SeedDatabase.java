package acme.seed;

import acme.data.HibernateAdapter;
import acme.pd.*;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import javax.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;

public class SeedDatabase {
    public static void main(String[] args) {
        HibernateAdapter.startUpSeed();

        seedDB();

        HibernateAdapter.shutDown();
    }

    public static Company getDefaultAcme() {
        Company acme = new Company();

        acme.setName("Acme");

        return acme;
    }

    public static void seedDB() {
        Company company = Company.loadCompanyFromDB();
        boolean seed = true;
        if (company != null) {
            Scanner sc = new Scanner(System.in);
            String input = "";
            do {
                System.out.print("There is already a company in the database do you wish to clear the current company? (Y/N): ");
                input = sc.nextLine().toUpperCase();
                if (input.equalsIgnoreCase("Y")) {
                    System.out.println("Overwriting Company");
                    // Delete tickets
                    Iterator it = new HashMap<>(company.getTickets()).values().iterator();
                    while (it.hasNext()) {
                        Ticket ticket = (Ticket) ((HashMap.Entry) it.next()).getValue();
                        company.getTickets().remove(ticket.getId());
                        company.update();
                        ticket.delete();
                    }

                    // Delete Customers
                    it = new HashMap<>(company.getCustomers()).values().iterator();
                    while (it.hasNext()) {
                        Customer customer = (Customer)((HashMap.Entry) it.next()).getValue();
                        company.getCustomers().remove(customer.getId());
                        company.update();
                        customer.delete();
                    }

                    // Delete Couriers
                    it = new HashMap<>(company.getCouriers()).values().iterator();
                    while (it.hasNext()) {
                        Courier courier = (Courier) ((HashMap.Entry) it.next()).getValue();
                        company.getCouriers().remove(courier.getId());
                        company.update();
                        courier.delete();
                    }

                    // Delete Users
                    it = new HashMap<>(company.getUsers()).values().iterator();
                    while (it.hasNext()) {
                        User user = (User) ((HashMap.Entry) it.next()).getValue();
                        company.getUsers().remove(user.getId());
                        company.update();
                        user.delete();
                    }

                    System.out.println("Cleared out company, seeding...");
                } else if (input.equalsIgnoreCase("N")){
                    System.out.println("Skipping seed");
                    seed = false;
                }
            } while (!input.equals("Y") && !input.equals("N"));
        }

        if (company == null) {
            company = SeedDatabase.getDefaultAcme();
            company.create();
        }

        if (seed) {
            company.setName("ACME Courier Service");
            company.setBonus(new BigDecimal(5.25));
            company.setBlockBillingRate(new BigDecimal(3.76));
            company.setBlocksPerMile(5.2);
            company.setCourierMilesPerHour(5.8);
            company.setFlatBillingRate(new BigDecimal(25.00));
            company.setLatenessMarginMinutes(5);
            company.update();
            System.out.println("Added company");

            // admin account
            User user = new User();
            user.setUsername("admin");
            user.setPassword("password");
            user.setName("An Administrator");
            user.setAdmin(true);
            user.create();
            company.addUser(user);

            // clerk account
            user = new User();
            user.setUsername("clerk");
            user.setPassword("password");
            user.setName("A Clerk");
            user.setAdmin(false);
            user.create();
            company.addUser(user);
            company.update();
            System.out.println("Added admin and clerk accounts");

            company.setCurrentUser(user);

            // Two couriers
            Courier courier = new Courier();
            courier.setName("John Doe");
            courier.setCourierNumber(1000);
            courier.create();
            company.addCourier(courier);
            company.update();

            Courier courier2 = new Courier();
            courier2.setName("Jane Doe");
            courier2.setCourierNumber(1001);
            courier2.create();
            company.addCourier(courier2);
            company.update();
            System.out.println("Added 2 couriers");

            // A couple customers
            Customer customer = new Customer();
            customer.setName("Company One");
            customer.setAvenueName("A");
            customer.setStreetName("1");
            customer.setCustomerNumber(5000);
            customer.create();
            company.addCustomer(customer);
            company.update();

            Customer customer2 = new Customer();
            customer2.setName("Company Two");
            customer2.setAvenueName("F");
            customer2.setStreetName("5");
            customer2.setCustomerNumber(5001);
            customer2.create();
            company.addCustomer(customer2);
            company.update();
            System.out.println("Added 2 customers");

            // Open ticket
            Ticket ticket = new Ticket(company);
            ticket.setDeliveryCustomer(customer);
            ticket.setPickupCustomer(customer2);
            ticket.setEstimatedDeliveryTime(LocalDateTime.now().plusHours(4).plusMinutes(45));
            ticket.setBillToSender(false);
            ticket.setNote("This is a note");
            ticket.create();
            company.addTicket(ticket);
            company.update();
            // Open ticket with courier

            Ticket ticket2 = new Ticket(company);
            ticket2.setDeliveryCustomer(customer2);
            ticket2.setPickupCustomer(customer);
            ticket2.setEstimatedDeliveryTime(LocalDateTime.now().plusDays(2));
            ticket2.setBillToSender(true);
            ticket2.setNote("This is a note");
            ticket2.setCourier(courier);
            ticket2.create();
            company.addTicket(ticket2);
            company.update();

            // Closed ticket
            Ticket ticket3 = new Ticket(company);
            ticket3.setDeliveryCustomer(customer2);
            ticket3.setPickupCustomer(customer);
            ticket3.setEstimatedDeliveryTime(LocalDateTime.now().minusHours(2));
            ticket3.setBillToSender(true);
            ticket3.setNote("This is a note");
            ticket3.setCourier(courier);
            ticket3.setDeliveryTime(LocalDateTime.now().minusHours(2));
            ticket3.setPickupTime(LocalDateTime.now().minusHours(3));
            ticket3.setDepartureTime(LocalDateTime.now().minusHours(4));
            ticket3.create();
            company.addTicket(ticket3);
            company.update();
            System.out.println("Added an open ticket, open with courier, and closed ticket");
        }

        Scanner sc = new Scanner(System.in);
        String input = "";
        do {
            System.out.print("Do you want a ton of stuff? (Y/N): ");
            input = sc.nextLine().toUpperCase();
            if (input.equalsIgnoreCase("Y")) {
                for (int i = 1; i <= 5; i++) {
                    Courier courier = randomCourier(i);
                    courier.create();
                    company.addCourier(courier);
                    company.update();
                }
                for (int i = 1; i <= 20; i++) {
                    Customer cust = randomCust(i);
                    cust.create();
                    company.addCustomer(cust);
                    company.update();
                }
                for (int i = 0; i < 200; i++) {
                    Ticket randTicket = randomTicket(company);
                    randTicket.create();
                    company.addTicket(randTicket);
                    company.update();
                }
            } else if (input.equalsIgnoreCase("N")){
                System.out.println("Done");
            }
        } while (!input.equals("Y") && !input.equals("N"));
    }


    private static Courier randomCourier(int i){
        Courier courier = new Courier();
        courier.setName("John Doe " + i);
        courier.setCourierNumber(5000 + i);

        return courier;
    }
    private static Customer randomCust(int i) {
        Random rand = new Random();
        Customer customer = new Customer();
        String [] names = {
                "A", "B", "C", "D", "E", "F", "G"
        };
        String [] nums = {
                "1", "2", "3", "4", "5", "6", "7"
        };
        customer.setName("Company " + i);
        customer.setAvenueName(names[rand.nextInt(names.length)]);
        customer.setStreetName(nums[rand.nextInt(nums.length)]);
        customer.setCustomerNumber(10000 + i);
        return customer;
    }

    private static Ticket randomTicket(Company company) {
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        Customer customer = (Customer) company.getCustomers().values().toArray()[rand.nextInt(company.getCustomers().values().size())];
        Customer customer2 = null;
        do {
            customer2 = (Customer) company.getCustomers().values().toArray()[rand.nextInt(company.getCustomers().values().size())];
        } while (customer == customer2);
        Ticket ticket = new Ticket(company);
        ticket.setDeliveryCustomer(customer2);
        ticket.setPickupCustomer(customer);
        int courier = rand.nextInt();
        long randTime = rand.nextInt(60 * 24 * 20) - 60 * 24 * 10 ;
        ticket.setEstimatedDeliveryTime(LocalDateTime.now().plusMinutes(randTime));
        ticket.setCreationDateTime(ticket.getEstimatedDeliveryTime().minusHours(6));
        if (ticket.getEstimatedDeliveryTime().isBefore(LocalDateTime.now())) { // Closed
            ticket.setCourier((Courier) company.getCouriers().values().toArray()[rand.nextInt(company.getCouriers().values().size())]);
            ticket.setDeliveryTime(ticket.getEstimatedDeliveryTime().plusMinutes(rand.nextInt(40) - 30));
            ticket.setPickupTime(ticket.getEstimatedDeliveryTime().minusMinutes(30));
            ticket.setDepartureTime(ticket.getEstimatedDeliveryTime().minusMinutes(50));
        } else if (courier % 2 == 1) { // Created with courier
            ticket.setCourier((Courier) company.getCouriers().values().toArray()[rand.nextInt(company.getCouriers().values().size())]);
        }

        ticket.setBillToSender(rand.nextInt() % 2 == 0);
        ticket.setNote("This is a note");

        return ticket;
    }


}
