package org.tiatus.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by johnreynolds on 26/08/2016.
 */
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "UseExistingOrGenerateIdGenerator")
    @GenericGenerator(name="UseExistingOrGenerateIdGenerator",
            strategy="org.tiatus.entity.UseExistingOrGenerateIdGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "role_id_sequence")
            })
    @Column(name = "id")
    private Long id;

    @Column(name = "role", unique = true)
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Role)) return false;

        Role role1 = (Role) o;

        return new EqualsBuilder()
                .append(role, role1.role)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(role)
                .toHashCode();
    }
}
