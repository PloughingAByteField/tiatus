package org.tiatus.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "event_position", uniqueConstraints = { @UniqueConstraint(columnNames = { "position_id", "event_id", "position_order" }) })
public class EventPosition implements Serializable {

	private static final long serialVersionUID = 486592711029352048L;

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "position_id", nullable = false, updatable = true, insertable = true)
	private Position position;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false, updatable = true, insertable = true)
	private Event event;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

    @Column(name = "position_order")
    private Integer positionOrder;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@JsonGetter("event")
	public Long getEventId() {
		return event.getId();
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
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

		EventPosition eventPosition = (EventPosition) o;
		return new EqualsBuilder()
				.append(this.position, eventPosition.position)
				.append(this.event, eventPosition.event)
				.append(this.positionOrder, eventPosition.positionOrder)
				.isEquals();
	}
}
