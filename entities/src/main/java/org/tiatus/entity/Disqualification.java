package org.tiatus.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Created by johnreynolds on 12/11/2016.
 */
@Entity
@Table(name = "disqualification")
public class Disqualification implements Serializable {
    private static final long serialVersionUID = 8109059549918473345L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entry_id")
    private Entry entry;

    @Column(name = "note")
    private String note;

    @Column(name = "comment")
    private String comment;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonGetter("entry")
    public Long getEntryId() {
        return entry.getId();
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.id)
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Disqualification)) {
            return false;
        }

        Disqualification disqualification = (Disqualification) o;
        return new EqualsBuilder()
                .append(this.id, disqualification.id)
                .isEquals();
    }
}
