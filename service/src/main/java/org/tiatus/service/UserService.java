package org.tiatus.service;

import org.tiatus.entity.User;

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
     * Add a user to the system
     * @param user to add
     * @throws ServiceException on error
     */
    void addUser(User user) throws ServiceException;

    /**
     * Get user for supplied username and password
     * @param userName supplied username
     * @param password supplied password
     * @return User or null
     */
    User getUser(String userName, String password);
}
