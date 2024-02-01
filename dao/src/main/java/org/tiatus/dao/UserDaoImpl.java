package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tiatus.entity.Role;
import org.tiatus.entity.User;
import org.tiatus.entity.UserRole;

import java.util.List;

/**
 * Created by johnreynolds on 02/09/2016.
 */
@Service
public class UserDaoImpl implements UserDao {

    private static final Logger LOG = LoggerFactory.getLogger(RaceDaoImpl.class);

    @Autowired 
    private UserRepository repository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Override
    public boolean hasAdminUser() {
        Role admin = getRoleForRole("ADMIN");
        List<UserRole> users = userRoleRepository.findByRole(admin);
        return users != null && !users.isEmpty();
    }

    @Override
    public User addUser(User user) throws DaoException {
        LOG.debug("Adding user " + user.getUserName());
        try {
            if (user.getId() == null) {
                return repository.save(user);

            } else {
                String message = "Failed to add user due to existing user with same id " + user.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }

        } catch (Exception e) {
            LOG.warn("Failed to persist user", e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void deleteUser(User user) throws DaoException {
        try {
            if (repository.existsById(user.getId())) {
                repository.delete(user);

            } else {
                LOG.warn("No such user of id " + user.getId());
            }
        } catch (Exception e) {
            LOG.warn("Failed to delete user", e.getMessage());
            throw new DaoException(e);
        }

    }

    @Override
    public User updateUser(User user) throws DaoException {
        try {
            User existing = null;
            if (user.getId() != null) {
                existing = repository.findById(user.getId()).orElse(null);
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
                return repository.save(existing);

            } else {
                LOG.warn("No such user of id " + user.getId());
            }

            return user;

        } catch (Exception e) {
            LOG.warn("Failed to update user", e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public Role getRoleForRole(String role) {
        return roleRepository.findByRoleName(role);
    }

    @Override
    public User getUser(String userName, String password) {
        return repository.findByUserNameAndPassword(userName, password);
    }

    @Override
    public List<User> getUsers() throws DaoException {
        return repository.findAll();
    }

    @Override
    public List<Role> getUserRoles() throws DaoException {
        return roleRepository.findAll();
    }

    @Override
    public User getUserForId(Long id) {
        return repository.findById(id).orElse(null);
    }
}
