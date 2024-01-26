package org.tiatus.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Created by johnreynolds on 26/08/2016.
 */
@Entity
@Table(name = "user_role", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "role_id" }) })
public class UserRole implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="ROLE_ID", nullable=false, updatable=false)
    private Role role;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="USER_ID", nullable=false, updatable=false)
    @JsonBackReference(value="user-role")
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
        if (this == o) {
            return true;
        }

        if (!(o instanceof UserRole)) {
            return false;
        }

        UserRole userRole = (UserRole) o;

        return new EqualsBuilder()
                .append(role, userRole.role)
                .append(user, userRole.user)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(role)
                .append(user)
                .toHashCode();
    }
}
