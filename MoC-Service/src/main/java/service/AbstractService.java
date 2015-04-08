package service;

import dao.AbstractDAO;
import java.util.List;

/**
 * Abstract service used to manage data of a certain type
 *
 * @author Astrid
 * @param <T> the type that the service class should manage
 */
public abstract class AbstractService<T> {

    /**
     * Get the DAO used in the service to manage data
     *
     * @return AbstractDAO object
     */
    protected abstract AbstractDAO getDAO();

    /**
     * Creates a new entity
     *
     * @param entity the entity that should be created
     */
    public void create(T entity) {
        getDAO().create(entity);
    }

    /**
     * Edits an existing entity
     *
     * @param entity the entity with the updated information
     */
    public void edit(T entity) {
        getDAO().edit(entity);
    }

    /**
     * Removes an existing entity
     *
     * @param id id of the entity that should be removed
     */
    public void remove(Object id) {
        Object e = getDAO().findById(id);
        if (e != null) {
            getDAO().remove(e);
        }
    }

    /**
     * Finds an entity with a certain id
     *
     * @param id id of the entity
     * @return an entity object
     */
    public T findById(Object id) {
        return (T) getDAO().findById(id);
    }

    /**
     * Finds all exisiting entities
     *
     * @return list with all entities
     */
    public List<T> findAll() {
        return (List<T>) getDAO().findAll();
    }
}
