package org.tiatus.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by johnreynolds on 29/03/2017.
 */
public class ConfigDaoImpl implements ConfigDao {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigDaoImpl.class);

    @Override
    public void setEventFooter(String footer) throws DaoException {
        try {
            updateConfig("footer", footer);

        } catch (IOException e) {
            LOG.warn("Got io exception ", e);
            throw new DaoException(e);
        }
    }

    @Override
    public void setEventTitle(String title) throws DaoException {
        try {
            updateConfig("title", title);

        } catch (IOException e) {
            LOG.warn("Got io exception ", e);
            throw new DaoException(e);
        }
    }

    @Override
    public String setEventLogo(InputStream stream, String fileName) throws DaoException {
        String logoFileName = "/tiatus/" + fileName;
        File logoFile = new File(System.getProperty("jboss.home.dir") + logoFileName);
        logoFile.getParentFile().mkdirs();
        try (FileOutputStream fop = new FileOutputStream(logoFile)) {
            IOUtils.copy(stream, fop);
            fop.close();

            updateConfig("logo", logoFileName);

        } catch (IOException e) {
            LOG.warn("Got io exception ", e);
            throw new DaoException(e);
        }

        return logoFileName;
    }

    private synchronized void updateConfig(String key, String data) throws IOException {
        File configFile = new File(System.getProperty("jboss.home.dir") + "/tiatus/" + "config/config.json");
        configFile.getParentFile().mkdirs();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        if (!configFile.exists()) {
            LOG.debug("Creating file " + configFile.getAbsolutePath());
            root = mapper.createObjectNode();
        } else {
            root = mapper.readTree(configFile);
        }

        ((ObjectNode) root).put(key, data);
        mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        mapper.writeValue(configFile, root);
    }
}
