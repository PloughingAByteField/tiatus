package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.tiatus.dao.DaoException;
import org.tiatus.dao.UserDao;
import org.tiatus.entity.Role;
import org.tiatus.entity.User;
import org.tiatus.entity.UserRole;

import jakarta.jms.JMSException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by johnreynolds on 02/09/2016.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    protected final static String CACHE_LIST_NAME = "users";
    protected final static String CACHE_ENTRY_NAME = "user";
    protected final static String CACHE_ENTRY_NAME_BY_NAME = "user_by_name";
    protected final static String ROLES_CACHE_LIST_NAME = "roles";

    @Autowired
    protected UserDao dao;

    @Autowired
    protected MessageSenderService sender;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected CacheManager cacheManager;

    @Override
    public boolean hasAdminUser() {
        return dao.hasAdminUser();
    }

    @Override
    public User addAdminUser(User user, String sessionId) throws ServiceException {
        return addAdminUser(user, sessionId, false);
    }

    @Override
    public User addAdminUser(User user, String sessionId, boolean ignoreExisting) throws ServiceException {
        // do we have an existing user
        if (!ignoreExisting && hasAdminUser()) {
            LOG.warn("Already have an admin user ");
            throw new ServiceException("Existing user");
        }

        LOG.debug("Adding admin user " + user.getUserName());
        try {
            UserRole userRole = new UserRole();
            // get role for admin from db
            Role role = dao.getRoleForRole("ROLE_" + org.tiatus.role.Role.ADMIN);
            userRole.setRole(role);
            userRole.setUser(user);
            Set<UserRole> userRoleList = new HashSet<>();
            userRoleList.add(userRole);
            user.setRoles(userRoleList);
            String password = passwordEncoder.encode(user.getPassword());
            user.setPassword(password);
            
            return dao.addUser(user);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public User addUser(User user, String sessionId) throws ServiceException {
        LOG.debug("Adding user " + user.getUserName());
        try {
            String password = passwordEncoder.encode(user.getPassword());
            user.setPassword(password);
            User daoUser = dao.addUser(user);
            Message message = Message.createMessage(daoUser, MessageType.ADD, sessionId);
            sender.sendMessage(message);
            clearCache();

            return daoUser;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
            
        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteUser(User user, String sessionId) throws ServiceException {
        try {
            dao.deleteUser(user);
            Message message = Message.createMessage(user, MessageType.DELETE, sessionId);
            sender.sendMessage(message);
            updateCache(user);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);

        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public User updateUser(User user, String sessionId) throws ServiceException {
        try {
            if (user.getPassword() != null) {
                String password = passwordEncoder.encode(user.getPassword());
                user.setPassword(password);
            }
            User updatedUser = dao.updateUser(user);
            Message message = Message.createMessage(updatedUser, MessageType.UPDATE, sessionId);
            sender.sendMessage(message);
            updateCache(user);

            return updatedUser;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);

        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    @Cacheable(value = CACHE_ENTRY_NAME_BY_NAME, key = "#userName")
    public User getUser(String userName, String password) {
        return dao.getUser(userName, password);
    }

    @Override
    @Cacheable(value = CACHE_LIST_NAME)
    public List<User> getUsers() throws ServiceException {
        try {
            return dao.getUsers();

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    @Cacheable(value = ROLES_CACHE_LIST_NAME)
    public List<Role> getUserRoles() throws ServiceException {
        try {
            return dao.getUserRoles();

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    @Cacheable(value = CACHE_ENTRY_NAME, key = "#id")
    public User getUserForId(Long id) {
        return dao.getUserForId(id);
    }

    private void updateCache(User user) {
        Cache cache = cacheManager.getCache(CACHE_ENTRY_NAME);
        if (cache != null) {
            cache.evictIfPresent(user.getId().longValue());
        }

        cache = cacheManager.getCache(CACHE_ENTRY_NAME_BY_NAME);
        if (cache != null) {
            cache.evictIfPresent(user.getUserName());
        }

        clearCache();
    }

    private void clearCache() {
        Cache cache = cacheManager.getCache(CACHE_LIST_NAME);
        if (cache != null) {
            cache.clear();
        }
    }
}
