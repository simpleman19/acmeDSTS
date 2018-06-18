package acme.data;
import java.util.List;
import java.util.Map;

import acme.data.HibernateAdapter;
public interface PersistableEntity {
	
	public Object getId();
	
	default public void create() {
		HibernateAdapter.create(this);
	}
	
	default public <T> T update() {
		return (T) HibernateAdapter.update(this);
	}

	default public void delete() {
		HibernateAdapter.delete(this);
	}
	
	public static <K, T> T get(Class<T> entityClass, K id) {
		return (T) HibernateAdapter.get(entityClass, id);
	}

	// for example of how to use sql parameters, see here: http://docs.jboss.org/hibernate/orm/5.3/userguide/html_single/Hibernate_User_Guide.html#spatial-types-query-example
    public static <T> T querySingle(Class<T> entityClass, String sql, Map<String, String> sqlParameters) {
    	return HibernateAdapter.querySingle(entityClass, sql, sqlParameters);
    }
    
    public static <T> List<T> queryList(Class<T> entityClass, String sql, Map<String, String> sqlParameters) {
    	return HibernateAdapter.queryList(entityClass, sql, sqlParameters);
    }
}
