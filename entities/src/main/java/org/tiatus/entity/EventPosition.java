package org.tiatus.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "event_position", uniqueConstraints = { @UniqueConstraint(columnNames = { "position_id", "event_id", "position_order" }) })
public class EventPosition implements Serializable {

	private static final long serialVersionUID = 486592711029352048L;

	@Id
	@ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "position_id", nullable = false, updatable = true, insertable = true)
	private Position position;

	@Id
    @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "event_id", nullable = false, updatable = true, insertable = true)
	private Event event;

    @Column(name = "position_order")
    private Integer positionOrder;

	@Column(name = "can_start")
	private Boolean canStart;

	@Column(name = "finishing_position")
	private Boolean finishingPosition;

	@Column(name = "starting_position")
	private Boolean startingPosition;

	@JsonGetter("event")
	public Long getEntryId() {
		return event.getId();
	}

	@JsonGetter("position")
	public Long getPositionId() {
		return position.getId();
	}

	public Integer getPositionOrder() {
		return positionOrder;
	}

	public void setPositionOrder(Integer positionOrder) {
		this.positionOrder = positionOrder;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Boolean getCanStart() {
		return canStart;
	}

	public void setCanStart(Boolean canStart) {
		this.canStart = canStart;
	}

	public Boolean getFinishingPosition() {
		return finishingPosition;
	}

	public void setFinishingPosition(Boolean finishingPosition) {
		this.finishingPosition = finishingPosition;
	}

	public Boolean getStartingPosition() {
		return startingPosition;
	}

	public void setStartingPosition(Boolean startingPosition) {
		this.startingPosition = startingPosition;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.position)
				.append(this.event)
				.append(this.positionOrder)
				.toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof EventPosition)) {
			return false;
		}

		EventPosition entryPositionTime = (EventPosition) o;
		return new EqualsBuilder()
				.append(this.position, entryPositionTime.position)
				.append(this.event, entryPositionTime.event)
				.append(this.positionOrder, entryPositionTime.positionOrder)
				.isEquals();
	}
}
