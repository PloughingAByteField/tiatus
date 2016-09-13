package org.tiatus.dao;

import mockit.Mock;
import mockit.MockUp;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Role;
import org.tiatus.entity.User;
import org.tiatus.entity.UserRole;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.transaction.NotSupportedException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by johnreynolds on 19/06/2016.
 */
public class UserDaoIT {

    private static final Logger LOG = LoggerFactory.getLogger(UserDaoIT.class);
    private UserDaoImpl dao;
    private EntityManager em;
    private Role adminRole;
    private Role adjudicatorRole;

    @Before
    public void setUp() throws Exception {
        dao = new UserDaoImpl();
        em = Persistence.createEntityManagerFactory("primary").createEntityManager();
        dao.em = em;
        populateRoles();
    }

    @After
    public void tearDown() throws Exception {
        em.close();
    }

    @Test
    public void testHasAdminUser() {
        boolean hasAdminUser = dao.hasAdminUser();
        Assert.assertFalse(hasAdminUser);

        // add user
        em.getTransaction().begin();
        User user = createUser(adminRole, null, null);
        em.persist(user);
        em.getTransaction().commit();

        hasAdminUser = dao.hasAdminUser();
        Assert.assertTrue(hasAdminUser);
    }

    @Test
    public void testAddUser() throws Exception {
        User user = createUser(adminRole, "user", "pass");
        dao.tx = new EntityUserTransaction(em);
        dao.addUser(user);
    }

    @Test (expected = DaoException.class)
    public void testAddUserExistingUser() throws Exception {
        User user = createUser(adminRole, "user", "pass");
        dao.tx = new EntityUserTransaction(em);
        dao.addUser(user);
        dao.addUser(user);
    }

    @Test
    public void testGetRole() {
        Role role = dao.getRoleForRole(adminRole.getRoleName());
        Assert.assertNotNull(role);
        Assert.assertEquals(role.getRoleName(), adminRole.getRoleName());
    }

    @Test
    public void testGetRoleInvalid() {
        Role role = dao.getRoleForRole("BAD");
        Assert.assertNull(role);
    }

    @Test
    public void testGetUser() throws Exception {
        User user = createUser(adminRole, "user", "pass");
        dao.tx = new EntityUserTransaction(em);
        dao.addUser(user);

        User fetchedUser = dao.getUser("user", "pass");
        Assert.assertNotNull(fetchedUser);
        Assert.assertEquals(fetchedUser.getUserName(), user.getUserName());
    }

    @Test
    public void testGetUserNotFound() throws Exception {
        User user = createUser(adminRole, "user", "pass");
        dao.tx = new EntityUserTransaction(em);
        dao.addUser(user);

        User fetchedUser = dao.getUser("bad", "pass");
        Assert.assertNull(fetchedUser);
    }

    @Test (expected = DaoException.class)
    public void testDaoExceptionWithException() throws Exception {
        User user = new User();
        user.setId(new Long(1));
        EntityManager em = new MockUp<EntityManager>(){
            @Mock
            public <T> T find(Class<T> entityClass, Object primaryKey) throws NotSupportedException {
                throw new NotSupportedException();
            }
        }.getMockInstance();
        dao.em = em;

        dao.addUser(user);
    }

    private User createUser(Role role, String username, String password) {
        User user = new User();
        user.setUserName(username);
        user.setPassword(password);
        UserRole userRole = new UserRole();
        userRole.setRole(role);
        userRole.setUser(user);
        Set<UserRole> userRoleList = new HashSet<>();
        userRoleList.add(userRole);
        user.setRoles(userRoleList);
        return user;
    }

    private void populateRoles() {
        adminRole = persistRole("ADMIN");
        adjudicatorRole = persistRole("ADJUDICATOR");
    }

    private Role persistRole(String roleName) {
        em.getTransaction().begin();
        Role role = new Role();
        role.setRoleName(roleName);
        em.persist(role);
        em.getTransaction().commit();
        return role;
    }
}
