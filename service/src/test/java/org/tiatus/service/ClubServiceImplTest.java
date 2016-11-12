package org.tiatus.service;

import mockit.Mock;
import mockit.MockUp;
import org.junit.Test;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.ClubDaoImpl;
import org.tiatus.entity.Club;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class ClubServiceImplTest {

    @Test
    public void testConstructor() {
        ClubServiceImpl service = new ClubServiceImpl(new ClubDaoImpl());
    }

    @Test
    public void testAddClub() throws Exception {
        new MockUp<ClubDaoImpl>() {
            @Mock
            Club addClub(Club club) throws DaoException {
                return club;
            }
        };
        ClubServiceImpl service = new ClubServiceImpl(new ClubDaoImpl());
        Club club = new Club();
        service.addClub(club);
    }

    @Test (expected = ServiceException.class)
    public void testAddClubException() throws Exception {
        new MockUp<ClubDaoImpl>() {
            @Mock
            Club addClub(Club club) throws DaoException {
                throw new DaoException("message");
            }
        };
        ClubServiceImpl service = new ClubServiceImpl(new ClubDaoImpl());
        Club club = new Club();
        service.addClub(club);
    }

    @Test
    public void testDeleteClub() throws Exception {
        new MockUp<ClubDaoImpl>() {
            @Mock
            public void removeClub(Club club) throws DaoException {}
        };
        ClubServiceImpl service = new ClubServiceImpl(new ClubDaoImpl());
        Club club = new Club();
        service.deleteClub(club);
    }

    @Test (expected = ServiceException.class)
    public void testDeleteClubException() throws Exception {
        new MockUp<ClubDaoImpl>() {
            @Mock
            public void removeClub(Club club) throws DaoException {
                throw new DaoException("message");
            }
        };
        ClubServiceImpl service = new ClubServiceImpl(new ClubDaoImpl());
        Club club = new Club();
        service.deleteClub(club);
    }

    @Test
    public void testUpdateClub() throws Exception {
        new MockUp<ClubDaoImpl>() {
            @Mock
            public void updateClub(Club club) throws DaoException {}
        };
        ClubServiceImpl service = new ClubServiceImpl(new ClubDaoImpl());
        Club club = new Club();
        service.updateClub(club);
    }

    @Test (expected = ServiceException.class)
    public void testUpdateClubException() throws Exception {
        new MockUp<ClubDaoImpl>() {
            @Mock
            public void updateClub(Club club) throws DaoException {
                throw new DaoException("message");
            }
        };
        ClubServiceImpl service = new ClubServiceImpl(new ClubDaoImpl());
        Club club = new Club();
        service.updateClub(club);
    }

    @Test
    public void testGetClubs() throws Exception {
        new MockUp<ClubDaoImpl>() {
            @Mock
            public List<Club> getClubs() {
                List<Club> clubs = new ArrayList<>();
                Club club = new Club();
                club.setId(1L);
                club.setClubName("Club 1");
                clubs.add(club);
                return clubs;
            }
        };
        ClubServiceImpl service = new ClubServiceImpl(new ClubDaoImpl());
        service.getClubs();
    }

}
