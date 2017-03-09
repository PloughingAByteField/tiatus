package org.tiatus.dao;

import mockit.Mock;
import mockit.MockUp;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Position;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.transaction.*;
import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
public class PositionIT {

    private static final Logger LOG = LoggerFactory.getLogger(PositionIT.class);
    private PositionDaoImpl dao;
    private EntityManager em;

    @Before
    public void setUp() throws Exception {
        dao = new PositionDaoImpl();
        em = Persistence.createEntityManagerFactory("primary").createEntityManager();
        dao.em = em;
    }

    @After
    public void tearDown() throws Exception {
        em.close();
    }

    @Test
    public void getPositions() {
        List<Position> positions = dao.getPositions();
        Assert.assertTrue(positions.isEmpty());

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
        Assert.assertTrue(!positions.isEmpty());
        Assert.assertTrue(positions.size() == 2);
    }

    @Test
    public void addPosition() throws Exception {
        List<Position> positions = dao.getPositions();
        Assert.assertTrue(positions.isEmpty());
        Position newPosition = new Position();
        newPosition.setId(1L);
        newPosition.setName("Position 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addPosition(newPosition);

        positions = dao.getPositions();
        Assert.assertTrue(!positions.isEmpty());
        Assert.assertTrue(positions.size() == 1);
    }


    @Test (expected = DaoException.class)
    public void addExistingPosition() throws Exception {
        List<Position> positions = dao.getPositions();
        Assert.assertTrue(positions.isEmpty());
        Position newPosition = new Position();
        newPosition.setId(1L);
        newPosition.setName("Position 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addPosition(newPosition);
        dao.addPosition(newPosition);
    }

    @Test (expected = DaoException.class)
    public void testDaoExceptionWithException() throws Exception {
        List<Position> positions = dao.getPositions();
        Assert.assertTrue(positions.isEmpty());
        Position newPosition = new Position();
        newPosition.setId(1L);
        newPosition.setName("Position 1");
        EntityManager em = new MockUp<EntityManager>(){
            @Mock
            public <T> T find(Class<T> entityClass, Object primaryKey) throws NotSupportedException {
                throw new NotSupportedException();
            }
        }.getMockInstance();
        dao.em = em;

        dao.addPosition(newPosition);
    }

    @Test
    public void removePosition() throws Exception {
        List<Position> positions = dao.getPositions();
        Assert.assertTrue(positions.isEmpty());
        Position position = new Position();
        position.setId(1L);
        position.setName("Position 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addPosition(position);

        positions = dao.getPositions();
        Assert.assertTrue(!positions.isEmpty());
        Assert.assertTrue(positions.size() == 1);

        dao.removePosition(position);
        positions = dao.getPositions();
        Assert.assertTrue(positions.isEmpty());
    }


    @Test
    public void removePositionNotExisting() throws Exception {
        List<Position> positions = dao.getPositions();
        Assert.assertTrue(positions.isEmpty());
        Position position = new Position();
        position.setId(1L);
        position.setName("Position 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addPosition(position);

        positions = dao.getPositions();
        Assert.assertTrue(!positions.isEmpty());
        Assert.assertTrue(positions.size() == 1);

        Position position2 = new Position();
        position2.setId(2L);
        position2.setName("Position 2");
        dao.removePosition(position2);
        positions = dao.getPositions();
        Assert.assertTrue(!positions.isEmpty());
        Assert.assertTrue(positions.size() == 1);
        Assert.assertEquals(positions.get(0).getName(), "Position 1");
    }


    @Test (expected = DaoException.class)
    public void removePositionWithException() throws Exception {
        Position position = new Position();
        position.setId(1L);
        position.setName("Position 1");

        EntityManager em = new MockUp<EntityManager>(){
            @Mock
            public <T> T find(Class<T> entityClass, Object primaryKey) throws NotSupportedException {
                throw new NotSupportedException();
            }
        }.getMockInstance();
        dao.em = em;

        dao.removePosition(position);
    }

    @Test
    public void updatePosition() throws Exception {
        List<Position> positions = dao.getPositions();
        Assert.assertTrue(positions.isEmpty());
        Position newPosition = new Position();
        newPosition.setId(1L);
        newPosition.setName("Position 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addPosition(newPosition);

        positions = dao.getPositions();
        Assert.assertTrue(!positions.isEmpty());
        Assert.assertTrue(positions.size() == 1);
        Assert.assertEquals(positions.get(0).getName(), "Position 1");
        newPosition.setName("new name");
        dao.updatePosition(newPosition);
        positions = dao.getPositions();
        Assert.assertTrue(!positions.isEmpty());
        Assert.assertTrue(positions.size() == 1);
        Assert.assertEquals(positions.get(0).getId(), Long.valueOf(1L));
        Assert.assertEquals(positions.get(0).getName(), "new name");
    }

    @Test (expected = DaoException.class)
    public void updatePositionWithException() throws Exception {
        Position position = new Position();
        position.setId(1L);
        position.setName("Position 1");

        UserTransaction tx = new MockUp<UserTransaction>() {
            @Mock
            void commit() throws HeuristicMixedException, HeuristicRollbackException, IllegalStateException, RollbackException, SecurityException, SystemException {
                throw new HeuristicMixedException();
            }

        }.getMockInstance();
        dao.tx = tx;
        dao.updatePosition(position);
    }
}
