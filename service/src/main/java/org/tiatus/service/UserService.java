package org.tiatus.service;

import org.tiatus.entity.User;

/**
 * Created by johnreynolds on 02/09/2016.
 */
public interface UserService {
    boolean hasAdminUser();
    void addUser(User user) throws ServiceException;
    User getUser(String userName, String password);
}
