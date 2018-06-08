package acme.data;
import acme.data.HibernateAdapter;
public interface PersistableEntity {
	default public void save() {
		HibernateAdapter.save(this);
	}
	
	default public void delete() {
		HibernateAdapter.delete(this);
	}
	
	public static <K, T> T get(Class<T> entityClass, K id) {
		return (T) HibernateAdapter.get(entityClass, id);
	}
}
