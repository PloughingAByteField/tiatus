package org.tiatus.dao;

import org.tiatus.entity.Position;

import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
public interface PositionDao {
    /**
     * Get Positions
     * @return a list of Positions
     */
    List<Position> getPositions();

    /**
     * Get Active Timing Positions
     * @return a list of Active Timing Positions
     */
    List<Position> getActiveTimingPositions();

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
    void updatePosition(Position position) throws DaoException;

}
