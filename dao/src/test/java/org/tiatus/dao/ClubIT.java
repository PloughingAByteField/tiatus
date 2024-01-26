package org.tiatus.dao;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.Mockito;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.tiatus.entity.Club;

// import javax.persistence.EntityManager;
// import javax.persistence.Persistence;
// import javax.transaction.*;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.doThrow;

// import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
// @ExtendWith(MockitoExtension.class)
public class ClubIT {

    // private static final Logger LOG = LoggerFactory.getLogger(ClubIT.class);
    // private ClubDaoImpl dao;
    // private EntityManager em;

    // @BeforeEach
    // public void setUp() throws Exception {
    //     dao = new ClubDaoImpl();
    //     em = Persistence.createEntityManagerFactory("primary").createEntityManager();
    //     dao.em = em;
    // }

    // @AfterEach
    // public void tearDown() throws Exception {
    //     em.close();
    // }

    // @Test
    // public void getClubs() {
    //     List<Club> clubs = dao.getClubs();
    //     Assertions.assertTrue(clubs.isEmpty());

    //     // add club
    //     em.getTransaction().begin();
    //     Club club1 = new Club();
    //     club1.setId(1L);
    //     club1.setClubName("Club 1");
    //     Club club2 = new Club();
    //     club2.setId(2L);
    //     club2.setClubName("Club 2");
    //     em.merge(club1);
    //     em.merge(club2);
    //     em.getTransaction().commit();

    //     clubs = dao.getClubs();
    //     Assertions.assertTrue(!clubs.isEmpty());
    //     Assertions.assertTrue(clubs.size() == 2);
    // }

    // @Test
    // public void addClub() throws Exception {
    //     List<Club> clubs = dao.getClubs();
    //     Assertions.assertTrue(clubs.isEmpty());
    //     Club newClub = new Club();
    //     newClub.setClubName("Club 1");
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addClub(newClub);

    //     clubs = dao.getClubs();
    //     Assertions.assertTrue(!clubs.isEmpty());
    //     Assertions.assertTrue(clubs.size() == 1);
    // }


    // @Test
    // public void addExistingClub() throws Exception {
    //     List<Club> clubs = dao.getClubs();
    //     Assertions.assertTrue(clubs.isEmpty());
    //     Club newClub = new Club();
    //     newClub.setId(1L);
    //     newClub.setClubName("Club 1");
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addClub(newClub);
        
    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.addClub(newClub);
    //     });
    // }

    // @Test
    // public void testDaoExceptionWithException() throws Exception {
    //     List<Club> clubs = dao.getClubs();
    //     Assertions.assertTrue(clubs.isEmpty());
    //     Club newClub = new Club();
    //     newClub.setId(1L);
    //     newClub.setClubName("Club 1");

    //     EntityManager em = Mockito.mock(EntityManager.class);
    //     doThrow(NotSupportedException.class).when(em).find(any(), any());
        
    //     dao.em = em;
    //     dao.tx = new EntityUserTransaction(em);

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.addClub(newClub);
    //     });
    // }

    // @Test
    // public void removeClub() throws Exception {
    //     List<Club> clubs = dao.getClubs();
    //     Assertions.assertTrue(clubs.isEmpty());
    //     Club club = new Club();
    //     club.setClubName("Club 1");
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addClub(club);

    //     clubs = dao.getClubs();
    //     Assertions.assertTrue(!clubs.isEmpty());
    //     Assertions.assertTrue(clubs.size() == 1);

    //     dao.removeClub(club);
    //     clubs = dao.getClubs();
    //     Assertions.assertTrue(clubs.isEmpty());
    // }


    // @Test
    // public void removeClubNotExisting() throws Exception {
    //     List<Club> clubs = dao.getClubs();
    //     Assertions.assertTrue(clubs.isEmpty());
    //     Club club = new Club();
    //     club.setClubName("Club 1");
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addClub(club);

    //     clubs = dao.getClubs();
    //     Assertions.assertTrue(!clubs.isEmpty());
    //     Assertions.assertTrue(clubs.size() == 1);

    //     Club club2 = new Club();
    //     club2.setId(2L);
    //     club2.setClubName("Club 2");
    //     dao.removeClub(club2);
    //     clubs = dao.getClubs();
    //     Assertions.assertTrue(!clubs.isEmpty());
    //     Assertions.assertTrue(clubs.size() == 1);
    //     Assertions.assertEquals(clubs.get(0).getClubName(), "Club 1");
    // }


    // @Test
    // public void removeClubWithException() throws Exception {
    //     Club club = new Club();
    //     club.setId(1L);
    //     club.setClubName("Club 1");

    //     doThrow(NotSupportedException.class).when(em).find(any(), any());
    //     dao.em = em;
    //     dao.tx = new EntityUserTransaction(em);

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.removeClub(club);
    //     });
    // }

    // @Test
    // public void updateClub() throws Exception {
    //     List<Club> clubs = dao.getClubs();
    //     Assertions.assertTrue(clubs.isEmpty());
    //     Club newClub = new Club();
    //     newClub.setClubName("Club 1");
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addClub(newClub);

    //     clubs = dao.getClubs();
    //     Assertions.assertTrue(!clubs.isEmpty());
    //     Assertions.assertTrue(clubs.size() == 1);
    //     Assertions.assertEquals(clubs.get(0).getClubName(), "Club 1");
    //     newClub.setClubName("new name");
    //     dao.updateClub(newClub);
    //     clubs = dao.getClubs();
    //     Assertions.assertTrue(!clubs.isEmpty());
    //     Assertions.assertTrue(clubs.size() == 1);
    //     Assertions.assertEquals(clubs.get(0).getId(), Long.valueOf(1L));
    //     Assertions.assertEquals(clubs.get(0).getClubName(), "new name");
    // }

    // @Test
    // public void updateClubWithException() throws Exception {
    //     Club club = new Club();
    //     club.setId(1L);
    //     club.setClubName("Club 1");

    //     UserTransaction userTransactionMock = Mockito.mock(UserTransaction.class);
    //     doThrow(HeuristicMixedException.class).when(userTransactionMock).commit();

    //     dao.tx = userTransactionMock;

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.updateClub(club);
    //     });
    // }
}
