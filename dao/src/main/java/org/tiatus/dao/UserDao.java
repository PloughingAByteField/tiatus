package org.tiatus.dao;

import org.tiatus.entity.Role;
import org.tiatus.entity.User;
import org.tiatus.entity.UserRole;

import java.util.List;

/**
 * Created by johnreynolds on 02/09/2016.
 */
public interface UserDao {

    /**
     * Check to see if an admin user has been created.
     * @return a <code>boolean</code> if system has had an admin user created.
     */
    boolean hasAdminUser();

    /**
     * Add a user to the system
     * @param user to add
     * @throws DaoException on error
     */
    User addUser(User user) throws DaoException;

    /**
     * Remove a user
     * @param user to remove
     * @throws DaoException on error
     */
    void deleteUser(User user) throws DaoException;

    /**
     * Update a user
     * @param user to update
     * @throws DaoException on error
     */
    void updateUser(User user) throws DaoException;

    /**
     * Get the role for string identifier of the role.
     * @param role identifier of role
     * @return Role or null
     */
    Role getRoleForRole(String role);

    /**
     * Get user for supplied username and password
     * @param userName supplied username
     * @param password supplied password
     * @return User or null
     */
    User getUser(String userName, String password);

    /**
     * Get users
     * @return list of users
     */
    List<User> getUsers() throws DaoException;

    /**
     * Get user roles
     * @return list of user roles
     */
    List<Role> getUserRoles() throws DaoException;

    User getUserForId(Long id);
}
