package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.UserDao;
import org.tiatus.entity.Role;
import org.tiatus.entity.User;
import org.tiatus.entity.UserRole;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by johnreynolds on 02/09/2016.
 */
class UserServiceImpl implements UserService {

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

    @Override
    public void addUser(User user) throws ServiceException {
        LOG.debug("Adding admin user " + user.getUserName());
        try {
            UserRole userRole = new UserRole();
            // get role for admin from db
            Role role = dao.getRoleForRole(org.tiatus.role.Role.ADMIN);
            userRole.setRole(role);
            userRole.setUser(user);
            Set<UserRole> userRoleList = new HashSet<>();
            userRoleList.add(userRole);
            user.setRoles(userRoleList);
            dao.addUser(user);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public User getUser(String userName, String password) {
        return dao.getUser(userName, password);
    }


}
