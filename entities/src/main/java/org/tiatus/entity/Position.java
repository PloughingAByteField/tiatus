package org.tiatus.entity;

import org.hibernate.annotations.GenericGenerator;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "position", uniqueConstraints = { @UniqueConstraint(columnNames = { "timing", "positionOrder" }) })
public class Position implements Serializable {

	private static final long serialVersionUID = 623701671930478711L;

	@Column(name = "name", unique = true)
	private String name;
	
	@Column(name = "active")
	private boolean active;
	
	@Column(name = "showAllEntries")
	private boolean showAllEntries;

	@Column(name = "positionOrder")
	private Integer order;

	@Column(name = "timing")
	private boolean timing;

	@Column(name = "can_start")
	private boolean canStart = false;

	@Id  @GeneratedValue(strategy=GenerationType.IDENTITY, generator = "UseExistingOrGenerateIdGenerator")
	@GenericGenerator(name="UseExistingOrGenerateIdGenerator",
			strategy="org.tiatus.entity.UseExistingOrGenerateIdGenerator",
			parameters = {
					@org.hibernate.annotations.Parameter(name = "sequence_name", value = "position_id_sequence")
			})
    @Column(name = "id")
	private Long id;

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public boolean isTiming() {
		return timing;
	}

	public void setTiming(boolean timing) {
		this.timing = timing;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isShowAllEntries() {
		return showAllEntries;
	}

	public void setShowAllEntries(boolean showAllEntries) {
		this.showAllEntries = showAllEntries;
	}

	public boolean isCanStart() {
		return canStart;
	}

	public void setCanStart(boolean canStart) {
		this.canStart = canStart;
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
