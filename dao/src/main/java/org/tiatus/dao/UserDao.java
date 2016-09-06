package org.tiatus.dao;

import org.tiatus.entity.Role;
import org.tiatus.entity.User;

/**
 * Created by johnreynolds on 02/09/2016.
 */
public interface UserDao {
    boolean hasAdminUser();
    void addUser(User user) throws DaoException;
    Role getRoleForRole(String role);
    public User getUser(String userName, String password);
}
