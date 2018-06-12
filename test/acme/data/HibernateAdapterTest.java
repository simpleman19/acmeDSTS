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
	public void testSave() {
        Company company = new Company();
        company.setName("ACME");
        company.save();
        assertNotNull(company);
        assertNotNull(company.getId());
	}
	
	@Test
	public void testGetWithSavedId() {
        Company company = new Company();
        company.setName("ACME");
        company.save();
        Company company2 = PersistableEntity.get(Company.class, company.getId());
        assertNotNull(company2);
        assertEquals(company.getId(), company2.getId());
        assertEquals("ACME", company2.getName());
	}
	
	@Test
	public void testUpdate() {
        Company company = new Company();
        company.setName("ACME");
        company.save();
        company = PersistableEntity.get(Company.class, company.getId());
        assertNotNull(company);
        company.setName("Rebranded ACME");
        company.save();
        company.save();
        Company company2 = PersistableEntity.get(Company.class, company.getId());
        assertEquals("Rebranded ACME", company2.getName());
	}
	
	@Test
	public void testDelete() {
        Company company = new Company();
        company.setName("ACME");
        company.save();
        UUID companyId = company.getId();
        company = PersistableEntity.get(Company.class, companyId);
        assertNotNull(company);
        company.delete();
        company = PersistableEntity.get(Company.class, companyId);
        assertNull(company);
	}
}
