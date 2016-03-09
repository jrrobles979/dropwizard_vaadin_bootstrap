package com.sae.api.db;

/**
 * Created by ralmeida on 10/16/15.
 */
import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * CrudServiceDAO interface.
 *
 * @author karesti
 */
public interface ManagedServiceDAO {

    void openSession();
    void closeSession();

    /**
     * Creates a new object for the given type. After a call to this method the
     * entity will be persisted into database and then refreshed. Also current
     * persistent Session will be flushed.
     *
     * @param <T>
     * @param t
     * @return persisted Object
     */
    <T> T create(T t);

    /**
     * Updates the given object
     *
     * @param <T>
     * @param t
     * @return persisted object
     */
    <T> T update(T t);

    /**
     * Updates the given object
     *
     * @param queryName
     * @param params
     * @return  record count
     */
    int updateWithNamedQuery(String queryName, Map<String, Object> params);

    /**
     * Deletes the given object by id
     *
     * @param <T>
     * @param <PK>
     * @param type
     *            , entity class type
     * @param id
     */
    <T, PK extends Serializable> void delete(Class<T> type, PK id);

    /**
     * Loads all entities in a table
     * @param
     * @return
     */
    <T> List<T> findAll(Class<T> type);

    /**
     * Finds an object by id
     *
     * @param <T>
     * @param <PK>
     * @param type
     * @param id
     * @return the object
     */
    <T, PK extends Serializable> T find(Class<T> type, PK id);

    /**
     * Finds a list of objects for the given query name
     *
     * @param <T>
     * @param queryName
     * @return returns a list of objects
     */
    <T> List<T> findWithNamedQuery(String queryName);

    /**
     * Find a query with parameters
     *
     * @param <T>
     * @param queryName
     * @param params
     * @return resulting list
     */
    <T> List<T> findWithNamedQuery(String queryName, Map<String, Object> params);

    /**
     * Returns one result, query without parameters
     *
     * @param <T>
     * @param queryName
     * @return T object
     */
    <T> T findUniqueWithNamedQuery(String queryName);

    /**
     * Returns just one result with a named query and parameters
     *
     * @param <T>
     * @param queryName
     * @param params
     * @return T object
     */
    <T> T findUniqueWithNamedQuery(String queryName, Map<String, Object> params);

    /**
     * Returns the number of records in a table
     *
     * @param clazz
     * @return int the number of records
     */
    <T> int count(Class<T> clazz);
}