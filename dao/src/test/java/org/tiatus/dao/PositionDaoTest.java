package org.tiatus.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.tiatus.entity.Position;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 14/09/2016.
 */
@ExtendWith(MockitoExtension.class)
public class PositionDaoTest {

    // @Mock
    // private EntityManager entityManagerMock;

    // @Mock
    // private UserTransaction userTransactionMock;

    // @Mock
    // private TypedQuery typedQueryMock;

    // @Test
    // public void testAddPosition() throws DaoException {
    //     when(entityManagerMock.find(any(), any())).thenReturn(null);

    //     PositionDaoImpl dao = new PositionDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Position position = new Position();
    //     position.setId(1L);
    //     dao.addPosition(position);
    // }

    // @Test
    // public void testAddPositionExisting() throws DaoException {
    //     Position existingPosition = new Position();
    //     existingPosition.setId(1L);
    //     when(entityManagerMock.find(any(), any())).thenReturn(existingPosition);

    //     PositionDaoImpl dao = new PositionDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Position position = new Position();
    //     position.setId(1L);

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.addPosition(position);
    //     });
    // }

    // @Test
    // public void testAddPositionException() throws Exception {
    //     when(entityManagerMock.find(any(), any())).thenReturn(null);
    //     doThrow(HeuristicMixedException.class).when(userTransactionMock).commit();

    //     PositionDaoImpl dao = new PositionDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Position position = new Position();
    //     position.setId(1L);

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.addPosition(position);
    //     });
    // }

    // @Test
    // public void testGetPositions() {
    //     List<Position> list = new ArrayList<>();
    //     Position existingPosition = new Position();
    //     list.add(existingPosition);

    //     when(typedQueryMock.getResultList()).thenReturn(list);
    //     when(entityManagerMock.createQuery(any(), any())).thenReturn(typedQueryMock);


    //     PositionDaoImpl dao = new PositionDaoImpl();
    //     dao.em = entityManagerMock;
    //     Assertions.assertFalse(dao.getPositions().isEmpty());
    // }

    // @Test
    // public void testRemovePosition() throws Exception {
    //     Position existingPosition = new Position();
    //     existingPosition.setId(1L);

    //     when(entityManagerMock.find(any(), any())).thenReturn(existingPosition);
    //     when(entityManagerMock.contains(any())).thenReturn(false);

    //     PositionDaoImpl dao = new PositionDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Position position = new Position();
    //     position.setId(1L);
    //     dao.removePosition(position);
    // }

    // @Test
    // public void testRemovePositionNoPosition() throws Exception {
    //     when(entityManagerMock.find(any(), any())).thenReturn(null);

    //     PositionDaoImpl dao = new PositionDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Position position = new Position();
    //     position.setId(1L);
    //     dao.removePosition(position);
    // }

    // @Test 
    // public void testRemovePositionException() throws Exception {
    //     Position existingPosition = new Position();
    //     existingPosition.setId(1L);
    //     when(entityManagerMock.find(any(), any())).thenReturn(existingPosition);
    //     when(entityManagerMock.contains(any())).thenReturn(false);
    //     doThrow(HeuristicMixedException.class).when(userTransactionMock).commit();

    //     PositionDaoImpl dao = new PositionDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Position position = new Position();
    //     position.setId(1L);

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.removePosition(position);
    //     });
    // }

    // @Test
    // public void testUpdatePosition() throws Exception {
    //     PositionDaoImpl dao = new PositionDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Position position = new Position();
    //     position.setId(1L);
    //     dao.updatePosition(position);
    // }

    // @Test
    // public void testUpdatePositionException() throws Exception {
    //     doThrow(HeuristicMixedException.class).when(userTransactionMock).commit();

    //     PositionDaoImpl dao = new PositionDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Position position = new Position();
    //     position.setId(1L);

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.updatePosition(position);
    //     });
    // }
}
