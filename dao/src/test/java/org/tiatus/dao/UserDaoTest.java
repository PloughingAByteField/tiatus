package org.tiatus.dao;

import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Test;
import org.tiatus.entity.Role;
import org.tiatus.entity.User;
import org.tiatus.entity.UserRole;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class UserDaoTest {
    @Test
    public void testHasAdminUser() {
        TypedQuery typedQuery = new MockUp<TypedQuery>() {
            @Mock
            public TypedQuery setParameter(String name, Object value) {
                return this.getMockInstance();
            }

            @Mock
            public List<UserRole> getResultList() {
                List<UserRole> list = new ArrayList<UserRole>();
                UserRole userRole = new UserRole();
                list.add(userRole);
                return list;
            }
        }.getMockInstance();

        EntityManager em = new MockUp<EntityManager>() {
           @Mock
           public  TypedQuery createQuery(String qlString, Class resultClass) {
               return typedQuery;
           }
        }.getMockInstance();

        UserDaoImpl dao = new UserDaoImpl();
        dao.em = em;
        Assert.assertTrue(dao.hasAdminUser());
    }

    @Test
    public void testHasAdminUserNoUser() {
        TypedQuery typedQuery = new MockUp<TypedQuery>() {
            @Mock
            public TypedQuery setParameter(String name, Object value) {
                return this.getMockInstance();
            }

            @Mock
            public List<UserRole> getResultList() {
                return new ArrayList<>();
            }
        }.getMockInstance();

        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public  TypedQuery createQuery(String qlString, Class resultClass) {
                return typedQuery;
            }
        }.getMockInstance();

        UserDaoImpl dao = new UserDaoImpl();
        dao.em = em;
        Assert.assertFalse(dao.hasAdminUser());
    }

    @Test
    public void testGetRoleForRole() {
        TypedQuery typedQuery = new MockUp<TypedQuery>() {
            @Mock
            public TypedQuery setParameter(String name, Object value) {
                return this.getMockInstance();
            }

            @Mock
            public List<Role> getResultList() {
                List<Role> list = new ArrayList<Role>();
                Role role = new Role();
                role.setRoleName("ADMIN");
                list.add(role);
                return list;
            }

            @Mock
            public Role getSingleResult() {
                Role role = new Role();
                role.setRoleName("ADMIN");
                return role;
            }
        }.getMockInstance();

        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public  TypedQuery createQuery(String qlString, Class resultClass) {
                return typedQuery;
            }
        }.getMockInstance();

        UserDaoImpl dao = new UserDaoImpl();
        dao.em = em;
        Role role = dao.getRoleForRole("ADMIN");
        Assert.assertEquals(role.getRoleName(), "ADMIN");
    }

    @Test
    public void testGetUser() {
        TypedQuery typedQuery = new MockUp<TypedQuery>() {
            @Mock
            public TypedQuery setParameter(String name, Object value) {
                return this.getMockInstance();
            }

            @Mock
            public List<User> getResultList() {
                List<User> list = new ArrayList<User>();
                User user = new User();
                user.setUserName("username");
                list.add(user);
                return list;
            }

            @Mock
            public User getSingleResult() {
                User user = new User();
                user.setUserName("username");
                return user;
            }
        }.getMockInstance();

        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public  TypedQuery createQuery(String qlString, Class resultClass) {
                return typedQuery;
            }
        }.getMockInstance();

        UserDaoImpl dao = new UserDaoImpl();
        dao.em = em;
        User user = dao.getUser("username", "password");
        Assert.assertEquals(user.getUserName(), "username");
    }

    @Test
    public void testAddUser() throws DaoException {
        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public User find(Class entityClass, Object primaryKey) {
                return null;
            }

            @Mock
            public void persist(Object entity) {

            }
        }.getMockInstance();

        UserTransaction tx = new MockUp<UserTransaction>() {
            @Mock
            void begin() throws NotSupportedException, SystemException {

            }

            @Mock
            void commit() throws HeuristicMixedException, HeuristicRollbackException, IllegalStateException, RollbackException, SecurityException, SystemException {

            }

        }.getMockInstance();

        UserDaoImpl dao = new UserDaoImpl();
        dao.em = em;
        dao.tx = tx;
        User user = new User();
        user.setId(1L);
        dao.addUser(user);
    }

    @Test (expected = DaoException.class)
    public void testAddUserExisting() throws DaoException {
        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public User find(Class entityClass, Object primaryKey) {
                User user = new User();
                user.setId(1L);
                return user;
            }

            @Mock
            public void persist(Object entity) {

            }
        }.getMockInstance();

        UserTransaction tx = new MockUp<UserTransaction>() {
            @Mock
            void begin() throws NotSupportedException, SystemException {

            }

            @Mock
            void commit() throws HeuristicMixedException, HeuristicRollbackException, IllegalStateException, RollbackException, SecurityException, SystemException {

            }

        }.getMockInstance();

        UserDaoImpl dao = new UserDaoImpl();
        dao.em = em;
        dao.tx = tx;
        User user = new User();
        user.setId(1L);
        dao.addUser(user);
    }

    @Test (expected = DaoException.class)
    public void testAddUserException() throws DaoException {
        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public User find(Class entityClass, Object primaryKey) {
                User user = new User();
                user.setId(1L);
                return user;
            }

            @Mock
            public void persist(Object entity) {

            }
        }.getMockInstance();

        UserTransaction tx = new MockUp<UserTransaction>() {
            @Mock
            void begin() throws NotSupportedException, SystemException {

            }

            @Mock
            void commit() throws HeuristicMixedException, HeuristicRollbackException, IllegalStateException, RollbackException, SecurityException, SystemException {
                throw new HeuristicMixedException();
            }

        }.getMockInstance();

        UserDaoImpl dao = new UserDaoImpl();
        dao.em = em;
        dao.tx = tx;
        User user = new User();
        user.setId(1L);
        dao.addUser(user);
    }
}
