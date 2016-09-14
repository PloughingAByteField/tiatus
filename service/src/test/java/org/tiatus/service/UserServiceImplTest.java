package org.tiatus.service;

import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Test;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.UserDaoImpl;
import org.tiatus.entity.Role;
import org.tiatus.entity.User;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class UserServiceImplTest {
    @Test
    public void testConstructor() {
        UserServiceImpl service = new UserServiceImpl(new UserDaoImpl());
    }

    @Test
    public void testHasAdminUser() {
        new MockUp<UserDaoImpl>() {
            @Mock
            public boolean hasAdminUser() {
                return true;
            }
        };
        UserServiceImpl service = new UserServiceImpl(new UserDaoImpl());
        Assert.assertTrue(service.hasAdminUser());
    }

    @Test
    public void testGetUser() {
        new MockUp<UserDaoImpl>() {
            @Mock
            public User getUser(String userName, String password) {
                User user = new User();
                user.setUserName(userName);
                user.setPassword(password);
                return user;
            }
        };
        UserServiceImpl service = new UserServiceImpl(new UserDaoImpl());
        User user = service.getUser("username", "password");
        Assert.assertEquals(user.getUserName(), "username");
        Assert.assertEquals(user.getPassword(), "password");
    }

    @Test
    public void testAddUser() throws Exception {
        new MockUp<UserDaoImpl>() {
            @Mock
            public void addUser(User user) throws DaoException {
            }

            @Mock
            public Role getRoleForRole(String role) {
                return new Role();
            }
        };
        UserServiceImpl service = new UserServiceImpl(new UserDaoImpl());
        service.addUser(new User());
    }

    @Test (expected = ServiceException.class)
    public void testAddUserException() throws Exception {
        new MockUp<UserDaoImpl>() {
            @Mock
            public void addUser(User user) throws DaoException {
                throw new DaoException("message");
            }

            @Mock
            public Role getRoleForRole(String role) {
                return new Role();
            }
        };
        UserServiceImpl service = new UserServiceImpl(new UserDaoImpl());
        service.addUser(new User());
    }
}
