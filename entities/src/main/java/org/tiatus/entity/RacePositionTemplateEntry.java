package org.tiatus.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "race_position_template_entry", uniqueConstraints = { @UniqueConstraint(columnNames = { "template_id", "position_id", "position_order" }) })
public class RacePositionTemplateEntry implements Serializable {

	private static final long serialVersionUID = 486592711129352048L;

	@Id
	@ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "position_id", nullable = false, updatable = true, insertable = true)
	private Position position;

	@Id
    @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "template_id", nullable = false, updatable = true, insertable = true)
	private RacePositionTemplate template;

    @Column(name = "position_order")
    private Integer positionOrder;

	@Column(name = "can_start")
	private Boolean canStart;

	@Column(name = "finishing_position")
	private Boolean finishingPosition;

	@Column(name = "starting_position")
	private Boolean startingPosition;

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

	@JsonGetter("template")
	public Long getTemplateId() {
		return template.getId();
	}

	public RacePositionTemplate getTemplate() {
		return template;
	}

	public void setTemplate(RacePositionTemplate template) {
		this.template = template;
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
				.append(this.template)
				.append(this.positionOrder)
				.toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof RacePositionTemplateEntry)) {
			return false;
		}

		RacePositionTemplateEntry templateEntry = (RacePositionTemplateEntry) o;
		return new EqualsBuilder()
				.append(this.position, templateEntry.position)
				.append(this.template, templateEntry.template)
				.append(this.positionOrder, templateEntry.positionOrder)
				.isEquals();
	}
}
