package org.tiatus.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.tiatus.entity.Role;
import org.tiatus.entity.User;
import org.tiatus.entity.UserRole;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 14/09/2016.
 */
@ExtendWith(MockitoExtension.class)
public class UserDaoTest {

    @Mock
    private EntityManager entityManagerMock;

    @Mock
    private UserTransaction userTransactionMock;

    @Mock
    private TypedQuery typedQueryMock;
    
    @Test
    public void testHasAdminUser() {
        List<UserRole> list = new ArrayList<>();
        UserRole userRole = new UserRole();
        list.add(userRole);

        when(typedQueryMock.getResultList()).thenReturn(list);
        when(typedQueryMock.setParameter(anyString(), any())).thenReturn(typedQueryMock);
        when(entityManagerMock.createQuery(any(), any())).thenReturn(typedQueryMock);

        UserDaoImpl dao = new UserDaoImpl();
        dao.em = entityManagerMock;
        Assertions.assertTrue(dao.hasAdminUser());
    }

    @Test
    public void testHasAdminUserNoUser() {
        
        when(typedQueryMock.getResultList()).thenReturn(new ArrayList<>());
        when(typedQueryMock.setParameter(anyString(), any())).thenReturn(typedQueryMock);
        when(entityManagerMock.createQuery(any(), any())).thenReturn(typedQueryMock);


        UserDaoImpl dao = new UserDaoImpl();
        dao.em = entityManagerMock;
        Assertions.assertFalse(dao.hasAdminUser());
    }

    @Test
    public void testGetRoleForRole() {
        List<Role> list = new ArrayList<>();
        Role existingRole = new Role();
        existingRole.setRoleName("ADMIN");
        list.add(existingRole);

        when(typedQueryMock.getResultList()).thenReturn(list);
        when(typedQueryMock.getSingleResult()).thenReturn(existingRole);
        when(typedQueryMock.setParameter(anyString(), any())).thenReturn(typedQueryMock);
        when(entityManagerMock.createQuery(any(), any())).thenReturn(typedQueryMock);

        UserDaoImpl dao = new UserDaoImpl();
        dao.em = entityManagerMock;
        Role role = dao.getRoleForRole("ADMIN");
        Assertions.assertEquals(role.getRoleName(), "ADMIN");
    }

    @Test
    public void testGetUser() {
        List<User> list = new ArrayList<>();
        User existingUser = new User();
        existingUser.setUserName("username");
        list.add(existingUser);

        when(typedQueryMock.getResultList()).thenReturn(list);
        when(typedQueryMock.getSingleResult()).thenReturn(existingUser);
        when(typedQueryMock.setParameter(anyString(), any())).thenReturn(typedQueryMock);
        when(entityManagerMock.createQuery(any(), any())).thenReturn(typedQueryMock);

        UserDaoImpl dao = new UserDaoImpl();
        dao.em = entityManagerMock;
        User user = dao.getUser("username", "password");
        Assertions.assertEquals(user.getUserName(), "username");
    }

    @Test
    public void testGetUserNoUser() {
        when(typedQueryMock.getResultList()).thenReturn(new ArrayList<>());
        when(typedQueryMock.setParameter(anyString(), any())).thenReturn(typedQueryMock);
        when(entityManagerMock.createQuery(any(), any())).thenReturn(typedQueryMock);

        UserDaoImpl dao = new UserDaoImpl();
        dao.em = entityManagerMock;
        User user = dao.getUser("username", "password");
        Assertions.assertNull(user);
    }

    @Test
    public void testAddUser() throws DaoException {
        when(entityManagerMock.find(any(), any())).thenReturn(null);

        UserDaoImpl dao = new UserDaoImpl();
        dao.em = entityManagerMock;
        dao.tx = userTransactionMock;
        User user = new User();
        user.setId(1L);
        dao.addUser(user);
    }

    @Test
    public void testAddUserExisting() throws DaoException {
        User existingUser = new User();
        existingUser.setId(1L);

        when(entityManagerMock.find(any(), any())).thenReturn(existingUser);

        UserDaoImpl dao = new UserDaoImpl();
        dao.em = entityManagerMock;
        dao.tx = userTransactionMock;
        User user = new User();
        user.setId(1L);

        Assertions.assertThrows(DaoException.class, () -> {
            dao.addUser(user);
        });
    }

    @Test
    public void testAddUserException() throws Exception {
        when(entityManagerMock.find(any(), any())).thenReturn(null);
        
        doThrow(HeuristicMixedException.class).when(userTransactionMock).commit();

        UserDaoImpl dao = new UserDaoImpl();
        dao.em = entityManagerMock;
        dao.tx = userTransactionMock;
        User user = new User();
        user.setId(1L);

        Assertions.assertThrows(DaoException.class, () -> {
            dao.addUser(user);
        });
    }
}
