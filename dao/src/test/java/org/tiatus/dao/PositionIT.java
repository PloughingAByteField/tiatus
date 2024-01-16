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
import org.tiatus.entity.Position;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.transaction.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
@ExtendWith(MockitoExtension.class)
public class PositionIT {

    private static final Logger LOG = LoggerFactory.getLogger(PositionIT.class);
    private PositionDaoImpl dao;
    private EntityManager em;

    @BeforeEach
    public void setUp() throws Exception {
        dao = new PositionDaoImpl();
        em = Persistence.createEntityManagerFactory("primary").createEntityManager();
        dao.em = em;
    }

    @AfterEach
    public void tearDown() throws Exception {
        em.close();
    }

    @Test
    public void getPositions() {
        List<Position> positions = dao.getPositions();
        Assertions.assertTrue(positions.isEmpty());

        // add position
        em.getTransaction().begin();
        Position position1 = new Position();
        position1.setId(1L);
        position1.setName("Position 1");
        Position position2 = new Position();
        position2.setId(2L);
        position2.setName("Position 2");
        em.merge(position1);
        em.merge(position2);
        em.getTransaction().commit();

        positions = dao.getPositions();
        Assertions.assertTrue(!positions.isEmpty());
        Assertions.assertTrue(positions.size() == 2);
    }

    @Test
    public void addPosition() throws Exception {
        List<Position> positions = dao.getPositions();
        Assertions.assertTrue(positions.isEmpty());
        Position newPosition = new Position();
        newPosition.setName("Position 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addPosition(newPosition);

        positions = dao.getPositions();
        Assertions.assertTrue(!positions.isEmpty());
        Assertions.assertTrue(positions.size() == 1);
    }


    @Test
    public void addExistingPosition() throws Exception {
        List<Position> positions = dao.getPositions();
        Assertions.assertTrue(positions.isEmpty());
        Position newPosition = new Position();
        newPosition.setId(1L);
        newPosition.setName("Position 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addPosition(newPosition);

        Assertions.assertThrows(DaoException.class, () -> {
            dao.addPosition(newPosition);
        });
    }

    @Test
    public void testDaoExceptionWithException() throws Exception {
        List<Position> positions = dao.getPositions();
        Assertions.assertTrue(positions.isEmpty());
        Position newPosition = new Position();
        newPosition.setId(1L);
        newPosition.setName("Position 1");

        EntityManager em = Mockito.mock(EntityManager.class);
        doThrow(NotSupportedException.class).when(em).find(any(), any());
        
        dao.em = em;

        Assertions.assertThrows(DaoException.class, () -> {
            dao.addPosition(newPosition);
        });
    }

    @Test
    public void removePosition() throws Exception {
        List<Position> positions = dao.getPositions();
        Assertions.assertTrue(positions.isEmpty());
        Position position = new Position();
        position.setName("Position 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addPosition(position);

        positions = dao.getPositions();
        Assertions.assertTrue(!positions.isEmpty());
        Assertions.assertTrue(positions.size() == 1);

        dao.removePosition(position);
        positions = dao.getPositions();
        Assertions.assertTrue(positions.isEmpty());
    }


    @Test
    public void removePositionNotExisting() throws Exception {
        List<Position> positions = dao.getPositions();
        Assertions.assertTrue(positions.isEmpty());
        Position position = new Position();
        position.setName("Position 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addPosition(position);

        positions = dao.getPositions();
        Assertions.assertTrue(!positions.isEmpty());
        Assertions.assertTrue(positions.size() == 1);

        Position position2 = new Position();
        position2.setId(2L);
        position2.setName("Position 2");
        dao.removePosition(position2);
        positions = dao.getPositions();
        Assertions.assertTrue(!positions.isEmpty());
        Assertions.assertTrue(positions.size() == 1);
        Assertions.assertEquals(positions.get(0).getName(), "Position 1");
    }


    @Test
    public void removePositionWithException() throws Exception {
        Position position = new Position();
        position.setId(1L);
        position.setName("Position 1");

        EntityManager em = Mockito.mock(EntityManager.class);
        doThrow(NotSupportedException.class).when(em).find(any(), any());
        
        dao.em = em;

        Assertions.assertThrows(DaoException.class, () -> {
            dao.removePosition(position);
        });
    }

    @Test
    public void updatePosition() throws Exception {
        List<Position> positions = dao.getPositions();
        Assertions.assertTrue(positions.isEmpty());
        Position newPosition = new Position();
        newPosition.setName("Position 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addPosition(newPosition);

        positions = dao.getPositions();
        Assertions.assertTrue(!positions.isEmpty());
        Assertions.assertTrue(positions.size() == 1);
        Assertions.assertEquals(positions.get(0).getName(), "Position 1");
        newPosition.setName("new name");
        dao.updatePosition(newPosition);
        positions = dao.getPositions();
        Assertions.assertTrue(!positions.isEmpty());
        Assertions.assertTrue(positions.size() == 1);
        Assertions.assertEquals(positions.get(0).getId(), Long.valueOf(1L));
        Assertions.assertEquals(positions.get(0).getName(), "new name");
    }

    @Test
    public void updatePositionWithException() throws Exception {
        Position position = new Position();
        position.setId(1L);
        position.setName("Position 1");

        UserTransaction userTransactionMock = Mockito.mock(UserTransaction.class);
        doThrow(HeuristicMixedException.class).when(userTransactionMock).commit();
        
        dao.tx = userTransactionMock;

        Assertions.assertThrows(DaoException.class, () -> {
            dao.updatePosition(position);
        });
    }
}
