package acme.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import acme.pd.Company;

public class HibernateAdapterTest {
	@BeforeClass
	public static void setUp() throws Exception {
		HibernateAdapter.startUp();
	}

	@AfterClass
	public static void tearDown() throws Exception {
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
}
