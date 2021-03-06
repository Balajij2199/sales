package pss1.services;

import static pss1.domain.QUser.*;

import org.apache.tapestry5.ioc.annotations.Inject;

import com.mysema.rdfbean.object.SessionFactory;

import pss1.domain.User;

public class UserDAOImpl extends AbstractDAO<User> implements UserDAO {

    public UserDAOImpl(@Inject SessionFactory sessionFactory) {
        super(sessionFactory, user);
    }

    @Override
    public User getByUsername(String username) {
        return getSession().from(user).where(user.username.eq(username)).uniqueResult(user);
    }

}
