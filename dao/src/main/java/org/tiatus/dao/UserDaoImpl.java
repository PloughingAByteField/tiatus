package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Role;
import org.tiatus.entity.User;
import org.tiatus.entity.UserRole;

import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.*;
import java.util.List;
import java.util.Set;

/**
 * Created by johnreynolds on 02/09/2016.
 */
public class UserDaoImpl implements UserDao {

    private static final Logger LOG = LoggerFactory.getLogger(RaceDaoImpl.class);

    @PersistenceContext(unitName = "primary")
    protected EntityManager em;

    @Resource
    protected UserTransaction tx;

    @Override
    public boolean hasAdminUser() {
        TypedQuery<UserRole> query = em.createQuery("FROM UserRole where role = :role" , UserRole.class);
        Role admin = getRoleForRole("ADMIN");
        List<UserRole> users = query.setParameter("role", admin).getResultList();
        return users != null && !users.isEmpty();
    }

    @Override
    public User addUser(User user) throws DaoException {
        LOG.debug("Adding user " + user);
        try {
            tx.begin();
            User existing = null;
            if (user.getId() != null) {
                existing = em.find(User.class, user.getId());
            }
            if (existing == null) {

                em.persist(user);
                tx.commit();

                return user;

            } else {
                String message = "Failed to add user due to existing user with same id " + user.getId();
                LOG.warn(message);
                tx.rollback();
                throw new DaoException(message);
            }

        } catch (Exception e) {
            LOG.warn("Failed to persist user", e.getMessage());
            try { tx.rollback(); } catch (Exception se) {
                LOG.warn("Failed to rollback", se); }
            throw new DaoException(e);
        }
    }

    @Override
    public void deleteUser(User user) throws DaoException {
        try {
            User existing = null;
            if (user.getId() != null) {
                existing = em.find(User.class, user.getId());
            }
            if (existing != null) {
                tx.begin();
                em.remove(em.contains(user) ? user : em.merge(user));
                tx.commit();
            } else {
                LOG.warn("No such user of id " + user.getId());
            }
        } catch (Exception e) {
            LOG.warn("Failed to delete user", e.getMessage());
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e);
        }

    }

    @Override
    public void updateUser(User user) throws DaoException {
        try {
            tx.begin();
            User existing = null;
            if (user.getId() != null) {
                existing = em.find(User.class, user.getId());
            }
            if (existing != null) {
                if (user.getPassword().length() > 0) {
                    existing.setPassword(user.getPassword());
                }
                if (!user.getUserName().equals(existing.getUserName())) {
                    existing.setUserName(user.getUserName());
                }

                UserRole updatedRoles[] = new UserRole[user.getRoles().size()];
                user.getRoles().toArray(updatedRoles);
                UserRole existingRoles[] = new UserRole[existing.getRoles().size()];
                existing.getRoles().toArray(existingRoles);
                for (int i = 0; i < updatedRoles.length; i++) {
                    UserRole existingUserRole = existingRoles[i];
                    Role existingRole = existingUserRole.getRole();
                    UserRole updatedUserRole = updatedRoles[i];
                    Role updatedRole = updatedUserRole.getRole();
                    if (existingRole.getId() != updatedRole.getId()) {
                        existing.getRoles().remove(existingUserRole);
                        existing.getRoles().add(updatedUserRole);
                    }
                }
                em.merge(existing);
                tx.commit();
            } else {
                LOG.warn("No such user of id " + user.getId());
                tx.rollback();
            }

        } catch (Exception e) {
            LOG.warn("Failed to update user", e.getMessage());
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e);
        }
    }

    @Override
    public Role getRoleForRole(String role) {
        TypedQuery<Role> query = em.createQuery("FROM Role where role = :role ", Role.class);
        query.setParameter("role", role);
        if (query.getResultList().isEmpty()) {
            return null;
        }
        return query.getSingleResult();
    }

    @Override
    public User getUser(String userName, String password) {
        TypedQuery<User> query = em.createQuery("FROM User where user_name = :user_name and password = :password" , User.class);
        query.setParameter("user_name", userName);
        query.setParameter("password", password);
        if (query.getResultList().isEmpty()) {
            return null;
        }
        return query.getSingleResult();
    }

    @Override
    public List<User> getUsers() throws DaoException {
        TypedQuery<User> query = em.createQuery("FROM User", User.class);
        return query.getResultList();
    }

    @Override
    public List<Role> getUserRoles() throws DaoException {
        TypedQuery<Role> query = em.createQuery("FROM Role", Role.class);
        return query.getResultList();
    }
}
