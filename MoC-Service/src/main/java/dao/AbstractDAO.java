package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Abstract DAO used to manage data of a certain type
 *
 * @author Astrid
 * @param <T> the type of data that the DAO class should manage
 */
public abstract class AbstractDAO<T> {

    private final Class<T> entityClass;

    /**
     * Creates a new DAO that manages data of a certain type
     *
     * @param entityClass the type of data that should be managed
     */
    public AbstractDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Gets the entitymanager used in the DAO class
     *
     * @return EntitityManager
     */
    protected abstract EntityManager getEntityManager();

    /**
     * Creates a new entity
     *
     * @param entity the entity that should be created
     */
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    /**
     * Edits an existing entity
     *
     * @param entity the entity with the updated information
     */
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    /**
     * Removes an existing entity
     *
     * @param entity the entity that should be removed
     */
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    /**
     * Finds an entity with a certain id
     *
     * @param id id of the entity
     * @return an entity object
     */
    public T findById(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Finds all exisiting entities
     *
     * @return list with all entities
     */
    public List<T> findAll() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }
}
