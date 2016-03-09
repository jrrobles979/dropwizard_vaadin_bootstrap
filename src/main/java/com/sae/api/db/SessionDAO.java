package com.sae.api.db;

import com.sae.api.core.Session;
import com.sae.api.services.SAEUtil;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.Optional;

/**
 * Created by ralmeida on 2/19/16.
 */
public class SessionDAO extends AbstractDAO<Session> {

    private SessionFactory sessionFactory;

    public SessionDAO(SessionFactory sessionFactory){
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public Optional<Session> find(String token){
        org.hibernate.Session hibernateSession = sessionFactory.openSession();

        Criteria criteria = hibernateSession.createCriteria(Session.class);
        criteria.add(Restrictions.eq("token", token));
        Optional<Session> result = Optional.ofNullable(uniqueResult(criteria));

        if (result.isPresent()){
            Session currentSession = result.get();
            currentSession.setUpdated_at(SAEUtil.getNow());
            hibernateSession.persist(currentSession);
        }

        hibernateSession.close();

        return result;
    }
}
