package org.tiatus.auth;

import org.tiatus.entity.User;

import javax.security.auth.Subject;
import java.security.Principal;

/**
 * Created by johnreynolds on 26/08/2016.
 */
public class UserPrincipal implements Principal {

    private User user;

    @Override
    public String getName() {
        return getUser().getUserName();
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
