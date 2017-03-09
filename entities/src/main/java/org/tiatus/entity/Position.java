package org.tiatus.entity;

import org.hibernate.annotations.GenericGenerator;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by johnreynolds on 11/11/2016.
 */
@Entity
@Table(name = "position")
public class Position implements Serializable {

	private static final long serialVersionUID = 623701671930478711L;

	@Column(name = "name", unique = true)
	private String name;
	
	@Id  @GeneratedValue(strategy=GenerationType.IDENTITY, generator = "UseExistingOrGenerateIdGenerator")
	@GenericGenerator(name="UseExistingOrGenerateIdGenerator",
			strategy="org.tiatus.entity.UseExistingOrGenerateIdGenerator",
			parameters = {
					@org.hibernate.annotations.Parameter(name = "sequence_name", value = "position_id_sequence")
			})
    @Column(name = "id")
	private Long id;

	public Position() {}

	public Position(Long id) {
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

		if (!(o instanceof Position)) {
			return false;
		}

		Position position = (Position) o;
		return new EqualsBuilder()
				.append(this.id, position.id)
				.append(this.name, position.name)
				.isEquals();
	}
}
