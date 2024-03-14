package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tiatus.dao.ConfigDao;
import org.tiatus.dao.DaoException;

import java.io.InputStream;

/**
 * Created by johnreynolds on 29/03/2017.
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigServiceImpl.class);

    @Autowired
    protected ConfigDao dao;

    @Override
    public void setEventFooter(String footer) throws ServiceException {
        try {
            dao.setEventFooter(footer);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public void setEventTitle(String title) throws ServiceException {
        try {
            dao.setEventTitle(title);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public String setEventLogo(InputStream stream, String fileName) throws ServiceException {
        try {
            return dao.setEventLogo(stream, fileName);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public String getEventTitle() {
        return dao.getEventTitle();
    }

    @Override
    public String getEventLogo() {
        return dao.getEventLogo();
    }

    @Override
    public String getEventFooter() {
        return dao.getEventFooter();
    }
}
