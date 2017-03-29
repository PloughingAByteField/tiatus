package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.dao.ConfigDao;
import org.tiatus.dao.DaoException;

import javax.inject.Inject;
import java.io.InputStream;

/**
 * Created by johnreynolds on 29/03/2017.
 */
public class ConfigServiceImpl implements ConfigService {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigServiceImpl.class);

    private final ConfigDao dao;

    @Inject
    public ConfigServiceImpl(ConfigDao dao) {
        this.dao = dao;
    }

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
}
