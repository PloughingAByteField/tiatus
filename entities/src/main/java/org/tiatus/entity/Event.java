package org.tiatus.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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

	@Id  @GeneratedValue(strategy=GenerationType.IDENTITY, generator = "UseExistingOrGenerateIdGenerator")
	@GenericGenerator(name="UseExistingOrGenerateIdGenerator",
			strategy="org.tiatus.entity.UseExistingOrGenerateIdGenerator",
			parameters = {
					@org.hibernate.annotations.Parameter(name = "sequence_name", value = "event_id_sequence")
			})
	@Column(name = "id")
	private Long id;


	@OneToMany(fetch = FetchType.EAGER, mappedBy = "event", orphanRemoval = true)
	@OrderBy("positionOrder")
	private Set<EventPosition> positions = new HashSet<>();

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

	public Set<EventPosition> getPositions() {
		return positions;
	}

	public void setPositions(Set<EventPosition> positions) {
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
