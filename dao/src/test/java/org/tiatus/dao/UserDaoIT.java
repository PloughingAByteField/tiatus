package org.tiatus.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Role;
import org.tiatus.entity.User;
import org.tiatus.entity.UserRole;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by johnreynolds on 19/06/2016.
 */
@ExtendWith(MockitoExtension.class)
public class UserDaoIT {

    // private static final Logger LOG = LoggerFactory.getLogger(UserDaoIT.class);
    // private UserDaoImpl dao;
    // private EntityManager em;
    // private Role adminRole;
    // private Role adjudicatorRole;

    // @BeforeEach
    // public void setUp() throws Exception {
    //     dao = new UserDaoImpl();
    //     em = Persistence.createEntityManagerFactory("primary").createEntityManager();
    //     dao.em = em;
    //     populateRoles();
    // }

    // @AfterEach
    // public void tearDown() throws Exception {
    //     em.close();
    // }

    // @Test
    // public void testHasAdminUser() {
    //     boolean hasAdminUser = dao.hasAdminUser();
    //     Assertions.assertFalse(hasAdminUser);

    //     // add user
    //     em.getTransaction().begin();
    //     User user = createUser(adminRole, null, null);
    //     em.persist(user);
    //     em.getTransaction().commit();

    //     hasAdminUser = dao.hasAdminUser();
    //     Assertions.assertTrue(hasAdminUser);
    // }

    // @Test
    // public void testAddUser() throws Exception {
    //     User user = createUser(adminRole, "user", "pass");
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addUser(user);
    // }

    // @Test
    // public void testAddUserExistingUser() throws Exception {
    //     User user = createUser(adminRole, "user", "pass");
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addUser(user);

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.addUser(user);
    //     });
    // }

    // @Test
    // public void testGetRole() {
    //     Role role = dao.getRoleForRole(adminRole.getRoleName());
    //     Assertions.assertNotNull(role);
    //     Assertions.assertEquals(role.getRoleName(), adminRole.getRoleName());
    // }

    // @Test
    // public void testGetRoleInvalid() {
    //     Role role = dao.getRoleForRole("BAD");
    //     Assertions.assertNull(role);
    // }

    // @Test
    // public void testGetUser() throws Exception {
    //     User user = createUser(adminRole, "user", "pass");
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addUser(user);

    //     User fetchedUser = dao.getUser("user", "pass");
    //     Assertions.assertNotNull(fetchedUser);
    //     Assertions.assertEquals(fetchedUser.getUserName(), user.getUserName());
    // }

    // @Test
    // public void testGetUserNotFound() throws Exception {
    //     User user = createUser(adminRole, "user", "pass");
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addUser(user);

    //     User fetchedUser = dao.getUser("bad", "pass");
    //     Assertions.assertNull(fetchedUser);
    // }

    // @Test
    // public void testDaoExceptionWithException() throws Exception {
    //     User user = new User();
    //     user.setId(new Long(1));
        
    //     EntityManager em = Mockito.mock(EntityManager.class);
    //     doThrow(NotSupportedException.class).when(em).find(any(), any());
        
    //     dao.em = em;

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.addUser(user);
    //     });
    // }

    // private User createUser(Role role, String username, String password) {
    //     User user = new User();
    //     user.setUserName(username);
    //     user.setPassword(password);
    //     UserRole userRole = new UserRole();
    //     userRole.setRole(role);
    //     userRole.setUser(user);
    //     Set<UserRole> userRoleList = new HashSet<>();
    //     userRoleList.add(userRole);
    //     user.setRoles(userRoleList);
    //     return user;
    // }

    // private void populateRoles() {
    //     adminRole = persistRole("ADMIN");
    //     adjudicatorRole = persistRole("ADJUDICATOR");
    // }

    // private Role persistRole(String roleName) {
    //     em.getTransaction().begin();
    //     Role role = new Role();
    //     role.setRoleName(roleName);
    //     em.persist(role);
    //     em.getTransaction().commit();
    //     return role;
    // }
}
