package org.tiatus.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "entry")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Entry implements Serializable {
	
	private static final long serialVersionUID = 8109059549918273315L;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "entry_club_xref", joinColumns = { @JoinColumn(name = "entry_id") }, inverseJoinColumns = { @JoinColumn(name = "club_id") })
	private Set<Club> clubs = new HashSet<>();

	@Column(name = "crew")
	private String crew;

    @Column(name = "weighting")
    private String weighting;
	
	@Column(name = "timeOnly")
	private boolean timeOnly = false;

	@Id  @GeneratedValue(strategy=GenerationType.IDENTITY, generator = "UseExistingOrGenerateIdGenerator")
	@GenericGenerator(name="UseExistingOrGenerateIdGenerator",
			strategy="org.tiatus.entity.UseExistingOrGenerateIdGenerator",
			parameters = {
					@org.hibernate.annotations.Parameter(name = "sequence_name", value = "entry_id_sequence")
			})
	@Column(name = "id")
	private Long id;
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "race_id")
    private Race race;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private Event event;

	@Column(name = "number")
    private Integer number;

	@Column(name = "raceOrder")
	private Integer raceOrder;

    @Column(name = "isFixedNumber")
    private boolean isFixedNumber = false;

	public Entry() {}

	public Entry(Long id) {
		this.setId(id);
	}

	public Entry(Entry entry) {
		crew = entry.crew;
		id = entry.id;
		event = entry.event;
		weighting = entry.weighting;
		clubs = entry.clubs;
		timeOnly = entry.timeOnly;
		race = entry.race;
		number = entry.number;
		raceOrder = entry.raceOrder;
		isFixedNumber = entry.isFixedNumber;
	}

    public String getWeighting() {
        return weighting;
    }

    public void setWeighting(String weighting) {
        this.weighting = weighting;
    }

    public Set<Club> getClubs() {
        return clubs;
    }

    public void setClubs(Set<Club> clubs) {
        this.clubs = clubs;
    }

    public boolean isFixedNumber() {
        return isFixedNumber;
    }

    public void setFixedNumber(boolean fixedNumber) {
        isFixedNumber = fixedNumber;
    }

    public Integer getRaceOrder() {
		return raceOrder;
	}

	public void setRaceOrder(Integer raceOrder) {
		this.raceOrder = raceOrder;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}
	
	public String getCrew() {
		return crew;
	}
	
	public void setCrew(String crew) {
		this.crew = crew;
	}
	
	public boolean isTimeOnly() {
		return timeOnly;
	}
	
	public void setTimeOnly(boolean timeOnly) {
		this.timeOnly = timeOnly;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((crew == null) ? 0 : crew.hashCode());
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((race == null) ? 0 : race.hashCode());
		result = prime * result + (timeOnly ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entry other = (Entry) obj;
		if (crew == null) {
			if (other.crew != null)
				return false;
		} else if (!crew.equals(other.crew))
			return false;
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (race == null) {
			if (other.race != null)
				return false;
		} else if (!race.equals(other.race))
			return false;
		if (timeOnly != other.timeOnly)
			return false;
		return true;
	}


}
