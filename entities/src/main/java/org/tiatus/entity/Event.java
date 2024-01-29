package org.tiatus.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 10/10/2016.
 */
@Entity
@Table(name = "event")
public class Event implements Serializable {
	
	private static final long serialVersionUID = -8203697323141728439L;

	@Column(name = "name")
	private String name;

	@Column(name = "is_weighted")
	private boolean isWeighted = false;

	@Id  
	@SequenceGenerator(name="event_id_sequence",
                       sequenceName="event_id_sequence",
                       allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_id_sequence")
	@Column(name = "id")
	private Long id;


	@OneToMany(fetch = FetchType.EAGER, mappedBy = "event", orphanRemoval = true)
	@OrderBy("positionOrder")
	private List<EventPosition> positions = new ArrayList<>();

	public Event(){}

	public Event(Long id){
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isWeighted() {
		return isWeighted;
	}

	public void setWeighted(boolean weighted) {
		isWeighted = weighted;
	}

	public List<EventPosition> getPositions() {
		return positions;
	}

	public void setPositions(List<EventPosition> positions) {
		this.positions = positions;
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

		if (!(o instanceof Event)) {
			return false;
		}

		Event event = (Event) o;
		return new EqualsBuilder()
			.append(this.id, event.id)
			.append(this.name, event.name)
			.isEquals();
	}

	
}
