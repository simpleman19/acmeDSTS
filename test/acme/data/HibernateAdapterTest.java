package acme.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import acme.pd.Company;
import acme.pd.Courier;
import acme.pd.Customer;
import acme.pd.Ticket;
import acme.pd.User;

public class HibernateAdapterTest {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		HibernateAdapter.startUp();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		HibernateAdapter.shutDown();
	}

	@Test
	public void testCreate() {
        Company company = new Company();
        company.setName("ACME");
        company.create();
        company = company.update();
        assertNotNull(company);
        assertNotNull(company.getId());
	}
	
	@Test(expected = javax.persistence.PersistenceException.class)
	public void testMultipleCreates() {
        Company company = new Company();
        company.setName("ACME");
        company.create();
        company.create();
	}
	
	@Test
	public void testGetWithSavedId() {
        Company company = new Company();
        company.setName("ACME");
        company.create();
        Company company2 = PersistableEntity.get(Company.class, company.getId());
        assertNotNull(company2);
        assertEquals(company.getId(), company2.getId());
        assertEquals("ACME", company2.getName());
	}
	
	@Test
	public void testUpdate() {
        Company company = new Company();
        company.setName("ACME");
        company.create();
        Company company2 = PersistableEntity.get(Company.class, company.getId());
        assertNotNull(company);
        company2.setName("Rebranded ACME");
        company2.update();
        company2.update();
        Company company3 = PersistableEntity.get(Company.class, company2.getId());
        assertEquals("Rebranded ACME", company3.getName());
	}
	
	@Test
	public void testDelete() {
        Company company = new Company();
        company.setName("ACME");
        company = company.update();
        UUID companyId = company.getId();
        company = PersistableEntity.get(Company.class, companyId);
        assertNotNull(company);
        company.delete();
        company = PersistableEntity.get(Company.class, companyId);
        assertNull(company);
	}
	
	@Test
	public void testQuerySingleResult() {
		Company company = new Company();
        company.setName(UUID.randomUUID().toString());
        company.create();
        HashMap<String, String> parameter = new HashMap<String, String>();
        parameter.put("name", company.getName());
        PersistableEntity.querySingle(Company.class, "select c from COMPANY c where NAME = :name", parameter);
	}
	
	@Test(expected = javax.persistence.NoResultException.class)
	public void testQuerySingleNoResult() {
        HashMap<String, String> parameter = new HashMap<String, String>();
        parameter.put("name", UUID.randomUUID().toString());
        PersistableEntity.querySingle(Company.class, "select c from COMPANY c where NAME = :name", parameter);
	}
	
	@Test
	public void testQueryListResults() {
		Company company = new Company();
        company.setName(UUID.randomUUID().toString());
        company.create();
        Company company2 = new Company();
        company2.setName(company.getName());
        company2.create();
        HashMap<String, String> parameter = new HashMap<String, String>();
        parameter.put("name", company.getName());
        java.util.List<Company> companiesWithSameName = PersistableEntity.queryList(Company.class, "select c from COMPANY c where NAME = :name", parameter);
        assertEquals(2, companiesWithSameName.size());
        String a = company.getId().toString();
        String b = company2.getId().toString();
        String c = companiesWithSameName.get(0).getId().toString();
        String d = companiesWithSameName.get(1).getId().toString();
        assertTrue(a.equals(c) || a.equals(d));
        assertTrue(b.equals(c) || b.equalsIgnoreCase(d));
	}
	
	@Test
	public void testCourier() {
		Courier courier = new Courier();
		courier.setName("Mary");
		courier.create();
		courier = PersistableEntity.get(Courier.class, courier.getId());
		assertNotNull(courier);
		assertEquals("Mary", courier.getName());
		courier.setName("Sue");
		courier = courier.update();
		assertEquals("Sue", courier.getName());
		courier.delete();
		assertNull(PersistableEntity.get(Courier.class, courier.getId()));
	}

	@Test
	public void testCustomer() {
		Customer customer = new Customer();
		customer.setName("Bickers & Bickers");
		customer.create();
		customer = PersistableEntity.get(Customer.class, customer.getId());
		assertNotNull(customer);
		assertEquals("Bickers & Bickers", customer.getName());
		customer.setName("Only Bickers now");
		customer = customer.update();
		assertEquals("Only Bickers now", customer.getName());
		customer.delete();
		assertNull(PersistableEntity.get(Customer.class, customer.getId()));
	}
	
	@Test
	public void testTicket() {
		Ticket ticket = new Ticket(null);
		ticket.setNote("go to lobby");
		ticket.create();
		ticket = PersistableEntity.get(Ticket.class, ticket.getId());
		assertNotNull(ticket);
		assertEquals("go to lobby", ticket.getNote());
		ticket.setNote("go to 3rd floor");
		ticket = ticket.update();
		assertEquals("go to 3rd floor", ticket.getNote());
		ticket.delete();
		assertNull(PersistableEntity.get(Ticket.class, ticket.getId()));
	}
	
	@Test
	public void testUser() {
		User user = new User();
		user.setUsername("user");
		user.create();
		user = PersistableEntity.get(User.class, user.getId());
		assertNotNull(user);
		assertEquals("user", user.getUsername());
		user.setUsername("admin");
		user = user.update();
		assertEquals("admin", user.getUsername());
		user.delete();
		assertNull(PersistableEntity.get(User.class, user.getId()));
	}
	
	@Test
	public void testTicketAssociation() {
		Company company = new Company();
        company.setName("ACME");
        company.create();
        Customer customer = new Customer();
        customer.setName("Bickers & Bickers");
        customer.create();

        Ticket ticket = new Ticket(company);
        ticket.setDeliveryCustomer(customer);
        ticket.create();
        ticket.setCompany(company);
        company = company.update();

        assertEquals(company.getId(), ticket.getCompany().getId());
        assertEquals(customer.getId(), ticket.getDeliveryCustomer().getId());
	}
	
	@Test
	public void testBidirectionalAssociation() {
		Company company = new Company();
        company.setName("ACME");
        company.create();
        assertEquals(0, company.getCouriers().size());
        
        Courier courier = new Courier();
        courier.create();
        company.addCourier(courier);
        company = company.update();
        
        assertEquals(1, company.getCouriers().size());
        assertNotNull(company.getCouriers().get(courier.getId()));
	}
}
