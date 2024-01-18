package org.tiatus.service;

import org.junit.Test;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.PositionDaoImpl;
import org.tiatus.entity.Position;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class PositionServiceImplTest {

    @Test
    public void testConstructor() {
        PositionServiceImpl service = new PositionServiceImpl(new PositionDaoImpl(), new MessageSenderServiceImpl());
    }

    // @Test
    // public void testAddPosition() throws Exception {
    //     new MockUp<PositionDaoImpl>() {
    //         @Mock
    //         Position addPosition(Position position) throws DaoException {
    //             return position;
    //         }
    //     };
    //     new MockUp<MessageSenderServiceImpl>() {
    //         @Mock
    //         public void sendMessage(final Message obj) throws JMSException {}
    //     };
    //     PositionServiceImpl service = new PositionServiceImpl(new PositionDaoImpl(), new MessageSenderServiceImpl());
    //     Position position = new Position();
    //     service.addPosition(position, "id");
    // }

    // @Test (expected = ServiceException.class)
    // public void testAddPositionException() throws Exception {
    //     new MockUp<PositionDaoImpl>() {
    //         @Mock
    //         Position addPosition(Position position) throws DaoException {
    //             throw new DaoException("message");
    //         }
    //     };
    //     PositionServiceImpl service = new PositionServiceImpl(new PositionDaoImpl(), new MessageSenderServiceImpl());
    //     Position position = new Position();
    //     service.addPosition(position, "id");
    // }

    // @Test
    // public void testDeletePosition() throws Exception {
    //     new MockUp<PositionDaoImpl>() {
    //         @Mock
    //         public void removePosition(Position position) throws DaoException {}
    //     };
    //     PositionServiceImpl service = new PositionServiceImpl(new PositionDaoImpl(), new MessageSenderServiceImpl());
    //     Position position = new Position();
    //     service.removePosition(position, "id");
    // }

    // @Test (expected = ServiceException.class)
    // public void testDeletePositionException() throws Exception {
    //     new MockUp<PositionDaoImpl>() {
    //         @Mock
    //         public void removePosition(Position position) throws DaoException {
    //             throw new DaoException("message");
    //         }
    //     };
    //     PositionServiceImpl service = new PositionServiceImpl(new PositionDaoImpl(), new MessageSenderServiceImpl());
    //     Position position = new Position();
    //     service.removePosition(position, "id");
    // }

    // @Test
    // public void testUpdatePosition() throws Exception {
    //     new MockUp<PositionDaoImpl>() {
    //         @Mock
    //         public void updatePosition(Position position) throws DaoException {}
    //     };
    //     PositionServiceImpl service = new PositionServiceImpl(new PositionDaoImpl(), new MessageSenderServiceImpl());
    //     Position position = new Position();
    //     service.updatePosition(position, "id");
    // }

    // @Test (expected = ServiceException.class)
    // public void testUpdatePositionException() throws Exception {
    //     new MockUp<PositionDaoImpl>() {
    //         @Mock
    //         public void updatePosition(Position position) throws DaoException {
    //             throw new DaoException("message");
    //         }
    //     };
    //     PositionServiceImpl service = new PositionServiceImpl(new PositionDaoImpl(), new MessageSenderServiceImpl());
    //     Position position = new Position();
    //     service.updatePosition(position, "id");
    // }

    // @Test
    // public void testGetPositions() throws Exception {
    //     new MockUp<PositionDaoImpl>() {
    //         @Mock
    //         public List<Position> getPositions() {
    //             List<Position> positions = new ArrayList<>();
    //             Position position = new Position();
    //             position.setId(1L);
    //             position.setName("Position 1");
    //             positions.add(position);
    //             return positions;
    //         }
    //     };
    //     PositionServiceImpl service = new PositionServiceImpl(new PositionDaoImpl(), new MessageSenderServiceImpl());
    //     service.getPositions();
    // }
}
