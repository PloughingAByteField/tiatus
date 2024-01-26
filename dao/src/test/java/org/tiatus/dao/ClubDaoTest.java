package org.tiatus.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.tiatus.entity.Club;

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
    private ClubRepository repository;

    @Test
    public void testAddClub() throws DaoException {
        ClubDaoImpl dao = new ClubDaoImpl();
        dao.repository = repository;
        Club club = new Club();
        club.setId(1L);
        dao.addClub(club);
    }

    @Test 
    public void testAddClubExisting() throws DaoException {
        Club existingClub = new Club();
        existingClub.setId(1L);

        when(repository.existsById(any())).thenReturn(true);
        
        ClubDaoImpl dao = new ClubDaoImpl();
        dao.repository = repository;
        Club club = new Club();
        club.setId(1L);

        Assertions.assertThrows(DaoException.class, () -> {
            dao.addClub(club);
        });
    }

    @Test 
    public void testAddClubException() throws DaoException {
        doThrow(IllegalArgumentException .class).when(repository).save(any());

        ClubDaoImpl dao = new ClubDaoImpl();
        dao.repository = repository;
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

        when(repository.findAll()).thenReturn(list);

        ClubDaoImpl dao = new ClubDaoImpl();
        dao.repository = repository;
        Assertions.assertFalse(dao.getClubs().isEmpty());
    }

    @Test
    public void testRemoveClub() throws Exception {
        when(repository.existsById(any())).thenReturn(true);

        ClubDaoImpl dao = new ClubDaoImpl();
        dao.repository = repository;

        Club club = new Club();
        club.setId(1L);
        dao.removeClub(club);
    }

    @Test
    public void testRemoveClubNoClub() throws Exception {
        when(repository.existsById(any())).thenReturn(false);

        ClubDaoImpl dao = new ClubDaoImpl();
        dao.repository = repository;

        Club club = new Club();
        club.setId(1L);
        dao.removeClub(club);
    }

    @Test 
    public void testRemoveClubException() throws Exception {
        when(repository.existsById(any())).thenReturn(true);
        doThrow(IllegalArgumentException .class).when(repository).delete(any());

        ClubDaoImpl dao = new ClubDaoImpl();
        dao.repository = repository;

        Club club = new Club();
        club.setId(1L);
        
        Assertions.assertThrows(DaoException.class, () -> {
            dao.removeClub(club);
        });
    }

    @Test
    public void testUpdateClub() throws Exception {
        ClubDaoImpl dao = new ClubDaoImpl();
        dao.repository = repository;

        Club club = new Club();
        club.setId(1L);

        dao.updateClub(club);
    }

    @Test
    public void testUpdateClubException() throws Exception {
        doThrow(IllegalArgumentException .class).when(repository).save(any());

        ClubDaoImpl dao = new ClubDaoImpl();
        dao.repository = repository;

        Club club = new Club();
        club.setId(1L);
        
        Assertions.assertThrows(DaoException.class, () -> {
            dao.updateClub(club);
        });
    }
}
