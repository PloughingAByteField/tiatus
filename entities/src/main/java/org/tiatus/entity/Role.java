package org.tiatus.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Created by johnreynolds on 26/08/2016.
 */
@Entity
@Table(name = "role")
public class Role implements Serializable {
    @Id
    @SequenceGenerator(name="role_id_sequence",
                       sequenceName="role_id_sequence",
                       allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_sequence")
    @Column(name = "id")
    private Long id;

    @Column(name = "role", unique = true)
    private String roleName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String role) {
        this.roleName = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Role)) {
            return false;
        }

        Role role1 = (Role) o;

        return new EqualsBuilder()
                .append(roleName, role1.roleName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(roleName)
                .toHashCode();
    }
}
