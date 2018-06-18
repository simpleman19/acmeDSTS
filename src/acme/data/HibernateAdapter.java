package acme.data;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;

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

    // for example of how to use sql parameters, see here: http://docs.jboss.org/hibernate/orm/5.3/userguide/html_single/Hibernate_User_Guide.html#spatial-types-query-example
    private static <T> Object doWithQuery(Class<T> classOfEntity, String sql, Map<String, String> sqlParameters, Function<TypedQuery<T>, Object> lambda) {
    	return doWithEntityManager((em) -> {
    		TypedQuery<T> q = em.createQuery(sql, classOfEntity);
    		if (sqlParameters != null) {
	    		for (Map.Entry<String, String> entry : (sqlParameters.entrySet())) {
	    			q.setParameter(entry.getKey(), entry.getValue());
	    		}
    		}
    		return lambda.apply(q);
    	});
    }
    
    public static <T> T querySingle(Class<T> entityClass, String sql, Map<String, String> sqlParameters) {
    	return (T) doWithQuery(entityClass, sql, sqlParameters, q -> q.getSingleResult());
    }
    
    public static <T> List<T> queryList(Class<T> entityClass, String sql, Map<String, String> sqlParameters) {
    	return (List<T>) doWithQuery(entityClass, sql, sqlParameters, q -> q.getResultList());
    }
}
