package org.tiatus.dao;

import java.io.InputStream;

/**
 * Created by johnreynolds on 29/03/2017.
 */
public interface ConfigDao {
    void setEventFooter(String footer) throws DaoException;
    void setEventTitle(String title) throws DaoException;
    String setEventLogo(InputStream stream, String fileName) throws DaoException;
    String getEventTitle();
    String getEventLogo();
    String getEventFooter();
}
