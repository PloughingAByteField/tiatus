package org.tiatus.service;

import org.tiatus.entity.Role;
import org.tiatus.entity.User;

import java.util.List;

/**
 * Created by johnreynolds on 02/09/2016.
 */
public interface UserService {
    /**
     * Check to see if an admin user has been created.
     * @return a <code>boolean</code> if system has had an admin user created.
     */
    boolean hasAdminUser();

    /**
     * Add an admin user to the system
     * @param user to add
     * @throws ServiceException on error
     */
    User addAdminUser(User user, String sessionId) throws ServiceException;

    /**
     * Add a user to the system
     * @param user to add
     * @throws ServiceException on error
     */
    User addUser(User user, String sessionId) throws ServiceException;

    /**
     * Remove a user
     * @param user to remove
     * @throws ServiceException on error
     */
    void deleteUser(User user, String sessionId) throws ServiceException;

    /**
     * update user
     * @param user to update
     * @throws ServiceException on error
     */
    User updateUser(User user, String sessionId) throws ServiceException;

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
    List<User> getUsers() throws ServiceException;

    /**
     * Get user roles
     * @return list of user roles
     */
    List<Role> getUserRoles() throws ServiceException;

    User getUserForId(Long id);
}
