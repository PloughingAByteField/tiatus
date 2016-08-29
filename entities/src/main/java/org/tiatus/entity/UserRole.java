package org.tiatus.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by johnreynolds on 26/08/2016.
 */
@Entity
@Table(name = "user_role")
//@Table(name = "user_role", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "role_id" }) })
public class UserRole {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "UseExistingOrGenerateIdGenerator")
    @GenericGenerator(name="UseExistingOrGenerateIdGenerator",
            strategy="org.tiatus.entity.UseExistingOrGenerateIdGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_role_id_sequence")
            })
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ROLE_ID", nullable=false, updatable=false)
    private Role role;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="USER_ID", nullable=false, updatable=false)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof UserRole)) return false;

        UserRole userRole = (UserRole) o;

        return new EqualsBuilder()
                .append(role, userRole.role)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(role)
                .toHashCode();
    }
}
