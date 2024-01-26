package org.tiatus.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by johnreynolds on 12/11/2016.
 */
@Entity
@Table(name = "race_position_template")
public class RacePositionTemplate implements Serializable {
    private static final long serialVersionUID = 8109059549918773345L;

    @Column(name = "name")
    private String name;

    @Column(name = "default_template")
    private Boolean defaultTemplate;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="RACE_ID", nullable=false, updatable=false)
    private Race race;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "template")
    @OrderBy("positionOrder")
    private Set<RacePositionTemplateEntry> templates = new HashSet<>();

    public RacePositionTemplate() {}

    public RacePositionTemplate(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDefaultTemplate() {
        return defaultTemplate;
    }

    public void setDefaultTemplate(Boolean defaultTemplate) {
        this.defaultTemplate = defaultTemplate;
    }

    @JsonGetter("race")
    public Long getRaceId() {
        return race.getId();
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Set<RacePositionTemplateEntry> getTemplates() {
        return templates;
    }

    public void setTemplates(Set<RacePositionTemplateEntry> templates) {
        this.templates = templates;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.id)
                .append(this.name)
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof RacePositionTemplate)) {
            return false;
        }

        RacePositionTemplate template = (RacePositionTemplate) o;
        return new EqualsBuilder()
                .append(this.id, template.id)
                .append(this.name, template.name)
                .isEquals();
    }
}
