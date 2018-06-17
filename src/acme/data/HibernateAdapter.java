package acme.data;

import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

public class HibernateAdapter {	
    @PersistenceUnit
    protected static EntityManagerFactory entityManagerFactory;

    public static void startUp() {
        if (entityManagerFactory == null)
            entityManagerFactory = Persistence.createEntityManagerFactory("my-pu");
    }

    public static void shutDown() {
        if (entityManagerFactory != null)
            entityManagerFactory.close();
    }

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    private static <T> T doWithEntityManager(Function<EntityManager, T> lambda) {
        EntityManager em = entityManagerFactory.createEntityManager();
        T returnVal = lambda.apply(em);
        em.close();
        return returnVal;
    }

    private static <T> T transaction(Function<EntityManager, T> lambda) {
        return doWithEntityManager((em) -> {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            T returnVal = lambda.apply(em);
            transaction.commit();
            return returnVal;
        });
    }

    public static void create(Object entity) {
        transaction((em) -> {
            em.persist(entity);
            return null;
        });
    }

    public static Object update(Object entity) {
        return transaction((em) -> em.merge(entity));
    }

    public static void delete(Object entity) {
        transaction((em) -> {
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            return null;
        });
    }

    public static <T> T get(Class<T> classOfEntity, Object id) {
        return doWithEntityManager((em) -> (T) em.find(classOfEntity, id));
    }
}
