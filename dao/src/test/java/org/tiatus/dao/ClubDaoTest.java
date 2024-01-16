package org.tiatus.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.tiatus.entity.Club;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 14/09/2016.
 */
@ExtendWith(MockitoExtension.class)
public class ClubDaoTest {
    
    @Mock
    private EntityManager entityManagerMock;

    @Mock
    private UserTransaction userTransactionMock;

    @Mock
    private TypedQuery typedQueryMock;

    @Test
    public void testAddClub() throws DaoException {
        ClubDaoImpl dao = new ClubDaoImpl();
        dao.em = entityManagerMock;
        dao.tx = userTransactionMock;
        Club club = new Club();
        club.setId(1L);
        dao.addClub(club);
    }

    @Test 
    public void testAddClubExisting() throws DaoException {
        Club existingClub = new Club();
        existingClub.setId(1L);

        when(entityManagerMock.find(any(), any())).thenReturn(existingClub);
        
        ClubDaoImpl dao = new ClubDaoImpl();
        dao.em = entityManagerMock;
        dao.tx = userTransactionMock;
        Club club = new Club();
        club.setId(1L);

        Assertions.assertThrows(DaoException.class, () -> {
            dao.addClub(club);
        });
    }

    @Test 
    public void testAddClubException() throws DaoException, IllegalStateException, SecurityException, HeuristicMixedException, HeuristicRollbackException, RollbackException, SystemException {
        doThrow(HeuristicMixedException.class).when(userTransactionMock).commit();

        ClubDaoImpl dao = new ClubDaoImpl();
        dao.em = entityManagerMock;
        dao.tx = userTransactionMock;
        Club club = new Club();
        club.setId(1L);

        Assertions.assertThrows(DaoException.class, () -> {
            dao.addClub(club);
        });
    }

    @Test
    public void testGetClubs() {
        List<Club> list = new ArrayList<>();
        Club club = new Club();
        club.setId(1L);
        list.add(club);

        when(typedQueryMock.getResultList()).thenReturn(list);
        when(entityManagerMock.createQuery(any(), any())).thenReturn(typedQueryMock);


        ClubDaoImpl dao = new ClubDaoImpl();
        dao.em = entityManagerMock;
        Assertions.assertFalse(dao.getClubs().isEmpty());
    }

    @Test
    public void testRemoveClub() throws Exception {
        
        Club existingClub = new Club();
        existingClub.setId(1L);

        when(entityManagerMock.find(any(), any())).thenReturn(existingClub);
        when(entityManagerMock.contains(any())).thenReturn(false);

        ClubDaoImpl dao = new ClubDaoImpl();
        dao.em = entityManagerMock;
        dao.tx = userTransactionMock;

        Club club = new Club();
        club.setId(1L);
        dao.removeClub(club);
    }

    @Test
    public void testRemoveClubNoClub() throws Exception {
        when(entityManagerMock.find(any(), any())).thenReturn(null);

        ClubDaoImpl dao = new ClubDaoImpl();
        dao.em = entityManagerMock;
        dao.tx = userTransactionMock;

        Club club = new Club();
        club.setId(1L);
        dao.removeClub(club);
    }

    @Test 
    public void testRemoveClubException() throws Exception {
        doThrow(HeuristicMixedException.class).when(userTransactionMock).commit();

        Club existingClub = new Club();
        existingClub.setId(1L);

        when(entityManagerMock.find(any(), any())).thenReturn(existingClub);
        when(entityManagerMock.contains(any())).thenReturn(false);

        ClubDaoImpl dao = new ClubDaoImpl();
        dao.em = entityManagerMock;
        dao.tx = userTransactionMock;
        Club club = new Club();
        club.setId(1L);
        
        Assertions.assertThrows(DaoException.class, () -> {
            dao.removeClub(club);
        });
    }

    @Test
    public void testUpdateClub() throws Exception {
        ClubDaoImpl dao = new ClubDaoImpl();
        dao.em = entityManagerMock;
        dao.tx = userTransactionMock;
        Club club = new Club();
        club.setId(1L);

        dao.updateClub(club);
    }

    @Test
    public void testUpdateClubException() throws Exception {
        doThrow(HeuristicMixedException.class).when(userTransactionMock).commit();

        ClubDaoImpl dao = new ClubDaoImpl();
        dao.em = entityManagerMock;
        dao.tx = userTransactionMock;
        Club club = new Club();
        club.setId(1L);
        
        Assertions.assertThrows(DaoException.class, () -> {
            dao.updateClub(club);
        });
    }
}
