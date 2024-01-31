package org.tiatus.role;

/**
 * Created by johnreynolds on 30/08/2016.
 */
public class Role {
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String ADJUDICATOR = "ROLE_ADJUDICATOR";
    public static final String TIMING = "ROLE_TIMING";

    private Role() {
        throw new IllegalAccessError("Utility class");
    }

}
