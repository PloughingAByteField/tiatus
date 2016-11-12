package org.tiatus.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by johnreynolds on 12/11/2016.
 */
@Entity
@Table(name = "club")
public class Club implements Serializable {
    private static final long serialVersionUID = 8109059549918273345L;

    @Column(name = "club")
    private String club;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator = "UseExistingOrGenerateIdGenerator")
    @GenericGenerator(name="UseExistingOrGenerateIdGenerator",
            strategy="org.tiatus.entity.UseExistingOrGenerateIdGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "club_id_sequence")
            })
    @Column(name = "id")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.id)
                .append(this.club)
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
                .append(this.club, club.club)
                .isEquals();
    }
}
