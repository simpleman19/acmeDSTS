package acme.data;
import acme.data.HibernateAdapter;
public interface PersistableEntity {

    public <K> K getId();

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
}
