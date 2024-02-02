package org.tiatus.dao;

import org.tiatus.entity.Position;

import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
public interface PositionDao {
    /**
     * Get Position for a given id
     * @return Position or null
     */
    Position getPositionForId(Long id);

    /**
     * Get Positions
     * @return a list of Positions
     */
    List<Position> getPositions();

    /**
     * Add a new Position
     * @param position Position to add
     * @return Position Added race
     * @throws DaoException on error
     */
    Position addPosition(Position position) throws DaoException;

    /**
     * Remove a Position
     * @param position Position to remove
     * @throws DaoException on error
     */
    void removePosition(Position position) throws DaoException;

    /**
     * Update a Position
     * @param position Position to update
     * @throws DaoException on error
     */
    Position updatePosition(Position position) throws DaoException;

}
