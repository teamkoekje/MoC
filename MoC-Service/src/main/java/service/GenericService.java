package service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Abstract DAO used to manage data of a certain type
 *
 * @author Astrid
 * @param <T> the type of data that the DAO class should manage
 */
public class GenericService<T> {

    private final Class<T> entityClass;

    @PersistenceContext
    protected EntityManager em;

    /**
     * Creates a new DAO that manages data of a certain type
     *
     * @param entityClass the type of data that should be managed
     */
    protected GenericService(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Creates a new entity
     *
     * @param entity the entity that should be created
     */
    public void create(T entity) {
        em.persist(entity);
    }

    /**
     * Edits an existing entity
     *
     * @param entity the entity with the updated information
     */
    public void edit(T entity) {
        em.merge(entity);
    }

    /**
     * Removes an existing entity
     *
     * @param id id of the entity that should be removed
     */
    public void remove(Object id) {
        em.remove(findById(id));
    }

    /**
     * Finds an entity with a certain id
     *
     * @param id id of the entity
     * @return an entity object
     */
    public T findById(Object id) {
        return em.find(entityClass, id);
    }

    /**
     * Finds all exisiting entities
     *
     * @return list with all entities
     */
    public List<T> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return em.createQuery(cq).getResultList();
    }
}
