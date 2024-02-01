package org.tiatus.role;

/**
 * Created by johnreynolds on 30/08/2016.
 */
public class Role {
    public static final String ADMIN = "ADMIN";
    public static final String ADJUDICATOR = "ADJUDICATOR";
    public static final String TIMING = "TIMING";

    private Role() {
        throw new IllegalAccessError("Utility class");
    }

}
