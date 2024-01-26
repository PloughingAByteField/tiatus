package org.tiatus.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * Created by johnreynolds on 12/11/2016.
 */
@Entity
@Table(name = "club")
public class Club implements Serializable {
    private static final long serialVersionUID = 8109059549918273345L;

    @Column(name = "clubName")
    private String clubName;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    public Club() {}

    public Club(Long id) {
        this.setId(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.id)
                .append(this.clubName)
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Club)) {
            return false;
        }

        Club club = (Club) o;
        return new EqualsBuilder()
                .append(this.id, club.id)
                .append(this.clubName, club.clubName)
                .isEquals();
    }
}
