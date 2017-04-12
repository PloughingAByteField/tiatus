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

    private static final String JBOSS_HOME_DIR = "jboss.home.dir";

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
        File logoFile = new File(System.getProperty(JBOSS_HOME_DIR) + logoFileName);
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

    @Override
    public String getEventTitle() {
        return getKeyFromConfig("title");
    }

    @Override
    public String getEventLogo() {
        return getKeyFromConfig("logo");
    }

    private File getConfigFile() {
        return new File(System.getProperty(JBOSS_HOME_DIR) + "/tiatus/" + "config/config.json");
    }

    private String getKeyFromConfig(String key) {
        File configFile = getConfigFile();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        try {
            root = mapper.readTree(configFile);
        } catch (IOException e) {
            LOG.warn("Failed to read config file", e);
            return null;
        }

        JsonNode node = root.path(key);
        if (! node.isMissingNode()) {
            return node.asText();
        }

        return null;
    }

    private synchronized void updateConfig(String key, String data) throws IOException {
        File configFile = getConfigFile();
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
