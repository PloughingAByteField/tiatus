package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.dao.UserDao;

import javax.inject.Inject;

/**
 * Created by johnreynolds on 02/09/2016.
 */
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserDao dao = null;

    @Inject
    public UserServiceImpl(UserDao dao) {
        this.dao = dao;
    }

    @Override
    public boolean hasAdminUser() {
        return dao.hasAdminUser();
    }
}
