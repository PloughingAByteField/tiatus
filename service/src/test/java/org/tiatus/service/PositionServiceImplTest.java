package org.tiatus.service;

import mockit.Mock;
import mockit.MockUp;
import org.junit.Test;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.PositionDaoImpl;
import org.tiatus.entity.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class PositionServiceImplTest {

    @Test
    public void testConstructor() {
        PositionServiceImpl service = new PositionServiceImpl(new PositionDaoImpl());
    }

    @Test
    public void testAddPosition() throws Exception {
        new MockUp<PositionDaoImpl>() {
            @Mock
            Position addPosition(Position position) throws DaoException {
                return position;
            }
        };
        PositionServiceImpl service = new PositionServiceImpl(new PositionDaoImpl());
        Position position = new Position();
        service.addPosition(position);
    }

    @Test (expected = ServiceException.class)
    public void testAddPositionException() throws Exception {
        new MockUp<PositionDaoImpl>() {
            @Mock
            Position addPosition(Position position) throws DaoException {
                throw new DaoException("message");
            }
        };
        PositionServiceImpl service = new PositionServiceImpl(new PositionDaoImpl());
        Position position = new Position();
        service.addPosition(position);
    }

    @Test
    public void testDeletePosition() throws Exception {
        new MockUp<PositionDaoImpl>() {
            @Mock
            public void removePosition(Position position) throws DaoException {}
        };
        PositionServiceImpl service = new PositionServiceImpl(new PositionDaoImpl());
        Position position = new Position();
        service.removePosition(position);
    }

    @Test (expected = ServiceException.class)
    public void testDeletePositionException() throws Exception {
        new MockUp<PositionDaoImpl>() {
            @Mock
            public void removePosition(Position position) throws DaoException {
                throw new DaoException("message");
            }
        };
        PositionServiceImpl service = new PositionServiceImpl(new PositionDaoImpl());
        Position position = new Position();
        service.removePosition(position);
    }

    @Test
    public void testGetPositions() throws Exception {
        new MockUp<PositionDaoImpl>() {
            @Mock
            public List<Position> getPositions() {
                List<Position> positions = new ArrayList<>();
                Position position = new Position();
                position.setId(1L);
                position.setOrder(1);
                position.setName("Position 1");
                positions.add(position);
                return positions;
            }
        };
        PositionServiceImpl service = new PositionServiceImpl(new PositionDaoImpl());
        service.getPositions();
    }


    @Test
    public void testGetActiveTimingPositions() throws Exception {
        new MockUp<PositionDaoImpl>() {
            @Mock
            public List<Position> getActiveTimingPositions() {
                List<Position> positions = new ArrayList<>();
                Position position = new Position();
                position.setId(1L);
                position.setOrder(1);
                position.setName("Position 1");
                positions.add(position);
                return positions;
            }
        };
        PositionServiceImpl service = new PositionServiceImpl(new PositionDaoImpl());
        service.getActiveTimingPositions();
    }
}
