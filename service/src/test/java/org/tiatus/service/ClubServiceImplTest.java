package org.tiatus.service;

import org.junit.Test;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.ClubDaoImpl;
import org.tiatus.entity.Club;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class ClubServiceImplTest {

    @Test
    public void testConstructor() {
        ClubServiceImpl service = new ClubServiceImpl(new ClubDaoImpl(), new MessageSenderServiceImpl());
    }

    // @Test
    // public void testAddClub() throws Exception {
    //     new MockUp<ClubDaoImpl>() {
    //         @Mock
    //         Club addClub(Club club) throws DaoException {
    //             return club;
    //         }
    //     };
    //     new MockUp<MessageSenderServiceImpl>() {
    //         @Mock
    //         public void sendMessage(final Message obj) throws JMSException {}
    //     };
    //     ClubServiceImpl service = new ClubServiceImpl(new ClubDaoImpl(), new MessageSenderServiceImpl());
    //     Club club = new Club();
    //     service.addClub(club, "id");
    // }

    // @Test (expected = ServiceException.class)
    // public void testAddClubException() throws Exception {
    //     new MockUp<ClubDaoImpl>() {
    //         @Mock
    //         Club addClub(Club club) throws DaoException {
    //             throw new DaoException("message");
    //         }
    //     };
    //     ClubServiceImpl service = new ClubServiceImpl(new ClubDaoImpl(), new MessageSenderServiceImpl());
    //     Club club = new Club();
    //     service.addClub(club, "id");
    // }

    // @Test
    // public void testDeleteClub() throws Exception {
    //     new MockUp<ClubDaoImpl>() {
    //         @Mock
    //         public void removeClub(Club club) throws DaoException {}
    //     };
    //     ClubServiceImpl service = new ClubServiceImpl(new ClubDaoImpl(), new MessageSenderServiceImpl());
    //     Club club = new Club();
    //     service.deleteClub(club, "id");
    // }

    // @Test (expected = ServiceException.class)
    // public void testDeleteClubException() throws Exception {
    //     new MockUp<ClubDaoImpl>() {
    //         @Mock
    //         public void removeClub(Club club) throws DaoException {
    //             throw new DaoException("message");
    //         }
    //     };
    //     ClubServiceImpl service = new ClubServiceImpl(new ClubDaoImpl(), new MessageSenderServiceImpl());
    //     Club club = new Club();
    //     service.deleteClub(club, "id");
    // }

    // @Test
    // public void testUpdateClub() throws Exception {
    //     new MockUp<ClubDaoImpl>() {
    //         @Mock
    //         public void updateClub(Club club) throws DaoException {}
    //     };
    //     ClubServiceImpl service = new ClubServiceImpl(new ClubDaoImpl(), new MessageSenderServiceImpl());
    //     Club club = new Club();
    //     service.updateClub(club, "id");
    // }

    // @Test (expected = ServiceException.class)
    // public void testUpdateClubException() throws Exception {
    //     new MockUp<ClubDaoImpl>() {
    //         @Mock
    //         public void updateClub(Club club) throws DaoException {
    //             throw new DaoException("message");
    //         }
    //     };
    //     ClubServiceImpl service = new ClubServiceImpl(new ClubDaoImpl(), new MessageSenderServiceImpl());
    //     Club club = new Club();
    //     service.updateClub(club, "id");
    // }

    // @Test
    // public void testGetClubs() throws Exception {
    //     new MockUp<ClubDaoImpl>() {
    //         @Mock
    //         public List<Club> getClubs() {
    //             List<Club> clubs = new ArrayList<>();
    //             Club club = new Club();
    //             club.setId(1L);
    //             club.setClubName("Club 1");
    //             clubs.add(club);
    //             return clubs;
    //         }
    //     };
    //     ClubServiceImpl service = new ClubServiceImpl(new ClubDaoImpl(), new MessageSenderServiceImpl());
    //     service.getClubs();
    // }

}
