package com.sae.event.db;

/**
 * Created by ralmeida on 10/16/15.
 */

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class HibernateServiceDAO implements ServiceDAO {
    private SessionFactory sessionFactory;

    public HibernateServiceDAO(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public <T> T create(T t) {
        Session session = getSession();
        session.persist(t);
        session.flush();
        session.refresh(t);
        return t;
    }

    public <T, PK extends Serializable> T find(Class<T> type, PK id) {
        return (T) getSession().get(type, id);
    }

    public <T> T update(T type) {
        getSession().merge(type);
        return type;
    }

    public <T, PK extends Serializable> void delete(Class<T> type, PK id) {
        Session session = getSession();

        @SuppressWarnings("unchecked")
        T ref = (T) session.get(type, id);
        session.delete(ref);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findAll(Class<T> type) {
        Criteria c = getSession().createCriteria(type).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return (List<T>) c.list();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findWithNamedQuery(String queryName) {
        return getSession().getNamedQuery(queryName).list();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findWithNamedQuery(String queryName,
                                          Map<String, Object> params) {
        Set<Entry<String, Object>> rawParameters = params.entrySet();
        Query query = getSession().getNamedQuery(queryName);

        for (Entry<String, Object> entry : rawParameters) {

            if (entry.getValue() instanceof Collection<?>){
                query.setParameterList(entry.getKey(), (Collection<?>)entry.getValue());

            } else {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public <T> T findUniqueWithNamedQuery(String queryName) {
        return (T) getSession().getNamedQuery(queryName).uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public int updateWithNamedQuery(String queryName,
                                      Map<String, Object> params) {

        Set<Entry<String, Object>> rawParameters = params.entrySet();
        Query query = getSession().getNamedQuery(queryName);

        for (Entry<String, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());

        }
        return query.executeUpdate();
    }


    @SuppressWarnings("unchecked")
    public <T> T findUniqueWithNamedQuery(String queryName,
                                          Map<String, Object> params) {
        Set<Entry<String, Object>> rawParameters = params.entrySet();
        Query query = getSession().getNamedQuery(queryName);

        for (Entry<String, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());

        }
        return (T) query.uniqueResult();
    }

    public <T> int count(Class<T> clazz) {
        Criteria criteria = getSession().createCriteria(clazz);
        Long count = (Long) criteria.setProjection(Projections.rowCount())
                .uniqueResult();
        return count.intValue();
    }
}