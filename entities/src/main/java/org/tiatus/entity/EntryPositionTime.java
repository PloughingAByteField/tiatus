package org.tiatus.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@NamedQuery(name = "findByRace", query = "select t from EntryPositionTime t where t.entry in (select e from Entry e where e.race = :race) order by t.entry, t.position")
@Table(name = "entry_position_time", uniqueConstraints = { @UniqueConstraint(columnNames = { "position_id", "entry_id" }) })
@IdClass(EntryPositionId.class)
public class EntryPositionTime implements Serializable {

	private static final long serialVersionUID = 486591711029352048L;

	@Id
	@ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "position_id", nullable = false, updatable = true, insertable = true)
	private Position position;

	@Id
    @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "entry_id", nullable = false, updatable = true, insertable = true)
	private Entry entry;
    
	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Column(name = "time")
	private Timestamp time;
	
    @Column(name = "synced")
	private Boolean synced;

    @Column(name = "start_point")
    private Boolean startPoint;

	@JsonGetter("entry")
	public Long getEntryId() {
		return entry.getId();
	}

	@JsonGetter("position")
	public Long getPositionId() {
		return position.getId();
	}

    public Boolean isStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Boolean startPoint) {
        this.startPoint = startPoint;
    }

    public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public Boolean getSynced() {
		return synced;
	}

	public void setSynced(Boolean synced) {
		this.synced = synced;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.position)
				.append(this.entry)
				.append(this.time)
				.toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof EntryPositionTime)) {
			return false;
		}

		EntryPositionTime entryPositionTime = (EntryPositionTime) o;
		return new EqualsBuilder()
				.append(this.position, entryPositionTime.position)
				.append(this.entry, entryPositionTime.entry)
				.append(this.time, entryPositionTime.time)
				.isEquals();
	}
}
