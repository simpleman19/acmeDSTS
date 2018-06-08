package acme.data;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

import acme.pd.Company;

public class HibernateAdapter {	
	@PersistenceUnit
	protected static EntityManagerFactory entityManagerFactory;
	private static void doWithEntityManager(Consumer<EntityManager> lambda) {
		HashMap<String, Object> hints = new HashMap<>();
		hints.put("javax.persistence.query.timeout", 1);
		hints.put("javax.persistence.lock.timeout", 1);
		hints.put("org.hibernate.timeout", 1);
		if (entityManagerFactory == null)
			entityManagerFactory = Persistence.createEntityManagerFactory("my-pu", hints);
		
		EntityManager em = entityManagerFactory.createEntityManager(hints);
		lambda.accept(em);
		em.close();
	}

	private static void transaction(Consumer<EntityManager> lambda) {
		doWithEntityManager((em) -> {
			EntityTransaction transaction = em.getTransaction();
			transaction.begin();
			lambda.accept(em);
			transaction.commit();
		});
	}
	
	public static void save(Object entity) {
		transaction((em) -> em.persist(entity));
		//doWithEntityManager((em) -> em.persist(entity));
	}
	
	public static void delete(Object entity) {
		transaction((em) -> em.remove(entity));
	}
	
	public static <T> T get(Class<T> classOfEntity, Object id) {
		AtomicReference<T> object = new AtomicReference<T>(null);
		doWithEntityManager((em) -> object.set((T) em.find(classOfEntity, id)));
		return object.get();
	}
}
