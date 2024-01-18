package org.tiatus.service;

import org.junit.Assert;
import org.junit.Test;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.UserDaoImpl;
import org.tiatus.entity.Role;
import org.tiatus.entity.User;

import javax.jms.JMSException;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class UserServiceImplTest {
    @Test
    public void testConstructor() {
        UserServiceImpl service = new UserServiceImpl(new UserDaoImpl(), new MessageSenderServiceImpl());
    }

    // @Test
    // public void testHasAdminUser() {
    //     new MockUp<UserDaoImpl>() {
    //         @Mock
    //         public boolean hasAdminUser() {
    //             return true;
    //         }
    //     };
    //     UserServiceImpl service = new UserServiceImpl(new UserDaoImpl(), new MessageSenderServiceImpl());
    //     Assert.assertTrue(service.hasAdminUser());
    // }

    // @Test
    // public void testGetUser() {
    //     new MockUp<UserDaoImpl>() {
    //         @Mock
    //         public User getUser(String userName, String password) {
    //             User user = new User();
    //             user.setUserName(userName);
    //             user.setPassword(password);
    //             return user;
    //         }
    //     };
    //     UserServiceImpl service = new UserServiceImpl(new UserDaoImpl(), new MessageSenderServiceImpl());
    //     User user = service.getUser("username", "password");
    //     Assert.assertEquals(user.getUserName(), "username");
    //     Assert.assertEquals(user.getPassword(), "password");
    // }

    // @Test
    // public void testAddAdminUser() throws Exception {
    //     new MockUp<UserDaoImpl>() {
    //         @Mock
    //         public boolean hasAdminUser() {
    //             return false;
    //         }

    //         @Mock
    //         public User addUser(User user) throws DaoException {
    //             return user;
    //         }

    //         @Mock
    //         public Role getRoleForRole(String role) {
    //             return new Role();
    //         }
    //     };
    //     new MockUp<MessageSenderServiceImpl>() {
    //         @Mock
    //         public void sendMessage(final Message obj) throws JMSException {}
    //     };
    //     UserServiceImpl service = new UserServiceImpl(new UserDaoImpl(), new MessageSenderServiceImpl());
    //     service.addAdminUser(new User(), "id");
    // }

    // @Test (expected = ServiceException.class)
    // public void testAddUserException() throws Exception {
    //     new MockUp<UserDaoImpl>() {
    //         @Mock
    //         public boolean hasAdminUser() {
    //             return false;
    //         }

    //         @Mock
    //         public User addUser(User user) throws DaoException {
    //             throw new DaoException("message");
    //         }

    //         @Mock
    //         public Role getRoleForRole(String role) {
    //             return new Role();
    //         }
    //     };
    //     UserServiceImpl service = new UserServiceImpl(new UserDaoImpl(), new MessageSenderServiceImpl());
    //     service.addAdminUser(new User(), "id");
    // }

    // @Test (expected = ServiceException.class)
    // public void testAddUserExistingUser() throws Exception {
    //     new MockUp<UserDaoImpl>() {
    //         @Mock
    //         public boolean hasAdminUser() {
    //             return true;
    //         }

    //         @Mock (invocations = 0)
    //         public User addUser(User user) throws DaoException {
    //             return user;
    //         }

    //         @Mock (invocations = 0)
    //         public Role getRoleForRole(String role) {
    //             return new Role();
    //         }
    //     };
    //     UserServiceImpl service = new UserServiceImpl(new UserDaoImpl(), new MessageSenderServiceImpl());
    //     service.addAdminUser(new User(), "id");
    // }
}
